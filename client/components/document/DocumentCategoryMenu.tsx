import { DocumentCategory } from "@/lib/types/document";
import Link from "next/link";

interface DocumentCategoryMenuProps {
  selectedCategory: string;
}

const CATEGORY_MENU_CONFIG = {
  ALL: {
    label: "전체",
  },
  USER: {
    label: "런너",
  },
  GUILD: {
    label: "길드",
  },
} as const;

const CATEGORIES: ("ALL" | DocumentCategory)[] = ["ALL", "USER", "GUILD"];

export default function DocumentCategoryMenu({
  selectedCategory,
}: DocumentCategoryMenuProps) {
  return (
    <div className="flex gap-2">
      {CATEGORIES.map((category) => {
        const { label } = CATEGORY_MENU_CONFIG[category];
        const bgColor =
          category === selectedCategory
            ? "bg-mint dark:bg-zinc-700"
            : "bg-gray-400 dark:bg-zinc-500";
        const href = `/document?category=${category}`;

        return (
          <Link key={category} href={href}>
            <button
              className={`font-bmhanna ${bgColor} text-white dark:text-zinc-200 px-5 py-0.5 rounded-sm hover:opacity-90 transition-opacity cursor-pointer text-base`}
            >
              {label}
            </button>
          </Link>
        );
      })}
    </div>
  );
}
