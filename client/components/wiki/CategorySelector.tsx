"use client";

import { bmhanna } from "@/app/layout";
import { useState } from "react";

interface CategorySelectorProps {
  onCategoryChange?: (category: string) => void;
  initialCategory?: string;
}

export default function CategorySelector({
  onCategoryChange,
  initialCategory = "runner",
}: CategorySelectorProps) {
  const [selectedCategory, setSelectedCategory] =
    useState<string>(initialCategory);

  const handleCategorySelect = (category: string) => {
    setSelectedCategory(category);
    onCategoryChange?.(category);
  };

  return (
    <div className="mb-6">
      <h2 className={`text-lg mb-3 ${bmhanna.className}`}>카테고리</h2>
      <div className="flex gap-2">
        <button
          onClick={() => handleCategorySelect("runner")}
          className={`px-4 py-1 rounded-lg border transition-colors ${
            selectedCategory === "runner"
              ? "bg-[#00A495] text-white border-[#00A495]"
              : "bg-white text-gray-700 border-gray-300 hover:bg-gray-50"
          }`}
        >
          런너
        </button>

        <button
          onClick={() => handleCategorySelect("guild")}
          className={`px-4 py-1 rounded-lg border transition-colors ${
            selectedCategory === "guild"
              ? "bg-[#00A495] text-white border-[#00A495]"
              : "bg-white text-gray-700 border-gray-300 hover:bg-gray-50"
          }`}
        >
          길드
        </button>
      </div>
    </div>
  );
}
