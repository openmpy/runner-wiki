"use client";

import SearchResults from "@/components/ui/SearchResults";
import { SearchResponse, SearchResult } from "@/libs/types";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaEdit, FaRandom, FaRegClock, FaSearch } from "react-icons/fa";
import { PiGraphBold } from "react-icons/pi";

const Header = () => {
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [isSearching, setIsSearching] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  const searchContainerRef = useRef<HTMLDivElement>(null);

  // 검색 API 호출
  const searchDocuments = async (query: string) => {
    if (!query.trim()) {
      setSearchResults([]);
      return;
    }

    setIsSearching(true);
    try {
      const response = await fetch(
        `${
          process.env.NEXT_PUBLIC_API_URL
        }/api/v1/search/search?uid=document&query=${encodeURIComponent(query)}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        const data: SearchResponse = await response.json();
        setSearchResults(data.hits);
        setShowResults(true);
      } else {
        console.error("검색 실패:", response.status);
        setSearchResults([]);
      }
    } catch (error) {
      console.error("검색 중 오류 발생:", error);
      setSearchResults([]);
    } finally {
      setIsSearching(false);
    }
  };

  // 검색어 입력 시 디바운스 처리
  useEffect(() => {
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current);
    }

    if (searchQuery.trim()) {
      searchTimeoutRef.current = setTimeout(() => {
        searchDocuments(searchQuery);
      }, 300);
    } else {
      setSearchResults([]);
      setShowResults(false);
    }

    return () => {
      if (searchTimeoutRef.current) {
        clearTimeout(searchTimeoutRef.current);
      }
    };
  }, [searchQuery]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        searchContainerRef.current &&
        !searchContainerRef.current.contains(event.target as Node)
      ) {
        if (showResults) {
          setShowResults(false);
        }
      }
    };

    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);
  }, [showResults]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      searchDocuments(searchQuery);
    }
  };

  const handleResultClick = () => {
    setTimeout(() => {
      setShowResults(false);
      setSearchQuery("");
    }, 100);
  };

  const handleShuffle = () => {
    console.log("셔플 버튼 클릭");
    // 셔플 로직 구현
  };

  return (
    <header className="bg-mint border-b border-mint px-4 py-3">
      <div className="max-w-7xl mx-auto">
        {/* 데스크톱 레이아웃 (원래 디자인) */}
        <div className="hidden lg:flex items-center justify-between">
          {/* 왼쪽 메뉴 */}
          <div className="flex items-center space-x-8">
            {/* 런너위키 로고 */}
            <Link
              href="/"
              className="text-2xl text-white font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
            >
              <PiGraphBold className="text-2xl" />
              <span>런너위키</span>
            </Link>

            {/* 메뉴 항목들 */}
            <nav className="flex items-center space-x-6">
              <Link
                href="/document"
                className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
              >
                <FaRegClock className="text-lg" />
                <span>최근 편집</span>
              </Link>
              <Link
                href="/document/new"
                className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
              >
                <FaEdit className="text-lg" />
                <span>문서 작성</span>
              </Link>
            </nav>
          </div>

          {/* 검색바 */}
          <div className="flex items-center space-x-3">
            {/* 셔플 버튼 */}
            <button
              type="button"
              onClick={handleShuffle}
              className="w-10 h-10 bg-white rounded-lg flex items-center justify-center border border-transparent hover:bg-gray-200 transition-colors duration-200"
            >
              <FaRandom className="text-mint text-lg" />
            </button>

            <div ref={searchContainerRef} className="relative">
              <form onSubmit={handleSearch} className="relative">
                <div className="relative flex items-center">
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder="검색할 문서 제목을 입력해주세요."
                    className="w-80 px-4 py-2 pl-4 pr-12 text-gray-800 bg-white rounded-lg border border-transparent focus:outline-none placeholder-gray-500 text-sm"
                  />
                  <button
                    type="submit"
                    className="absolute right-2 text-gray-400 p-2 hover:text-mint transition-colors duration-200"
                  >
                    {isSearching ? (
                      <div className="w-4 h-4 border-2 border-gray-300 border-t-mint rounded-full animate-spin"></div>
                    ) : (
                      <FaSearch className="text-md" />
                    )}
                  </button>
                </div>
              </form>

              <SearchResults
                results={searchResults}
                isVisible={showResults}
                onResultClick={handleResultClick}
              />
            </div>
          </div>
        </div>

        {/* 모바일 레이아웃 */}
        <div className="lg:hidden">
          {/* 첫 번째 줄: 제목과 아이콘들 */}
          <div className="flex items-center justify-between mb-3">
            {/* 런너위키 로고 */}
            <Link
              href="/"
              className="text-xl text-white font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
            >
              <PiGraphBold className="text-xl" />
              <span>런너위키</span>
            </Link>

            {/* 아이콘들 */}
            <div className="flex items-center space-x-4">
              <Link
                href="/document"
                className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
              >
                <FaRegClock className="text-lg" />
              </Link>
              <Link
                href="/document/new"
                className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
              >
                <FaEdit className="text-lg" />
              </Link>
            </div>
          </div>

          {/* 두 번째 줄: 셔플과 검색바 */}
          <div className="flex items-center space-x-3">
            {/* 셔플 버튼 */}
            <button
              type="button"
              onClick={handleShuffle}
              className="w-8 h-8 bg-white rounded-lg flex items-center justify-center border border-transparent hover:bg-gray-200 transition-colors duration-200 flex-shrink-0"
            >
              <FaRandom className="text-mint text-sm" />
            </button>

            <div ref={searchContainerRef} className="relative flex-1">
              <form onSubmit={handleSearch} className="relative">
                <div className="relative flex items-center">
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder="검색할 문서 제목을 입력해주세요."
                    className="w-full px-3 py-1.5 pl-3 pr-10 text-gray-800 bg-white rounded-lg border border-transparent focus:border-gray-400 focus:outline-none transition-all duration-200 placeholder-gray-500 text-sm"
                  />
                  <button
                    type="submit"
                    className="absolute right-1.5 text-gray-400 p-1.5 hover:text-mint transition-colors duration-200"
                  >
                    {isSearching ? (
                      <div className="w-3 h-3 border-2 border-gray-300 border-t-mint rounded-full animate-spin"></div>
                    ) : (
                      <FaSearch className="text-sm" />
                    )}
                  </button>
                </div>
              </form>

              <SearchResults
                results={searchResults}
                isVisible={showResults}
                onResultClick={handleResultClick}
              />
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
