"use client";

import "@toast-ui/editor/toastui-editor.css";
import dynamic from "next/dynamic";
import { useEffect, useRef } from "react";

const Viewer = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Viewer),
  {
    ssr: false,
    loading: () => (
      <div className="text-center text-gray-500">뷰어 로딩 중...</div>
    ),
  }
);

interface TuiViewerProps {
  content: string;
  onTocReady?: (
    tocItems: Array<{ id: string; text: string; level: number }>
  ) => void;
}

export default function TuiViewer({ content, onTocReady }: TuiViewerProps) {
  const viewerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    // 뷰어가 렌더링된 후 헤딩에 ID 추가
    const addHeadingIds = () => {
      if (!viewerRef.current) return;

      const headings = viewerRef.current.querySelectorAll(
        "h1, h2, h3, h4, h5, h6"
      );
      const tocItems: Array<{ id: string; text: string; level: number }> = [];

      headings.forEach((heading, index) => {
        const level = parseInt(heading.tagName.charAt(1));
        const text = heading.textContent || "";
        const id = `heading-${index}`;

        heading.id = id;

        tocItems.push({
          id,
          text,
          level,
        });
      });

      if (onTocReady) {
        onTocReady(tocItems);
      }
    };

    // 뷰어 렌더링 완료 후 실행
    const timer = setTimeout(addHeadingIds, 100);
    return () => clearTimeout(timer);
  }, [content, onTocReady]);

  return (
    <div className="w-full" ref={viewerRef}>
      <Viewer initialValue={content} />
    </div>
  );
}
