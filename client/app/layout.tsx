import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import Sidebar from "@/components/layout/Sidebar";
import localFont from "next/font/local";
import { Suspense } from "react";
import "./globals.css";

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
        className={`antialiased ${bmHanna.variable} ${pretendard.variable} font-pretendard min-h-screen flex flex-col bg-gray-100`}
      >
        <Header />
        <div className="flex-1 py-5 lg:px-4">
          <div className={`flex flex-col lg:flex-row gap-5 max-w-7xl mx-auto`}>
            <main className="w-full p-5 bg-white border-y border-mint lg:border lg:rounded-lg lg:border-mint">
              {children}
            </main>

            <Suspense
              fallback={
                <aside className="w-full lg:w-100 bg-white border-y border-mint lg:border p-5 lg:rounded-lg lg:border-mint lg:align-self-start lg:self-start">
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
      </body>
    </html>
  );
}
