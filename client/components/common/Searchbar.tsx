"use client";

import { searchDocuments, MAX_PAGE } from "@/lib/api/document";
import { DocumentPage } from "@/lib/types/document";
import { Page } from "@/lib/types/global";
import Link from "next/link";
import { useCallback, useEffect, useRef, useState } from "react";
import { FaSearch } from "react-icons/fa";
import DocumentCategoryBadge from "../document/DocumentCategoryBadge";

export default function Searchbar() {
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState<DocumentPage[]>([]);
  const [pageInfo, setPageInfo] = useState<Page<DocumentPage> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [overMaxPage, setOverMaxPage] = useState(false);
  const searchRef = useRef<HTMLDivElement>(null);
  const resultsRef = useRef<HTMLDivElement>(null);
  const loadingPageRef = useRef<number | null>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        searchRef.current &&
        !searchRef.current.contains(event.target as Node)
      ) {
        setShowResults(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const performSearch = useCallback(
    async (query: string, page: number = 0, reset: boolean = false) => {
      if (!query.trim()) return;
      if (!reset && loadingPageRef.current === page) return;
      if (page > MAX_PAGE) {
        setOverMaxPage(true);
        return;
      }

      loadingPageRef.current = page;
      if (reset) setOverMaxPage(false);

      if (reset) {
        setIsLoading(true);
        setCurrentPage(0);
        loadingPageRef.current = 0;
      } else {
        setIsLoadingMore(true);
      }

      try {
        const data = await searchDocuments(query, page, 10);
        if (data && data.payload) {
          if (reset) {
            setSearchResults(data.payload);
            setPageInfo(data);
          } else {
            setSearchResults((prev) => {
              const existingIds = new Set(prev.map((doc) => doc.documentId));
              const newDocs = data.payload.filter(
                (doc: DocumentPage) => !existingIds.has(doc.documentId)
              );
              return [...prev, ...newDocs];
            });
            setPageInfo(data);
          }
          setShowResults(true);
          setCurrentPage(page);
        } else {
          if (reset) {
            setSearchResults([]);
            setPageInfo(null);
          }
          setShowResults(true);
        }
      } catch (error) {
        console.error("검색 중 오류 발생:", error);
        if (reset) {
          setSearchResults([]);
          setPageInfo(null);
        }
      } finally {
        setIsLoading(false);
        setIsLoadingMore(false);
        if (loadingPageRef.current === page) {
          loadingPageRef.current = null;
        }
      }
    },
    []
  );

  const loadMoreResults = useCallback(() => {
    if (!pageInfo?.hasNext || isLoadingMore || !searchQuery.trim()) return;
    if (currentPage + 1 > MAX_PAGE) {
      setOverMaxPage(true);
      return;
    }
    performSearch(searchQuery, currentPage + 1, false);
  }, [pageInfo, isLoadingMore, searchQuery, currentPage, performSearch]);

  useEffect(() => {
    if (!searchQuery.trim()) {
      setSearchResults([]);
      setPageInfo(null);
      setShowResults(false);
      setCurrentPage(0);
      setOverMaxPage(false);
      loadingPageRef.current = null;
      return;
    }

    const debounceTimer = setTimeout(() => {
      performSearch(searchQuery, 0, true);
    }, 300);

    return () => clearTimeout(debounceTimer);
  }, [searchQuery, performSearch]);

  useEffect(() => {
    const resultsContainer = resultsRef.current;
    if (!resultsContainer || !pageInfo?.hasNext || isLoadingMore) return;

    let scrollTimer: NodeJS.Timeout | null = null;

    const handleScroll = () => {
      if (scrollTimer) {
        clearTimeout(scrollTimer);
      }

      scrollTimer = setTimeout(() => {
        const { scrollTop, scrollHeight, clientHeight } = resultsContainer;
        if (scrollHeight - scrollTop - clientHeight < 100) {
          loadMoreResults();
        }
      }, 100);
    };

    resultsContainer.addEventListener("scroll", handleScroll);
    return () => {
      resultsContainer.removeEventListener("scroll", handleScroll);
      if (scrollTimer) {
        clearTimeout(scrollTimer);
      }
    };
  }, [pageInfo, isLoadingMore, loadMoreResults]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
  };

  const handleResultClick = () => {
    setShowResults(false);
    setSearchQuery("");
  };

  return (
    <div ref={searchRef} className="relative flex items-center w-full lg:w-80">
      <input
        type="text"
        placeholder="검색할 문서 제목을 입력해주세요."
        value={searchQuery}
        onChange={handleInputChange}
        onFocus={() => {
          if (searchResults.length > 0) {
            setShowResults(true);
          }
        }}
        className="w-full px-4 py-2 pl-4 pr-12 bg-white dark:bg-zinc-900 rounded-sm focus:outline-none text-sm"
      />
      <FaSearch className="absolute right-4 text-gray-400" />
      {showResults && (
        <div
          ref={resultsRef}
          className="absolute top-full left-0 right-0 mt-1 bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-600 rounded-sm shadow-lg max-h-96 overflow-y-auto z-50"
        >
          {isLoading ? (
            <div className="p-4 text-center text-gray-400 dark:text-zinc-400 text-sm font-bmhanna">
              검색 중입니다.
            </div>
          ) : searchResults.length > 0 ? (
            <div className="py-2">
              {searchResults.map((document, index) => (
                <Link
                  key={`${document.documentId}-${index}`}
                  href={`/document/${document.documentId}`}
                  onClick={handleResultClick}
                  className="block border-b border-b-gray-200 dark:border-b-zinc-600 last:border-b-0"
                >
                  <div className="px-4 py-2 hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors">
                    <div className="flex items-center justify-between">
                      <span className="text-sm">{document.title}</span>
                      <DocumentCategoryBadge category={document.category} />
                    </div>
                  </div>
                </Link>
              ))}
              {isLoadingMore && (
                <div className="p-4 text-center text-gray-400 dark:text-zinc-400 text-sm font-bmhanna">
                  불러오는 중...
                </div>
              )}
              {overMaxPage && (
                <div className="p-4 text-center text-gray-500 dark:text-zinc-400 text-sm font-bmhanna border-t border-gray-200 dark:border-zinc-600">
                  최대 10,000 페이지까지만 조회할 수 있습니다.
                </div>
              )}
            </div>
          ) : (
            <div className="p-4 text-center text-gray-400 dark:text-zinc-400 text-sm font-bmhanna">
              검색 결과가 없습니다.
            </div>
          )}
        </div>
      )}
    </div>
  );
}
