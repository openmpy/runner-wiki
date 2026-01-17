import Link from "next/link";

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center gap-4 py-28">
      <h2 className="text-2xl font-bmhanna">페이지가 존재하지 않습니다.</h2>
      <Link
        href="/"
        className="font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 cursor-pointer transition-opacity text-lg px-3 py-1 bg-mint dark:bg-zinc-700"
      >
        홈으로
      </Link>
    </div>
  );
}
