"use client";

import ToastEditor, {
  ToastEditorHandle,
} from "@/components/common/ToastEditor";
import CategorySelector from "@/components/document/CategorySelector";
import DocumentFormActions from "@/components/document/DocumentFormActions";
import DocumentFormInputs from "@/components/document/DocumentFormInputs";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getLatestDocument, updateDocument } from "@/lib/api/document";
import { DocumentCategory } from "@/lib/types/document";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";

export default function DocumentHistoryEditPage() {
  const router = useRouter();
  const params = useParams();
  const editorRef = useRef<ToastEditorHandle>(null);

  const [category, setCategory] = useState<DocumentCategory>("USER");
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [content, setContent] = useState("");
  const [token, setToken] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [imageUrls, setImageUrls] = useState<string[]>([]);
  const [isEditorReady, setIsEditorReady] = useState(false);

  const slug = params.slug as string;
  const documentId = parseInt(slug);

  useEffect(() => {
    const fetchDocument = async () => {
      try {
        setIsLoading(true);
        setIsEditorReady(false);
        setImageUrls([]);

        const data = await getLatestDocument(documentId);
        setCategory(data.category);
        setTitle(data.title);
        setContent(data.content);
      } catch (error) {
        console.error(error);
        alert("문서를 불러오는데 실패했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchDocument();
  }, [documentId]);

  const handleSubmit = async () => {
    const markdown = editorRef.current?.getMarkdown();

    if (!markdown) {
      alert("에디터 로딩 중입니다. 잠시 후 다시 시도해주세요.");
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
    if (!token) {
      alert("봇 방지 인증을 완료해주세요.");
      return;
    }

    try {
      setIsSubmitting(true);

      const data = await updateDocument(documentId, {
        author,
        content: markdown,
        ...(imageUrls.length > 0 && { imageUrls }),
        token,
      });

      router.push(`/document/${data.documentId}`);
      alert("문서가 정상적으로 편집되었습니다.");
    } catch (error) {
      if (error instanceof Error) {
        alert(error.message);
      } else {
        alert("문서 편집 도중에 에러가 발생했습니다.");
      }
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div>
        <DocumentTitle text="문서편집" />
        <div className="flex justify-center items-center py-8">
          <p className="text-gray-400 dark:text-zinc-400 font-bmhanna">
            문서를 불러오는 중입니다.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div>
      <DocumentTitle text="문서편집" />
      <div className="flex flex-col gap-4">
        <CategorySelector
          category={category}
          disabled={true}
          onCategoryChange={setCategory}
        />

        <DocumentFormInputs
          title={title}
          disabledTitle={true}
          author={author}
          onTitleChange={setTitle}
          onAuthorChange={setAuthor}
        />

        <ToastEditor
          key={documentId}
          ref={editorRef}
          initialValue={content}
          onReady={() => setIsEditorReady(true)}
          onImageUploaded={(imageUrl) => {
            setImageUrls((prev) => [...prev, imageUrl]);
          }}
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
