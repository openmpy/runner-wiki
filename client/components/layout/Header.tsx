"use client";

import Link from "next/link";
import { BiBook, BiEdit, BiSearch, BiShuffle, BiTime } from "react-icons/bi";

export default function Header() {
  return (
    <header style={{ backgroundColor: "#00A495" }}>
      <div className="px-4 md:px-8 lg:px-16 xl:px-32">
        {/* 데스크탑 버전 */}
        <div className="hidden md:flex justify-between items-center h-16">
          {/* 로고와 네비게이션 */}
          <div className="flex items-center space-x-6">
            <Link
              href="/"
              className="flex items-center space-x-2 text-2xl font-bold text-white hover:text-blue-100 transition-colors"
            >
              <BiBook className="h-8 w-8" />
              <span>런너위키</span>
            </Link>

            {/* 네비게이션 */}
            <nav className="flex items-center space-x-4">
              <Link
                href="/wiki/recent"
                className="flex items-center space-x-2 text-white hover:text-blue-100 transition-colors font-medium text-xl"
              >
                <BiTime className="h-5 w-5" />
                <span>최근 편집</span>
              </Link>
              <Link
                href="/"
                className="flex items-center space-x-2 text-white hover:text-blue-100 transition-colors font-medium text-xl"
              >
                <BiEdit className="h-5 w-5" />
                <span>문서 작성</span>
              </Link>
            </nav>
          </div>

          {/* 검색바와 셔플 버튼 */}
          <div className="flex items-center space-x-3">
            {/* 셔플 버튼 */}
            <button
              type="button"
              className="flex items-center space-x-2 px-3 py-2 bg-white text-[#00A495] rounded-lg h-10"
              onClick={() => {
                // 셔플 기능 구현
                console.log("셔플 버튼 클릭됨");
              }}
            >
              <BiShuffle className="h-5 w-5" />
            </button>

            {/* 검색바 */}
            <div className="relative">
              <input
                type="text"
                placeholder="검색할 문서 제목을 입력해주세요."
                className="w-80 px-4 py-2 pl-4 pr-12 text-gray-900 bg-white border border-gray-300 rounded-lg focus:outline-none"
              />
              <button
                type="button"
                className="absolute right-2 top-2 p-1 text-gray-400"
                onClick={() => {
                  // 검색 기능 구현
                  console.log("검색 버튼 클릭됨");
                }}
              >
                <BiSearch className="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>

        {/* 모바일 버전 */}
        <div className="md:hidden">
          {/* 상단: 로고와 메뉴 아이콘 */}
          <div className="flex justify-between items-center h-16">
            <Link
              href="/"
              className="flex items-center space-x-2 text-xl font-bold text-white hover:text-blue-100 transition-colors"
            >
              <BiBook className="h-6 w-6" />
              <span>런너위키</span>
            </Link>

            {/* 메뉴 아이콘들 */}
            <div className="flex items-center">
              <Link
                href="/wiki/recent"
                className="text-white hover:text-blue-100 transition-colors p-2"
                title="최근 편집"
              >
                <BiTime className="h-6 w-6" />
              </Link>
              <Link
                href="/"
                className="text-white hover:text-blue-100 transition-colors p-2"
                title="문서 작성"
              >
                <BiEdit className="h-6 w-6" />
              </Link>
            </div>
          </div>

          {/* 하단: 셔플 버튼과 검색바 */}
          <div className="flex items-center space-x-3 pb-4">
            {/* 셔플 버튼 */}
            <button
              type="button"
              className="flex items-center justify-center bg-white text-[#00A495] rounded-lg h-8 w-8 flex-shrink-0"
              onClick={() => {
                // 셔플 기능 구현
                console.log("셔플 버튼 클릭됨");
              }}
            >
              <BiShuffle className="h-4 w-4" />
            </button>

            {/* 검색바 */}
            <div className="relative flex-1">
              <input
                type="text"
                placeholder="검색할 문서 제목을 입력해주세요."
                className="w-full px-3 py-1.5 pl-3 pr-10 text-gray-900 bg-white border border-gray-300 rounded-lg focus:outline-none h-8 text-sm"
              />
              <button
                type="button"
                className="absolute right-1.5 top-1/2 transform -translate-y-1/2 p-1 text-gray-400"
                onClick={() => {
                  // 검색 기능 구현
                  console.log("검색 버튼 클릭됨");
                }}
              >
                <BiSearch className="h-4 w-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
