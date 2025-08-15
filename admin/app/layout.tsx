import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";

export const metadata: Metadata = {
  title: "관리자 - 런너위키",
  description: "런너위키 관리자 페이지입니다.",
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
        className={`antialiased ${bmHanna.variable} ${pretendard.variable} min-h-screen font-pretendard flex flex-col bg-mint`}
      >
        <Header />
        <div className="flex-1 py-5 lg:px-4 bg-gray-100">
          <div
            className={`flex flex-col lg:flex-row gap-3 max-w-7xl mx-auto items-start`}
          >
            <main className="w-full p-5 bg-white border-y border-mint lg:border lg:rounded-lg lg:border-mint flex-1">
              {children}
            </main>
          </div>
        </div>
        <Footer />
      </body>
    </html>
  );
}
