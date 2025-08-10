"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import { useEffect, useRef } from "react";

interface ToastEditorProps {
  initialValue: string;
  onChange?: (content: string) => void;
  onImageUpload?: (imageIds: string[]) => void;
}

const ToastEditor = ({
  initialValue,
  onChange,
  onImageUpload,
}: ToastEditorProps) => {
  const editorRef = useRef<Editor>(null);
  const uploadedImageIds = useRef<string[]>([]);

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
          }
          return false;
        }
      );
    }
  }, [onImageUpload]);

  return (
    <Editor
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
  );
};

export default ToastEditor;
