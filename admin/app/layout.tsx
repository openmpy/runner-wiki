import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";

export const metadata: Metadata = {
  title: "런너위키 - 관리자",
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
        className={`antialiased ${bmHanna.variable} ${pretendard.variable} font-pretendard`}
      >
        {children}
      </body>
    </html>
  );
}
