"use client";

import ToastEditor from "@/components/common/ToastEditor";
import CategorySelector from "@/components/document/CategorySelector";
import DocumentFormActions from "@/components/document/DocumentFormActions";
import DocumentFormInputs from "@/components/document/DocumentFormInputs";
import DocumentTitle from "@/components/document/DocumentTitle";
import { createDocument } from "@/lib/api/document";
import { DocumentCategory } from "@/lib/types/document";
import { Editor } from "@toast-ui/react-editor";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";

export default function DocumentNewPage() {
  const router = useRouter();
  const editorRef = useRef<Editor>(null);

  const [category, setCategory] = useState<DocumentCategory>("USER");
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async () => {
    if (!editorRef.current) {
      return;
    }

    const editorInstance = editorRef.current.getInstance();
    const markdown = editorInstance.getMarkdown();

    if (!title.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }
    if (!author.trim()) {
      alert("작성자를 입력해주세요.");
      return;
    }
    if (!markdown.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    try {
      setIsSubmitting(true);

      const data = await createDocument({
        title,
        category,
        author,
        content: markdown,
      });

      alert("문서가 정상적으로 작성되었습니다.");
      router.push(`/document/${data.documentId}`);
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      } else {
        alert("문서 작성 도중에 에러가 발생했습니다.");
      }
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
        <div>
          <ToastEditor ref={editorRef} />
        </div>
        <DocumentFormActions
          onSubmit={handleSubmit}
          onCancel={router.back}
          isSubmitting={isSubmitting}
        />
      </div>
    </div>
  );
}
