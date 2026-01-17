import Link from "next/link";
import { LuBook, LuFilePen } from "react-icons/lu";
import Searchbar from "../common/Searchbar";
import ShuffleButton from "../common/ShuffleButton";
import ThemeToggleClient from "../common/ThemeToggleClient";

export default function Header() {
  return (
    <header className="bg-mint dark:bg-zinc-800 p-4">
      <div className="max-w-7xl mx-auto">
        {/* 데스크탑 */}
        <div className="hidden lg:flex items-center justify-between">
          <div className="lg:flex items-center justify-start gap-6">
            <Link href="/">
              <h1 className="text-3xl font-bmhanna text-white dark:text-zinc-200 hover:text-gray-200 transition-colors">
                런너위키
              </h1>
            </Link>
            <div className="flex items-center justify-start gap-4">
              <Link href="/document">
                <p className="text-xl font-bmhanna text-white dark:text-zinc-200 hover:text-gray-200 transition-colors">
                  최근편집
                </p>
              </Link>
              <Link href="/document/new">
                <p className="text-xl font-bmhanna text-white dark:text-zinc-200 hover:text-gray-200 transition-colors">
                  문서작성
                </p>
              </Link>
            </div>
          </div>
          <div className="flex gap-2">
            <ThemeToggleClient />
            <ShuffleButton />
            <Searchbar />
          </div>
        </div>

        {/* 모바일 */}
        <div className="flex flex-col gap-3 lg:hidden">
          <div className="flex items-center justify-between">
            <Link href="/">
              <h1 className="text-2xl font-bmhanna text-white dark:text-zinc-200 hover:text-gray-200 transition-colors">
                런너위키
              </h1>
            </Link>
            <div className="flex items-center justify-between gap-8">
              <ThemeToggleClient />
              <Link href="/document">
                <LuBook className="text-xl text-white font-bold dark:text-zinc-200 hover:text-gray-200 transition-colors" />
              </Link>
              <Link href="/document/new">
                <LuFilePen className="text-xl text-white font-bold dark:text-zinc-200 hover:text-gray-200 transition-colors" />
              </Link>
            </div>
          </div>
          <div className="flex gap-2">
            <ShuffleButton />
            <Searchbar />
          </div>
        </div>
      </div>
    </header>
  );
}
