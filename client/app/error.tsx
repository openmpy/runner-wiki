"use client";

import Link from "next/link";
import { useEffect } from "react";

interface ErrorProps {
  error: Error & { digest?: string };
  reset: () => void;
}

export default function Error({ error, reset }: ErrorProps) {
  useEffect(() => {
    console.error("Error:", error);
  }, [error]);

  return (
    <div className="flex flex-col items-center justify-center gap-6 py-28">
      <div className="flex flex-col items-center gap-4">
        <h2 className="text-2xl font-bmhanna text-center">
          오류가 발생했습니다
        </h2>
        <p className="text-sm text-gray-600 dark:text-zinc-500 text-center max-w-md">
          잠시 후 다시 시도해주세요.
        </p>
        {error.digest && (
          <p className="text-xs text-gray-400 dark:text-zinc-400">오류 코드: {error.digest}</p>
        )}
      </div>

      <div className="flex gap-3">
        <button
          onClick={reset}
          className="font-bmhanna text-white rounded-sm hover:opacity-90 cursor-pointer transition-opacity text-lg px-4 py-2 bg-mint dark:bg-zinc-700"
        >
          다시 시도
        </button>
        <Link
          href="/"
          className="font-bmhanna text-mint dark:text-zinc-200 rounded-sm hover:opacity-90 cursor-pointer transition-opacity text-lg px-4 py-2 border border-mint dark:border-zinc-600"
        >
          홈으로
        </Link>
      </div>
    </div>
  );
}
