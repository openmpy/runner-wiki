import localFont from "next/font/local";
import Footer from "./components/layout/Footer";
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
      <body
        className={`antialiased ${bmhanna.className} min-h-screen flex flex-col`}
      >
        <Header />
        <main className="flex-1">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
