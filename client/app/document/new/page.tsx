"use client";

import ToastEditor, {
  ToastEditorHandle,
} from "@/components/common/ToastEditor";
import CategorySelector from "@/components/document/CategorySelector";
import DocumentFormActions from "@/components/document/DocumentFormActions";
import DocumentFormInputs from "@/components/document/DocumentFormInputs";
import DocumentTitle from "@/components/document/DocumentTitle";
import { createDocument } from "@/lib/api/document";
import { DocumentCategory } from "@/lib/types/document";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";

export default function DocumentNewPage() {
  const router = useRouter();
  const editorRef = useRef<ToastEditorHandle>(null);

  const [category, setCategory] = useState<DocumentCategory>("USER");
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [token, setToken] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [imageUrls, setImageUrls] = useState<string[]>([]);
  const [isEditorReady, setIsEditorReady] = useState(false);

  const handleSubmit = async () => {
    const markdown = editorRef.current?.getMarkdown();

    if (!markdown) {
      alert("에디터 로딩 중입니다. 잠시 후 다시 시도해주세요.");
      return;
    }

    if (!title.trim()) return alert("제목을 입력해주세요.");
    if (!author.trim()) return alert("작성자를 입력해주세요.");
    if (!markdown.trim()) return alert("내용을 입력해주세요.");

    if (!token) {
      alert("봇 방지 인증을 완료해주세요.");
      return;
    }

    try {
      setIsSubmitting(true);

      const data = await createDocument({
        title,
        category,
        author,
        content: markdown,
        ...(imageUrls.length > 0 && { imageUrls }),
        token,
      });

      router.push(`/document/${data.documentId}`);
      alert("문서가 정상적으로 작성되었습니다.");
    } catch (error) {
      alert(
        error instanceof Error
          ? error.message
          : "문서 작성 도중에 에러가 발생했습니다."
      );
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <DocumentTitle text="문서작성" />
      <div className="flex flex-col gap-4">
        <CategorySelector category={category} onCategoryChange={setCategory} />
        <DocumentFormInputs
          title={title}
          author={author}
          onTitleChange={setTitle}
          onAuthorChange={setAuthor}
        />

        <ToastEditor
          ref={editorRef}
          onReady={() => setIsEditorReady(true)}
          onImageUploaded={(imageUrl) =>
            setImageUrls((prev) => [...prev, imageUrl])
          }
        />

        <DocumentFormActions
          onSubmit={handleSubmit}
          onCancel={router.back}
          isSubmitting={isSubmitting}
          disabled={!isEditorReady}
          onTokenChange={setToken}
        />
      </div>
    </div>
  );
}
