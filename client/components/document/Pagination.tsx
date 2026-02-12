"use client";

import { MAX_PAGE } from "@/lib/api/document";
import { Page } from "@/lib/types/global";
import Link from "next/link";
import { useEffect, useState } from "react";
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";

interface PaginationProps {
  pagination: Page<unknown>;
  currentCategory?: string;
  basePath: string;
}

export default function Pagination({
  pagination,
  currentCategory,
  basePath,
}: PaginationProps) {
  const { page, totalPages, hasNext, hasPrevious } = pagination;
  const effectiveTotalPages = Math.min(totalPages, MAX_PAGE);
  const effectiveHasNext = hasNext && page + 1 < MAX_PAGE;
  const [maxVisible, setMaxVisible] = useState(5);

  useEffect(() => {
    const updateMaxVisible = () => {
      setMaxVisible(window.innerWidth <= 768 ? 3 : 5);
    };

    updateMaxVisible();
    window.addEventListener("resize", updateMaxVisible);
    return () => window.removeEventListener("resize", updateMaxVisible);
  }, []);

  // 페이지 번호 배열 생성 (현재 페이지 주변 maxVisible개, 최대 1만 페이지까지)
  const getPageNumbers = () => {
    const pages: (number | string)[] = [];
    const lastPageIndex = effectiveTotalPages - 1;
    let start = Math.max(0, page - Math.floor(maxVisible / 2));
    const end = Math.min(lastPageIndex, start + maxVisible - 1);

    // 끝에 도달했을 때 시작점 조정
    if (end - start < maxVisible - 1) {
      start = Math.max(0, end - maxVisible + 1);
    }

    // 첫 페이지
    if (start > 0) {
      pages.push(0);
      if (start > 1) {
        pages.push("...");
      }
    }

    // 중간 페이지들
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    // 마지막 페이지
    if (end < lastPageIndex) {
      if (end < lastPageIndex - 1) {
        pages.push("...");
      }
      pages.push(lastPageIndex);
    }

    return pages;
  };

  const buildPageUrl = (pageNum: number) => {
    const params = new URLSearchParams();
    if (currentCategory && currentCategory !== "ALL") {
      params.set("category", currentCategory);
    }
    params.set("page", Math.min(pageNum, MAX_PAGE - 1).toString());
    return `${basePath}?${params.toString()}`;
  };

  const pageNumbers = getPageNumbers();

  if (effectiveTotalPages <= 1) {
    return null;
  }

  return (
    <div className="flex items-center justify-center gap-2 mt-8">
      {/* 이전 버튼 */}
      <Link
        href={hasPrevious ? buildPageUrl(Math.max(0, page - 1)) : "#"}
        className={`flex items-center justify-center w-7 h-7 rounded border transition-colors ${
          hasPrevious
            ? "border-gray-300 hover:bg-gray-100 text-gray-700 dark:border-zinc-700 dark:hover:bg-zinc-500 dark:text-zinc-200"
            : "border-gray-200 text-gray-400 dark:border-zinc-800 dark:text-gray-600 cursor-not-allowed"
        }`}
        aria-label="이전 페이지"
      >
        <LuChevronLeft className="w-5 h-5" />
      </Link>

      {/* 페이지 번호들 */}
      <div className="flex items-center gap-1">
        {pageNumbers.map((pageNum, index) => {
          if (pageNum === "...") {
            return (
              <span key={`ellipsis-${index}`} className="px-1 text-gray-400">
                ...
              </span>
            );
          }

          const pageIndex = pageNum as number;
          const isActive = pageIndex === page;

          return (
            <Link
              key={pageIndex}
              href={buildPageUrl(pageIndex)}
              className={`flex items-center justify-center px-2 h-7 rounded border transition-colors text-xs ${
                isActive
                  ? "bg-mint dark:bg-zinc-700 text-white border-mint dark:border-zinc-700 font-semibold dark:text-zinc-200"
                  : "border-gray-300 hover:bg-gray-100 text-gray-700 dark:border-zinc-700 dark:hover:bg-zinc-500 dark:text-zinc-200"
              }`}
              aria-label={`${pageIndex + 1}페이지`}
              aria-current={isActive ? "page" : undefined}
            >
              {pageIndex + 1}
            </Link>
          );
        })}
      </div>

      {/* 다음 버튼 */}
      <Link
        href={effectiveHasNext ? buildPageUrl(page + 1) : "#"}
        className={`flex items-center justify-center w-7 h-7 rounded border transition-colors ${
          effectiveHasNext
            ? "border-gray-300 hover:bg-gray-100 text-gray-700 dark:border-zinc-700 dark:hover:bg-zinc-500 dark:text-zinc-200"
            : "border-gray-200 text-gray-400 dark:border-zinc-800 dark:text-gray-600 cursor-not-allowed"
        }`}
        aria-label="다음 페이지"
      >
        <LuChevronRight className="w-5 h-5" />
      </Link>
    </div>
  );
}
