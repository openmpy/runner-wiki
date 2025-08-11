"use client";

import { useEffect, useState } from "react";

interface TableOfContentsProps {
  content: string;
}

interface TocItem {
  text: string;
  level: number;
  order: number;
  number: string; // 계층 번호 추가
}

export default function TableOfContents({ content }: TableOfContentsProps) {
  const [tocItems, setTocItems] = useState<TocItem[]>([]);

  // 계층 번호를 생성하는 함수
  const generateNumber = (items: TocItem[], currentIndex: number): string => {
    // 현재 레벨보다 높은 레벨의 마지막 번호를 찾습니다
    let number = "";
    const levelCounts: number[] = [0, 0, 0, 0, 0, 0]; // h1~h6까지의 카운트

    for (let i = 0; i <= currentIndex; i++) {
      const item = items[i];
      const level = item.level - 1; // 배열 인덱스는 0부터 시작

      if (level < levelCounts.length) {
        levelCounts[level]++;

        // 현재 아이템인 경우 번호 생성
        if (i === currentIndex) {
          // 상위 레벨들의 번호를 조합
          for (let j = 0; j <= level; j++) {
            if (levelCounts[j] > 0) {
              number += levelCounts[j];
              if (j < level) number += ".";
            }
          }
          break;
        }

        // 하위 레벨들은 리셋
        for (let j = level + 1; j < levelCounts.length; j++) {
          levelCounts[j] = 0;
        }
      }
    }

    return number;
  };

  useEffect(() => {
    if (!content) return;

    // 마크다운 헤딩을 파싱하여 목차 아이템 생성
    const lines = content.split("\n");
    const items: TocItem[] = [];
    let order = 0;

    lines.forEach((line) => {
      const headingMatch = line.match(/^(#{1,6})\s+(.+)$/);
      if (headingMatch) {
        const level = headingMatch[1].length;
        const text = headingMatch[2].trim();

        items.push({ text, level, order, number: "" });
        order++;
      }
    });

    // 각 아이템에 계층 번호 할당
    items.forEach((item, index) => {
      item.number = generateNumber(items, index);
    });

    setTocItems(items);
  }, [content]);

  if (tocItems.length === 0) {
    return null;
  }

  const scrollToHeading = (order: number) => {
    // ToastViewer가 렌더링한 컨텐츠 내부의 헤딩을 찾습니다
    const contentContainer = document.querySelector(".toastui-editor-contents");
    if (!contentContainer) return;

    const headings = contentContainer.querySelectorAll(
      "h1, h2, h3, h4, h5, h6"
    );

    // order에 해당하는 헤딩으로 이동
    if (headings[order]) {
      const targetHeading = headings[order] as HTMLElement;

      // 부드럽게 스크롤
      targetHeading.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });

      // 하이라이트 효과
      targetHeading.style.backgroundColor = "#fef3c7";
      targetHeading.style.transition = "background-color 0.3s ease";

      setTimeout(() => {
        targetHeading.style.backgroundColor = "";
      }, 2000);
    }
  };

  return (
    <div className="border border-gray-200 rounded-lg p-4 mb-6 w-fit">
      <h2 className="text-lg font-bm-hanna mb-3 text-gray-800">목차</h2>
      <nav>
        <ul className="space-y-1">
          {tocItems.map((item, index) => (
            <li key={index}>
              <button
                onClick={() => scrollToHeading(item.order)}
                className={`text-left cursor-pointer text-gray-800 text-md ${
                  item.level === 1
                    ? "ml-0"
                    : item.level === 2
                    ? "ml-3"
                    : item.level === 3
                    ? "ml-6"
                    : item.level === 4
                    ? "ml-9"
                    : item.level === 5
                    ? "ml-12"
                    : "ml-15"
                }`}
              >
                <span className="text-mint mr-1 inline-block">
                  {item.number}.
                </span>
                {item.text}
              </button>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
}
