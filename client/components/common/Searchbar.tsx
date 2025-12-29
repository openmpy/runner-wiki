"use client";

import { searchDocuments } from "@/lib/api/document";
import { DocumentPage } from "@/lib/types/document";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaSearch } from "react-icons/fa";
import DocumentCategoryBadge from "../document/DocumentCategoryBadge";

export default function Searchbar() {
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState<DocumentPage[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const searchRef = useRef<HTMLDivElement>(null);

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

  useEffect(() => {
    if (!searchQuery.trim()) {
      setSearchResults([]);
      setShowResults(false);
      return;
    }

    const debounceTimer = setTimeout(() => {
      performSearch(searchQuery);
    }, 300);

    return () => clearTimeout(debounceTimer);
  }, [searchQuery]);

  const performSearch = async (query: string) => {
    if (!query.trim()) return;

    setIsLoading(true);
    try {
      const data = await searchDocuments(query, 0, 10);
      if (data && data.payload) {
        setSearchResults(data.payload);
        setShowResults(true);
      } else {
        setSearchResults([]);
        setShowResults(true);
      }
    } catch (error) {
      console.error("검색 중 오류 발생:", error);
      setSearchResults([]);
    } finally {
      setIsLoading(false);
    }
  };

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
        className="w-full px-4 py-2 pl-4 pr-12 bg-white rounded-sm focus:outline-none text-sm"
      />
      <FaSearch className="absolute right-4 text-gray-400" />
      {showResults && (
        <div className="absolute top-full left-0 right-0 mt-1 bg-white border border-gray-200 rounded-sm shadow-lg max-h-96 overflow-y-auto z-50">
          {isLoading ? (
            <div className="p-4 text-center text-gray-400 text-sm font-bmhanna">
              검색 중입니다.
            </div>
          ) : searchResults.length > 0 ? (
            <div className="py-2">
              {searchResults.map((document) => (
                <Link
                  key={document.documentId}
                  href={`/document/${document.documentId}`}
                  onClick={handleResultClick}
                  className="block border-b border-b-gray-200 last:border-b-0"
                >
                  <div className="px-4 py-2 hover:bg-gray-100 transition-colors">
                    <div className="flex items-center justify-between">
                      <span className="text-sm">{document.title}</span>
                      <DocumentCategoryBadge category={document.category} />
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          ) : (
            <div className="p-4 text-center text-gray-400 text-sm font-bmhanna">
              검색 결과가 없습니다.
            </div>
          )}
        </div>
      )}
    </div>
  );
}
