import localFont from "next/font/local";
import Footer from "./components/layout/Footer";
import Header from "./components/layout/Header";
import Main from "./components/layout/Main";
import Sidebar from "./components/layout/Sidebar";
import "./globals.css";

const bmhanna = localFont({
  src: "../public/fonts/BMHANNA_11yrs_ttf.ttf",
  display: "swap",
});

export const pretendard = localFont({
  src: "../public/fonts/Pretendard-Regular.otf",
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
        <div className="flex flex-col flex-1">
          <Header />
          <div className="flex flex-col md:flex-row flex-1 gap-6 px-0 md:px-8 lg:px-16 xl:px-32 py-6">
            <Main>{children}</Main>
            <Sidebar />
          </div>
        </div>
        <Footer />
      </body>
    </html>
  );
}
