import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import Sidebar from "@/components/layout/Sidebar";
import ScrollToTop from "@/components/ui/ScrollToTop";
import { Metadata } from "next";
import localFont from "next/font/local";
import { Suspense } from "react";
import "./globals.css";

export const metadata: Metadata = {
  title: {
    template: "%s - 런너위키",
    default: "런너위키",
  },
  description: "누구나 자유롭게 기여할 수 있는 위키입니다.",
};

const bmHanna = localFont({
  src: "../public/fonts/BMHANNA_11yrs_otf.otf",
  variable: "--font-bm-hanna",
  display: "swap",
});

const pretendard = localFont({
  src: "../public/fonts/Pretendard-Regular.otf",
  variable: "--font-pretendard",
  display: "swap",
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body
        className={`antialiased ${bmHanna.variable} ${pretendard.variable} font-pretendard min-h-screen flex flex-col bg-mint`}
      >
        <Header />
        <div className="flex-1 py-5 lg:px-4 bg-gray-100">
          <div
            className={`flex flex-col lg:flex-row gap-3 max-w-7xl mx-auto items-start`}
          >
            <main className="w-full p-5 bg-white border-y border-mint lg:border lg:rounded-lg lg:border-mint flex-1">
              {children}
            </main>

            <Suspense
              fallback={
                <aside className="w-full lg:w-80 bg-white border-y border-mint lg:border p-5 lg:rounded-lg lg:border-mint lg:align-self-start lg:self-start lg:sticky lg:top-5">
                  <div>
                    <h1 className="font-bm-hanna text-2xl mb-4">인기 문서</h1>
                    <div className="flex flex-col gap-4">
                      <div className="animate-pulse">
                        <div className="h-4 bg-gray-200 rounded mb-2"></div>
                        <div className="h-4 bg-gray-200 rounded mb-2"></div>
                        <div className="h-4 bg-gray-200 rounded"></div>
                      </div>
                    </div>
                  </div>
                </aside>
              }
            >
              <Sidebar />
            </Suspense>
          </div>
        </div>
        <Footer />
        <ScrollToTop />
      </body>
    </html>
  );
}
