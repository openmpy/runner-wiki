"use client";

import { bmhanna } from "@/app/layout";

interface TitleInputProps {
  title: string;
  onTitleChange: (title: string) => void;
}

export default function TitleInput({ title, onTitleChange }: TitleInputProps) {
  return (
    <div className="mb-6">
      <label
        htmlFor="title"
        className={`block text-lg mb-3 ${bmhanna.className}`}
      >
        제목
      </label>
      <input
        type="text"
        id="title"
        value={title}
        onChange={(e) => onTitleChange(e.target.value)}
        placeholder="문서 제목을 입력하세요."
        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
      />
    </div>
  );
}
