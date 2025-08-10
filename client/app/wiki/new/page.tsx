"use client";

import ToastEditor from "@/components/toast/ToastEditor";
import Button from "@/components/ui/Button";
import { DocumentCategory } from "@/libs/types";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function WikiNewPage() {
  const router = useRouter();
  const [selectedCategory, setSelectedCategory] =
    useState<DocumentCategory>("런너");
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [content, setContent] = useState("");
  const [imageIds, setImageIds] = useState<string[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleCategorySelect = (category: DocumentCategory) => {
    setSelectedCategory(category);
  };

  const handleImageUpload = (uploadedImageIds: string[]) => {
    setImageIds(uploadedImageIds);
  };

  const handleSubmit = async () => {
    if (!title.trim() || !author.trim() || !content.trim()) {
      alert("모든 내용을 입력해주세요.");
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await fetch("http://localhost:8080/api/v1/documents", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          title: title.trim(),
          category: selectedCategory,
          author: author.trim(),
          content: content.trim(),
          imageIds: imageIds,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        alert("문서가 성공적으로 작성되었습니다.");
        router.push(`/wiki/${data.documentId}`);
      } else {
        const error = await response.json();
        alert(`${error.message}`);
      }
    } catch (error) {
      console.error("문서 작성 중 오류 발생: ", error);
      alert("문서 작성 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsSubmitting(false);
    }
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
            variant={selectedCategory === "런너" ? "primary" : "secondary"}
            size="lg"
            className="flex-1"
            onClick={() => handleCategorySelect("런너")}
          >
            런너
          </Button>
          <Button
            variant={selectedCategory === "길드" ? "primary" : "secondary"}
            size="lg"
            className="flex-1"
            onClick={() => handleCategorySelect("길드")}
          >
            길드
          </Button>
        </div>
        <div className="flex flex-col md:flex-row gap-4 md:gap-2">
          <input
            type="text"
            placeholder="제목을 입력하세요."
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
            maxLength={10}
          />
          <input
            type="text"
            placeholder="작성자명을 입력하세요."
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            className="w-full md:w-1/3 px-3 py-2 border border-gray-300 rounded-md focus:outline-none"
            maxLength={10}
          />
        </div>
        <ToastEditor
          initialValue=" "
          onChange={setContent}
          onImageUpload={handleImageUpload}
        />
        <div className="flex gap-3 justify-end mt-4">
          <Button variant="secondary" size="lg" onClick={() => router.back()}>
            뒤로가기
          </Button>
          <Button
            variant="primary"
            size="lg"
            onClick={handleSubmit}
            disabled={isSubmitting}
          >
            {isSubmitting ? "작성 중..." : "작성하기"}
          </Button>
        </div>
      </div>
    </div>
  );
}
