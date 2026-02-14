import ScrollToTop from "@/components/common/ScrollToTop";
import Sidebar from "@/components/common/Sidebar";
import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import { GoogleAnalytics } from "@next/third-parties/google";
import type { Metadata } from "next";
import Script from "next/script";
import { bmhanna, notoSans } from "./font";
import "./globals.css";
import Providers from "./providers";

export const metadata: Metadata = {
  title: "런너위키 | 테일즈런너",
  description:
    "테일즈런너를 플레이하는 유저라면 누구나 문서를 작성하고 열람할 수 있습니다.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" suppressHydrationWarning>
      <head>
        <Script
          src="https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit"
          strategy="beforeInteractive"
          defer
        />
      </head>
      <body
        className={`${notoSans.variable} ${bmhanna.variable} antialiased min-h-screen flex flex-col bg-mint dark:bg-zinc-800 text-zinc-900 dark:text-zinc-200`}
      >
        <Providers>
          {/* 헤더 */}
          <Header />

          {/* 본문 */}
          <main className="flex-1 bg-gray-100 dark:bg-black font-noto-sans py-4 lg:p-4">
            <div className="max-w-7xl mx-auto">
              <div className="flex flex-col lg:flex-row lg:justify-between gap-2">
                {/* 내용 */}
                <div className="border-t border-b border-mint dark:border-zinc-600 bg-white dark:bg-zinc-900 p-4 lg:rounded-sm lg:border lg:flex-1 lg:self-start min-w-0 w-full">
                  {children}
                </div>
                {/* 사이드바 */}
                <div className="border-t border-b border-mint dark:border-zinc-600 bg-white dark:bg-zinc-900 p-4 lg:rounded-sm lg:border lg:w-80 lg:shrink-0 lg:self-start lg:sticky lg:top-4">
                  <Sidebar />
                </div>
              </div>
            </div>
          </main>

          {/* 푸터 */}
          <Footer />

          {/* 스크롤 버튼*/}
          <ScrollToTop />
        </Providers>

        {/* 기타 */}
        <GoogleAnalytics gaId="G-3PQBMJHYW1" />
      </body>
    </html>
  );
}
