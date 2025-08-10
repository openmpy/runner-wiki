"use client";

import { DocumentHistory } from "@/libs/types";
import { useRouter } from "next/navigation";
import { useState } from "react";
import ToastEditor from "../toast/ToastEditor";
import Button from "../ui/Button";

export default function Editor({
  documentHistory,
}: {
  documentHistory: DocumentHistory;
}) {
  const router = useRouter();
  const [author, setAuthor] = useState("");
  const [content, setContent] = useState(documentHistory.content);
  const [imageIds, setImageIds] = useState<string[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const handleImageUpload = (uploadedImageIds: string[]) => {
    setImageIds(uploadedImageIds);
  };

  const handleSubmit = async () => {
    if (!author.trim()) {
      alert("작성자명을 입력해주세요.");
      return;
    }
    if (!content.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/v1/documents/${documentHistory.documentId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            author: author.trim(),
            content: content.trim(),
            imageIds: imageIds,
          }),
        }
      );

      if (response.ok) {
        const data = await response.json();
        alert("문서가 성공적으로 편집되었습니다.");
        router.push(`/wiki/${data.documentId}`);
      } else {
        const error = await response.json();
        alert(`${error.message}`);
      }
    } catch (error) {
      console.error("문서 편집 중 오류 발생: ", error);
      alert("문서 편집 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <div className="flex flex-col gap-4">
        <div className="flex gap-2">
          <Button
            variant={
              documentHistory.category === "런너" ? "primary" : "secondary"
            }
            size="lg"
            className="flex-1"
            disabled
          >
            런너
          </Button>
          <Button
            variant={
              documentHistory.category === "길드" ? "primary" : "secondary"
            }
            size="lg"
            className="flex-1"
            disabled
          >
            길드
          </Button>
        </div>
        <div className="flex flex-col md:flex-row gap-4">
          <input
            type="text"
            value={documentHistory.title}
            className="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none bg-gray-100"
            disabled
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
          initialValue={documentHistory.content}
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
            {isSubmitting ? "편집 중.." : "편집하기"}
          </Button>
        </div>
      </div>
    </div>
  );
}
