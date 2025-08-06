"use client";

import { bmhanna } from "@/app/layout";
import TuiEditor from "@/components/toast/TuiEditor";
import Button from "@/components/ui/Button";
import AuthorInput from "@/components/wiki/AuthorInput";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";

interface DocumentData {
  documentId: string;
  documentHistoryId: string;
  title: string;
  category: string;
  author: string;
  content: string;
  createdAt: string;
  updatedAt: string;
}

interface EditorInstance {
  getInstance: () => {
    getMarkdown: () => string;
    setMarkdown: (markdown: string) => void;
  };
}

export default function EditPage() {
  const params = useParams();
  const router = useRouter();
  const slug = params.slug as string;

  const [author, setAuthor] = useState("");
  const [title, setTitle] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [uploadedImageIds, setUploadedImageIds] = useState<string[]>([]);
  const editorRef = useRef<EditorInstance>(null);

  // 기존 문서 데이터 가져오기
  useEffect(() => {
    const fetchDocument = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/v1/documents/${slug}`,
          {
            cache: "no-store",
          }
        );

        if (!response.ok) {
          throw new Error("문서를 불러올 수 없습니다.");
        }

        const document: DocumentData = await response.json();

        setTitle(document.title);
        
        // 에디터에 기존 내용 설정
        setTimeout(() => {
          if (editorRef.current?.getInstance()) {
            editorRef.current.getInstance().setMarkdown(document.content);
          }
        }, 100);
      } catch (error) {
        console.error("문서 로드 실패:", error);
        alert("문서를 불러올 수 없습니다.");
        router.push(`/wiki/${slug}`);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDocument();
  }, [slug, router]);

  const handleImageUpload = (imageIds: string[]) => {
    setUploadedImageIds(imageIds);
  };

  const handleSubmit = async () => {
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
      const documentData = {
        author: author.trim(),
        content: content.trim(),
        imageIds: uploadedImageIds,
      };

      const response = await fetch(
        `http://localhost:8080/api/v1/documents/${slug}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(documentData),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        const errorMessage =
          errorData.message || `HTTP error! status: ${response.status}`;
        throw new Error(errorMessage);
      }

      const result = await response.json();
      console.log("문서 수정 완료:", result);
      alert("문서가 성공적으로 수정되었습니다.");

      router.push(`/wiki/${slug}`);
    } catch (error) {
      const errorMessage =
        error instanceof Error
          ? error.message
          : "문서 수정에 실패했습니다. 다시 시도해주세요.";
      console.error("문서 수정 실패:", error);
      alert(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-gray-500">문서를 불러오는 중...</div>
      </div>
    );
  }

  return (
    <>
      <h1 className={`text-2xl mb-3 ${bmhanna.className}`}>문서 편집</h1>

      <div className="mb-6">
        <h2 className={`text-xl text-gray-600 ${bmhanna.className}`}>
          {title}
        </h2>
      </div>

      <AuthorInput author={author} onAuthorChange={setAuthor} />
      <TuiEditor ref={editorRef} onImageUpload={handleImageUpload} />

      {/* 버튼 영역 */}
      <div className="flex gap-4 mt-4 justify-end">
        <Button variant="outline" onClick={() => router.back()}>
          뒤로가기
        </Button>
        <Button
          variant="primary"
          onClick={handleSubmit}
          disabled={isSubmitting}
        >
          {isSubmitting ? "수정 중..." : "수정하기"}
        </Button>
      </div>
    </>
  );
}
