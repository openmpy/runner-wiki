"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { FaFile, FaHistory, FaSignOutAlt, FaUser } from "react-icons/fa";
import { PiGraphBold } from "react-icons/pi";

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/health`,
          {
            method: "GET",
            credentials: "include",
          }
        );

        if (response.ok) {
          setIsLoggedIn(true);
        } else {
          setIsLoggedIn(false);
        }
      } catch (error) {
        console.error("인증 상태 확인 중 오류 발생:", error);
        setIsLoggedIn(false);
      }
    };

    checkAuthStatus();
  }, []);

  const handleLogout = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/logout`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
      );

      if (response.ok) {
        setIsLoggedIn(false);
        window.location.href = "/login";
      } else {
        console.error("로그아웃 실패:", response.status);
        setIsLoggedIn(false);
        window.location.href = "/login";
      }
    } catch (error) {
      console.error("로그아웃 중 오류 발생:", error);
      setIsLoggedIn(false);
      window.location.href = "/login";
    }
  };

  return (
    <header className="bg-mint px-4 py-3">
      <div className="max-w-7xl mx-auto">
        <div className="hidden lg:flex items-center justify-between">
          <Link
            href="/"
            className="text-2xl text-white font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
          >
            <PiGraphBold className="text-2xl" />
            <span>런너위키</span>
          </Link>

          <nav className="flex items-center space-x-6">
            <Link
              href="/document"
              className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
            >
              <FaFile className="text-lg" />
              <span>문서</span>
            </Link>
            <Link
              href="/document/history"
              className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
            >
              <FaHistory className="text-lg" />
              <span>문서 기록</span>
            </Link>
            {isLoggedIn ? (
              <button
                onClick={handleLogout}
                className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
              >
                <FaSignOutAlt className="text-lg" />
                <span>로그아웃</span>
              </button>
            ) : (
              <Link
                href="/login"
                className="text-white text-lg font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
              >
                <FaUser className="text-lg" />
                <span>로그인</span>
              </Link>
            )}
          </nav>
        </div>

        <div className="lg:hidden">
          <div className="flex items-center justify-between">
            <Link
              href="/"
              className="text-xl text-white font-bm-hanna hover:text-gray-200 flex items-center space-x-2 transition-colors duration-200"
            >
              <PiGraphBold className="text-xl" />
              <span>런너위키</span>
            </Link>

            <div className="flex items-center space-x-4">
              <Link
                href="/document"
                className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
              >
                <FaFile className="text-lg" />
              </Link>
              <Link
                href="/document/history"
                className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
              >
                <FaHistory className="text-lg" />
              </Link>
              {isLoggedIn ? (
                <button
                  onClick={handleLogout}
                  className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
                  title="로그아웃"
                >
                  <FaSignOutAlt className="text-lg" />
                </button>
              ) : (
                <Link
                  href="/login"
                  className="text-white hover:text-gray-200 transition-colors duration-200 p-2"
                >
                  <FaUser className="text-lg" />
                </Link>
              )}
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
