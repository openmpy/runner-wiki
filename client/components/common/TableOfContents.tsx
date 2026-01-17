import type { TocItem } from "@/lib/toc";
import Link from "next/link";

export default function TableOfContents({ items }: { items: TocItem[] }) {
  if (!items.length) return null;

  const counters: number[] = [];
  const getNumber = (depth: number) => {
    counters.length = depth;
    counters[depth - 1] = (counters[depth - 1] ?? 0) + 1;

    return counters.join("-");
  };

  return (
    <nav className="inline-block w-fit border border-gray-200 dark:border-zinc-600 rounded-sm p-3 mb-4">
      <div className="font-bmhanna mb-2">목차</div>

      <ul className="text-sm flex flex-col gap-1">
        {items.map((it) => {
          const number = getNumber(it.depth);

          return (
            <li
              key={it.id}
              className={[
                it.depth === 2 ? "pl-3" : "",
                it.depth === 3 ? "pl-6" : "",
                it.depth === 4 ? "pl-9" : "",
                it.depth >= 5 ? "pl-12" : "",
              ].join(" ")}
            >
              <Link href={`#${it.id}`} className="flex gap-1">
                <span className="text-mint dark:text-orange-400 tabular-nums whitespace-nowrap">
                  {number}.
                </span>
                <span className="hover:underline">{it.text}</span>
              </Link>
            </li>
          );
        })}
      </ul>
    </nav>
  );
}
