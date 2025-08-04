import localFont from "next/font/local";
import Header from "./components/layout/Header";
import "./globals.css";

const bmhanna = localFont({
  src: "../public/fonts/BMHANNA_11yrs_ttf.ttf",
  display: "swap",
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={`antialiased ${bmhanna.className}`}>
        <Header />
        <main>{children}</main>
      </body>
    </html>
  );
}
