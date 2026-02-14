import { DocumentCategory } from "@/lib/types/document";

interface DocumentCategoryBadgeProps {
  category: DocumentCategory;
}

const CATEGORY_CONFIG = {
  USER: {
    label: "런너",
    className: "bg-blue-100 text-blue-700 dark:bg-zinc-500 dark:text-zinc-200",
  },
  GUILD: {
    label: "길드",
    className: "bg-purple-100 text-purple-700 dark:bg-zinc-500 dark:text-zinc-200",
  },
} as const;

export default function DocumentCategoryBadge({
  category,
}: DocumentCategoryBadgeProps) {
  const { label, className } = CATEGORY_CONFIG[category];

  return (
    <span className={`rounded-sm font-bold px-2 py-0.5 text-sm ${className}`}>
      {label}
    </span>
  );
}
