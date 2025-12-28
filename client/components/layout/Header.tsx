import Link from "next/link";
import { LuBook, LuFilePen } from "react-icons/lu";

export default function Header() {
  return (
    <header className="bg-mint p-4">
      <div className="max-w-7xl mx-auto">
        {/* 데스크탑 */}
        <div className="hidden lg:flex items-center justify-start gap-6">
          <Link href="/">
            <h1 className="text-3xl font-bmhanna text-white hover:text-gray-200 transition-colors">
              런너위키
            </h1>
          </Link>
          <div className="flex items-center justify-start gap-4">
            <Link href="/document">
              <p className="text-xl font-bmhanna text-white hover:text-gray-200 transition-colors">
                최근편집
              </p>
            </Link>
            <Link href="/document/new">
              <p className="text-xl font-bmhanna text-white hover:text-gray-200 transition-colors">
                문서작성
              </p>
            </Link>
          </div>
        </div>

        {/* 모바일 */}
        <div className="flex lg:hidden items-center justify-between">
          <Link href="/">
            <h1 className="text-2xl font-bmhanna text-white hover:text-gray-200 transition-colors">
              런너위키
            </h1>
          </Link>
          <div className="flex items-center justify-between gap-8">
            <Link href="/document">
              <LuBook className="text-xl text-white font-bold hover:text-gray-200 transition-colors" />
            </Link>
            <Link href="/document/new">
              <LuFilePen className="text-xl text-white font-bold hover:text-gray-200 transition-colors" />
            </Link>
          </div>
        </div>
      </div>
    </header>
  );
}
