import Link from "next/link";

export default function Footer() {
  return (
    <footer className="bg-mint dark:bg-zinc-800 p-4">
      <div className="max-w-7xl mx-auto">
        <p className="text-center text-white dark:text-zinc-200 text-xs lg:text-sm">
          &copy; 2026{" "}
          <Link
            href="https://open.kakao.com/o/gtQPMY9h"
            target="_blank"
            rel="noopener noreferrer"
            className="font-semibold hover:underline"
          >
            런너위키.
          </Link>{" "}
          All rights reserved.
        </p>
      </div>
    </footer>
  );
}
