"use client";

import { bmhanna } from "@/app/layout";

interface AuthorInputProps {
  author: string;
  onAuthorChange: (author: string) => void;
}

export default function AuthorInput({
  author,
  onAuthorChange,
}: AuthorInputProps) {
  return (
    <div className="mb-6">
      <label
        htmlFor="author"
        className={`block text-lg mb-3 ${bmhanna.className}`}
      >
        작성자명
      </label>
      <input
        type="text"
        id="author"
        value={author}
        onChange={(e) => onAuthorChange(e.target.value)}
        placeholder="작성자명을 입력하세요."
        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
      />
    </div>
  );
}
