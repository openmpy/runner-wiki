import { DocumentCategory } from "@/lib/types/document";

interface CategorySelectorProps {
  category: DocumentCategory;
  onCategoryChange: (category: DocumentCategory) => void;
}

export default function CategorySelector({
  category,
  onCategoryChange,
}: CategorySelectorProps) {
  return (
    <div className="flex gap-2">
      <button
        onClick={() => onCategoryChange("USER")}
        className={`flex-1/2 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity text-lg py-1 ${
          category === "USER" ? "bg-mint" : "bg-gray-400"
        }`}
      >
        런너
      </button>
      <button
        onClick={() => onCategoryChange("GUILD")}
        className={`flex-1/2 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity text-lg py-1 ${
          category === "GUILD" ? "bg-mint" : "bg-gray-400"
        }`}
      >
        길드
      </button>
    </div>
  );
}
