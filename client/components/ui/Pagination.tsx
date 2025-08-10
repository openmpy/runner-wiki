"use client";

import Button from "./Button";

interface PaginationProps {
  currentPage: number;
  totalCount: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  className?: string;
}

export function Pagination({
  currentPage,
  totalCount,
  pageSize,
  onPageChange,
  className = "",
}: PaginationProps) {
  const totalPages = Math.ceil(totalCount / pageSize);

  if (totalPages <= 1) {
    return null;
  }

  const getVisiblePages = () => {
    const pages = [];
    const maxVisiblePages = 5;

    // 현재 페이지를 중심으로 최대 5개까지 표시
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    const endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    // endPage가 totalPages에 도달했을 때 startPage 조정
    if (endPage === totalPages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return pages;
  };

  const visiblePages = getVisiblePages();

  return (
    <div
      className={`flex items-center justify-center gap-1 sm:gap-2 ${className}`}
    >
      <Button
        variant="secondary"
        size="sm"
        onClick={() => onPageChange(Math.max(1, currentPage - 5))}
        disabled={currentPage <= 1}
        className="flex items-center gap-1 px-2 sm:px-3"
      >
        &lt;&lt;
      </Button>

      <Button
        variant="secondary"
        size="sm"
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage <= 1}
        className="flex items-center gap-1 px-2 sm:px-3"
      >
        &lt;
      </Button>

      {/* 모바일에서는 현재 페이지만 표시, 데스크톱에서는 모든 페이지 표시 */}
      <div className="hidden sm:flex items-center gap-1">
        {visiblePages.map((page, index) => (
          <Button
            key={index}
            variant={currentPage === page ? "primary" : "secondary"}
            size="sm"
            onClick={() => onPageChange(page)}
            className="min-w-[40px]"
          >
            {page}
          </Button>
        ))}
      </div>

      {/* 모바일에서는 현재 페이지만 표시 */}
      <div className="sm:hidden">
        <span className="px-3 py-2 text-sm font-medium text-gray-700">
          {currentPage}
        </span>
      </div>

      <Button
        variant="secondary"
        size="sm"
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages}
        className="flex items-center gap-1 px-2 sm:px-3"
      >
        &gt;
      </Button>

      <Button
        variant="secondary"
        size="sm"
        onClick={() => onPageChange(Math.min(totalPages, currentPage + 5))}
        disabled={currentPage >= totalPages}
        className="flex items-center gap-1 px-2 sm:px-3"
      >
        &gt;&gt;
      </Button>
    </div>
  );
}
