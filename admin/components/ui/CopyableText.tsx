"use client";

import { useRef, useState } from "react";

interface CopyableTextProps {
  text: string;
  className?: string;
}

export default function CopyableText({
  text,
  className = "",
}: CopyableTextProps) {
  const [showToast, setShowToast] = useState(false);
  const [toastPosition, setToastPosition] = useState({ x: 0, y: 0 });
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);

  const handleCopy = async (event: React.MouseEvent) => {
    try {
      await navigator.clipboard.writeText(text);

      // 이전 타이머가 있다면 취소
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }

      setToastPosition({ x: event.clientX, y: event.clientY });
      setShowToast(true);

      // 새로운 타이머 설정
      timeoutRef.current = setTimeout(() => {
        setShowToast(false);
        timeoutRef.current = null;
      }, 2000);
    } catch (error) {
      console.error("클립보드 복사 실패:", error);
    }
  };

  return (
    <div className="relative">
      <button
        onClick={handleCopy}
        className={`hover:text-mint transition-colors cursor-pointer ${className}`}
        title="클릭하여 복사"
      >
        {text}
      </button>

      {showToast && (
        <div
          className="fixed pointer-events-none z-50"
          style={{
            left: toastPosition.x,
            top: toastPosition.y + 20,
            transform: "translateX(-50%)",
          }}
        >
          <div className="bg-mint text-white text-sm rounded-md shadow-lg px-3 py-1 pointer-events-none font-bm-hanna">
            복사 완료!
          </div>
        </div>
      )}
    </div>
  );
}
