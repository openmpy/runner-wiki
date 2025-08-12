"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import dynamic from "next/dynamic";
import { useEffect, useRef, useState } from "react";

interface ToastEditorProps {
  initialValue: string;
  onChange?: (content: string) => void;
  onImageUpload?: (imageIds: string[]) => void;
}

const ToastEditorImport = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Editor),
  {
    ssr: false,
  }
);

const ToastEditor = ({
  initialValue,
  onChange,
  onImageUpload,
}: ToastEditorProps) => {
  const editorRef = useRef<Editor>(null);
  const uploadedImageIds = useRef<string[]>([]);
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    if (editorRef.current && onChange) {
      const editor = editorRef.current.getInstance();
      editor.on("change", () => {
        const content = editor.getMarkdown();
        onChange(content);
      });
    }
  }, [onChange]);

  useEffect(() => {
    if (editorRef.current) {
      const editor = editorRef.current.getInstance();

      editor.addHook(
        "addImageBlobHook",
        async (blob: File, callback: (url: string) => void) => {
          try {
            setIsUploading(true);
            const formData = new FormData();
            formData.append("file", blob);

            const response = await fetch(
              `${process.env.NEXT_PUBLIC_API_URL}/api/v1/images`,
              {
                method: "POST",
                body: formData,
              }
            );

            if (response.ok) {
              const data = await response.json();
              const { imageId, key } = data;

              uploadedImageIds.current.push(imageId);

              if (onImageUpload) {
                onImageUpload([...uploadedImageIds.current]);
              }

              const imageUrl = `${process.env.NEXT_PUBLIC_IMAGE_URL}/${key}`;
              callback(imageUrl);
            } else {
              console.error("이미지 업로드 실패");
              callback("");
            }
          } catch (error) {
            console.error("이미지 업로드 중 오류 발생:", error);
            callback("");
          } finally {
            setIsUploading(false);
          }
          return false;
        }
      );
    }
  }, [onImageUpload]);

  return (
    <>
      {isUploading && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 flex flex-col items-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-mint mb-4"></div>
            <p className="font-bm-hanna">이미지 업로드 중...</p>
          </div>
        </div>
      )}
      <ToastEditorImport
        ref={editorRef}
        initialValue={initialValue}
        initialEditType="wysiwyg"
        hideModeSwitch={false}
        previewStyle="vertical"
        height="500px"
        toolbarItems={[
          ["heading", "bold", "italic", "strike"],
          ["hr", "quote"],
          ["ul", "ol", "task", "indent", "outdent"],
          ["table", "image", "link"],
          ["code", "codeblock"],
          ["scrollSync"],
        ]}
      />
    </>
  );
};

export default ToastEditor;
