import Header from "@/components/layout/Header";
import type { Metadata } from "next";
import { bmhanna, geist } from "./font";
import "./globals.css";

export const metadata: Metadata = {
  title: "런너위키",
  description: "누구나 쉽게 런너 문서를 작성할 수 있습니다.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={`${geist.variable} ${bmhanna.className} antialiased`}>
        <Header />
        {children}
      </body>
    </html>
  );
}
