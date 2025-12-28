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
          category === selectedCategory ? "bg-mint" : "bg-gray-400";
        const href = `/document?category=${category}`;

        return (
          <Link key={category} href={href}>
            <button
              className={`font-bmhanna ${bgColor} text-white px-4 rounded-sm hover:opacity-90 transition-opacity cursor-pointer`}
            >
              {label}
            </button>
          </Link>
        );
      })}
    </div>
  );
}
