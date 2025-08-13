"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { FaRandom } from "react-icons/fa";

const ShuffleButton = ({
  size = "default",
  className = "",
}: {
  size?: "small" | "default";
  className?: string;
}) => {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const handleShuffle = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/documents/random`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      router.push(`/document/${data.documentId}`);
    } catch (error) {
      alert("문서를 불러오지 못했습니다.");
      console.error("랜덤 문서 조회 중 오류 발생:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const buttonSize = size === "small" ? "w-8 h-8" : "w-10 h-10";
  const iconSize = size === "small" ? "text-sm" : "text-lg";
  const spinnerSize = size === "small" ? "w-3 h-3" : "w-4 h-4";

  return (
    <button
      type="button"
      onClick={handleShuffle}
      disabled={isLoading}
      className={`${buttonSize} bg-white rounded-lg flex items-center justify-center border border-transparent hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 ${className}`}
      aria-label="랜덤 문서 보기"
      title="랜덤 문서 보기"
    >
      {isLoading ? (
        <div
          className={`${spinnerSize} border-2 border-gray-300 border-t-mint rounded-full animate-spin`}
        ></div>
      ) : (
        <FaRandom className={`text-mint ${iconSize}`} />
      )}
    </button>
  );
};

export default ShuffleButton;
