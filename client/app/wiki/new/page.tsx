"use client";

import { bmhanna } from "@/app/layout";
import TuiEditor from "@/components/toast/TuiEditor";
import Button from "@/components/ui/Button";
import AuthorInput from "@/components/wiki/AuthorInput";
import CategorySelector from "@/components/wiki/CategorySelector";
import TitleInput from "@/components/wiki/TitleInput";
import { useRef, useState } from "react";

interface DocumentData {
  title: string;
  category: string;
  author: string;
  content: string;
}

interface EditorInstance {
  getInstance: () => {
    getMarkdown: () => string;
  };
}

export default function NewPage() {
  const [author, setAuthor] = useState("");
  const [category, setCategory] = useState("runner");
  const [title, setTitle] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const editorRef = useRef<EditorInstance>(null);

  const handleSubmit = async () => {
    if (!title.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }
    if (!author.trim()) {
      alert("작성자를 입력해주세요.");
      return;
    }

    const content = editorRef.current?.getInstance()?.getMarkdown() || "";
    if (!content.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    setIsSubmitting(true);

    try {
      const documentData: DocumentData = {
        title: title.trim(),
        category,
        author: author.trim(),
        content: content.trim(),
      };

      const response = await fetch("http://localhost:8080/api/v1/documents", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(documentData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        const errorMessage =
          errorData.message || `HTTP error! status: ${response.status}`;
        throw new Error(errorMessage);
      }

      const result = await response.json();
      console.log("문서 작성 완료:", result);
      alert("문서가 성공적으로 작성되었습니다!");

      window.location.href = "/wiki/recent";
    } catch (error) {
      const errorMessage =
        error instanceof Error
          ? error.message
          : "문서 작성에 실패했습니다. 다시 시도해주세요.";
      console.error("문서 작성 실패:", error);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <>
      <h1 className={`text-2xl mb-6 ${bmhanna.className}`}>문서 작성</h1>

      <CategorySelector
        onCategoryChange={setCategory}
        initialCategory={category}
      />
      <TitleInput title={title} onTitleChange={setTitle} />
      <AuthorInput author={author} onAuthorChange={setAuthor} />
      <TuiEditor ref={editorRef} />

      {/* 버튼 영역 */}
      <div className="flex gap-4 mt-4 justify-end">
        <Button variant="outline" onClick={() => window.history.back()}>
          뒤로가기
        </Button>
        <Button
          variant="primary"
          onClick={handleSubmit}
          disabled={isSubmitting}
        >
          {isSubmitting ? "작성 중..." : "작성하기"}
        </Button>
      </div>
    </>
  );
}
