"use client";

import { bmhanna } from "@/app/layout";
import { useEffect, useState } from "react";

interface TocItem {
  id: string;
  text: string;
  level: number;
  number: string;
}

interface TableOfContentsProps {
  content: string;
}

export default function TableOfContents({ content }: TableOfContentsProps) {
  const [tocItems, setTocItems] = useState<TocItem[]>([]);

  useEffect(() => {
    // 계층적 번호를 생성하는 함수
    const generateNumbering = (items: Array<{ level: number }>) => {
      const counters = [0, 0, 0, 0, 0, 0]; // h1~h6까지의 카운터
      const numbers: string[] = [];

      items.forEach((item) => {
        const level = item.level - 1; // 배열 인덱스로 변환

        // 현재 레벨의 카운터 증가
        counters[level]++;

        // 상위 레벨들의 카운터는 유지, 하위 레벨들의 카운터는 리셋
        for (let i = level + 1; i < counters.length; i++) {
          counters[i] = 0;
        }

        // 번호 생성 (예: 1, 1-1, 1-1-1)
        const number = counters
          .slice(0, level + 1)
          .filter((count) => count > 0)
          .join("-");

        numbers.push(number);
      });

      return numbers;
    };

    // 마크다운 콘텐츠에서 헤딩을 파싱하는 함수
    const parseMarkdownHeadings = (markdown: string) => {
      const lines = markdown.split("\n");
      const items: Array<{ id: string; text: string; level: number }> = [];

      lines.forEach((line) => {
        // 마크다운 헤딩 패턴 매칭 (#, ##, ###, etc.)
        const headingMatch = line.match(/^(#{1,6})\s+(.+)$/);
        if (headingMatch) {
          const level = headingMatch[1].length;
          const text = headingMatch[2].trim();
          const id = `heading-${items.length}`;

          items.push({
            id,
            text,
            level,
          });
        }
      });

      // 번호 생성
      const numbers = generateNumbering(items);

      // TocItem 형태로 변환
      return items.map((item, index) => ({
        ...item,
        number: numbers[index],
      }));
    };

    // DOM에서 헤딩을 찾는 함수 (백업 방법)
    const findDOMHeadings = () => {
      const viewerContainer = document.querySelector(
        ".toastui-editor-contents"
      );
      if (!viewerContainer) {
        console.log("Toast UI Editor 컨테이너를 찾을 수 없습니다.");
        return [];
      }

      const headings = viewerContainer.querySelectorAll(
        "h1, h2, h3, h4, h5, h6"
      );
      console.log("DOM에서 찾은 헤딩 개수:", headings.length);

      const items: Array<{ id: string; text: string; level: number }> = [];
      headings.forEach((heading, index) => {
        const level = parseInt(heading.tagName.charAt(1));
        const text = heading.textContent || "";
        const id = `heading-${index}`;

        heading.id = id;
        items.push({
          id,
          text,
          level,
        });
      });

      // 번호 생성
      const numbers = generateNumbering(items);

      // TocItem 형태로 변환
      return items.map((item, index) => ({
        ...item,
        number: numbers[index],
      }));
    };

    // 먼저 마크다운에서 파싱 시도
    const items = parseMarkdownHeadings(content);

    // 마크다운에서 헤딩을 찾지 못한 경우 DOM에서 찾기
    if (items.length === 0) {
      console.log(
        "마크다운에서 헤딩을 찾지 못했습니다. DOM에서 찾아보겠습니다."
      );
      setTimeout(() => {
        const domItems = findDOMHeadings();
        setTocItems(domItems);
      }, 1000);
    } else {
      setTocItems(items);
    }
  }, [content]);

  const scrollToHeading = (id: string) => {
    const element = document.getElementById(id);
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    } else {
      // ID로 찾지 못한 경우 텍스트로 찾기
      const headings = document.querySelectorAll("h1, h2, h3, h4, h5, h6");
      const targetHeading = Array.from(headings).find((heading) =>
        heading.textContent?.includes(
          tocItems.find((item) => item.id === id)?.text || ""
        )
      );
      if (targetHeading) {
        targetHeading.scrollIntoView({ behavior: "smooth" });
      }
    }
  };

  // 목차가 없으면 아무것도 렌더링하지 않음
  if (tocItems.length === 0) {
    return null;
  }

  return (
    <div className="border border-gray-200 rounded-lg p-4 mb-6">
      <h3 className={`${bmhanna.className} text-xl mb-3 text-gray-800`}>
        목차
      </h3>
      <nav>
        <ul className="space-y-1">
          {tocItems.map((item) => (
            <li key={item.id}>
              <button
                onClick={() => scrollToHeading(item.id)}
                className={`text-left w-full text-sm ${
                  item.level === 1
                    ? "text-base font-medium"
                    : item.level === 2
                    ? "font-medium ml-2"
                    : "ml-2"
                }`}
                style={{ paddingLeft: `${(item.level - 1) * 8}px` }}
              >
                <span className="text-[#00A495] mr-2">{item.number}.</span>
                {item.text}
              </button>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
}
