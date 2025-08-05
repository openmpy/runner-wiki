interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export default function Pagination({
  currentPage,
  totalPages,
  onPageChange,
}: PaginationProps) {
  const getPageNumbers = () => {
    const pages = [];
    const maxVisiblePages = 5;

    if (totalPages <= maxVisiblePages) {
      for (let i = 0; i < totalPages; i++) {
        pages.push(i);
      }
    } else {
      // 현재 페이지 주변의 연속된 페이지들만 표시
      let startPage = Math.max(0, currentPage - 2);
      let endPage = Math.min(totalPages - 1, currentPage + 2);

      // 시작과 끝 페이지 조정
      if (endPage - startPage < 4) {
        if (startPage === 0) {
          endPage = Math.min(totalPages - 1, startPage + 4);
        } else {
          startPage = Math.max(0, endPage - 4);
        }
      }

      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
    }

    return pages;
  };

  if (totalPages <= 1) {
    return null;
  }

  return (
    <div className="flex items-center justify-center space-x-1 mt-6">
      {/* 이전 페이지 버튼 */}
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        className="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        이전
      </button>

      {/* 페이지 번호들 */}
      {getPageNumbers().map((page, index) => (
        <div key={index}>
          {page === -1 ? (
            <span className="px-3 py-2 text-sm text-gray-500">...</span>
          ) : (
            <button
              onClick={() => onPageChange(page)}
              className={`px-3 py-2 text-sm font-medium rounded-md ${
                currentPage === page
                  ? "bg-[#00A495] text-white"
                  : "text-gray-700 bg-white border border-gray-300 hover:bg-gray-50"
              }`}
            >
              {page + 1}
            </button>
          )}
        </div>
      ))}

      {/* 다음 페이지 버튼 */}
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages - 1}
        className="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
      >
        다음
      </button>
    </div>
  );
}
