"use client";

import ToastEditor from "@/components/toast/ToastEditor";
import Button from "@/components/ui/Button";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function WikiNewPage() {
  const router = useRouter();
  const [selectedCategory, setSelectedCategory] = useState<string>("runner");
  const handleCategorySelect = (category: string) => {
    setSelectedCategory(category);
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <div className="flex flex-col gap-2">
          <h1 className="font-bm-hanna text-2xl">문서 작성</h1>
          <p className="text-sm text-red-500">
            문서 작성 시 IP가 기록되며, 악의적 이용 방지를 위해서만 사용됩니다.
          </p>
        </div>
      </div>
      <div className="flex flex-col gap-4">
        <div className="flex gap-2">
          <Button
            variant={selectedCategory === "runner" ? "primary" : "secondary"}
            size="lg"
            className="flex-1"
            onClick={() => handleCategorySelect("runner")}
          >
            런너
          </Button>
          <Button
            variant={selectedCategory === "guild" ? "primary" : "secondary"}
            size="lg"
            className="flex-1"
            onClick={() => handleCategorySelect("guild")}
          >
            길드
          </Button>
        </div>
        <div className="flex flex-col md:flex-row gap-4">
          <input
            type="text"
            placeholder="제목을 입력하세요."
            className="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
          />
          <input
            type="text"
            placeholder="작성자명을 입력하세요."
            className="w-full md:w-1/3 px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
          />
        </div>
        <ToastEditor initialValue=" " />
        <div className="flex gap-3 justify-end mt-4">
          <Button variant="secondary" size="lg" onClick={() => router.back()}>
            뒤로가기
          </Button>
          <Button variant="primary" size="lg">
            작성하기
          </Button>
        </div>
      </div>
    </div>
  );
}
