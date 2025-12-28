import { DocumentCategory } from "@/lib/types/document";

interface CategorySelectorProps {
  category: DocumentCategory;
  disabled?: boolean;
  onCategoryChange: (category: DocumentCategory) => void;
}

export default function CategorySelector({
  category,
  disabled = false,
  onCategoryChange,
}: CategorySelectorProps) {
  return (
    <div className="flex gap-2">
      <button
        onClick={() => !disabled && onCategoryChange("USER")}
        disabled={disabled}
        className={`flex-1/2 font-bmhanna text-white rounded-sm transition-opacity text-lg py-1 ${
          category === "USER" ? "bg-mint" : "bg-gray-400"
        } ${
          disabled
            ? "opacity-50 cursor-not-allowed"
            : "hover:opacity-90 cursor-pointer"
        }`}
      >
        런너
      </button>
      <button
        onClick={() => !disabled && onCategoryChange("GUILD")}
        disabled={disabled}
        className={`flex-1/2 font-bmhanna text-white rounded-sm transition-opacity text-lg py-1 ${
          category === "GUILD" ? "bg-mint" : "bg-gray-400"
        } ${
          disabled
            ? "opacity-50 cursor-not-allowed"
            : "hover:opacity-90 cursor-pointer"
        }`}
      >
        길드
      </button>
    </div>
  );
}
