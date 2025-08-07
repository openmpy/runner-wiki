import Footer from "@/components/layout/Footer";
import Header from "@/components/layout/Header";
import Sidebar from "@/components/layout/Sidebar";
import localFont from "next/font/local";
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
            <main className="w-full lg:w-4/5 p-5 bg-white border-y border-mint lg:border lg:rounded-lg lg:border-mint">
              {children}
            </main>

            <Sidebar />
          </div>
        </div>
        <Footer />
      </body>
    </html>
  );
}
