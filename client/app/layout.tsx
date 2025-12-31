import ScrollToTop from "@/components/common/ScrollToTop";
import Sidebar from "@/components/common/Sidebar";
import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import { GoogleAnalytics } from "@next/third-parties/google";
import type { Metadata } from "next";
import { bmhanna, notoSans } from "./font";
import "./globals.css";

export const metadata: Metadata = {
  title: "런너위키",
  description: "누구나 쉽게 문서를 작업할 수 있습니다.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body
        className={`${notoSans.variable} ${bmhanna.variable} antialiased min-h-screen flex flex-col bg-mint`}
      >
        {/* 헤더 */}
        <Header />

        {/* 본문 */}
        <main className="flex-1 bg-gray-100 font-noto-sans py-4 lg:p-4">
          <div className="max-w-7xl mx-auto">
            <div className="flex flex-col lg:flex-row lg:justify-between gap-2">
              {/* 내용 */}
              <div className="border-t border-b border-mint bg-white p-4 lg:rounded-sm lg:border lg:flex-1 lg:self-start min-w-0 w-full">
                {children}
              </div>
              {/* 사이드바 */}
              <div className="border-t border-b border-mint bg-white p-4 lg:rounded-sm lg:border lg:w-80 lg:shrink-0 lg:self-start lg:sticky lg:top-4">
                <Sidebar />
              </div>
            </div>
          </div>
        </main>

        {/* 푸터 */}
        <Footer />

        {/* 스크롤 버튼*/}
        <ScrollToTop />

        {/* 기타 */}
        <GoogleAnalytics gaId="G-3PQBMJHYW1" />
      </body>
    </html>
  );
}
