"use client";

import "@toast-ui/editor/toastui-editor.css";
import dynamic from "next/dynamic";
import { forwardRef, useImperativeHandle, useRef } from "react";

const Editor = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Editor),
  {
    ssr: false,
    loading: () => (
      <div className="text-center text-gray-500">에디터 로딩 중...</div>
    ),
  }
);

interface TuiEditorProps {
  initialValue?: string;
  onImageUpload?: (imageIds: string[]) => void;
}

interface EditorInstance {
  getInstance: () => {
    getMarkdown: () => string;
    setMarkdown: (markdown: string) => void;
  };
}

const TuiEditor = forwardRef<EditorInstance, TuiEditorProps>((props, ref) => {
  const { onImageUpload } = props;
  const uploadedImageIds: string[] = [];
  const editorRef = useRef<{
    getInstance: () => {
      getMarkdown: () => string;
      setMarkdown: (markdown: string) => void;
    };
  }>(null);

  const handleImageUpload = async (
    file: File,
    callback: (url: string) => void
  ) => {
    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await fetch("http://localhost:8080/api/v1/images", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error("이미지 업로드에 실패했습니다.");
      }

      const result = await response.json();
      const imageId = result.imageId;
      const key = result.key;

      // 업로드된 이미지 ID 저장
      uploadedImageIds.push(imageId);

      // 부모 컴포넌트에 이미지 ID 목록 전달
      if (onImageUpload) {
        onImageUpload([...uploadedImageIds]);
      }

      // 에디터에 이미지 URL 반환 (임시 URL 또는 실제 이미지 URL)
      const imageUrl = `https://runner-wiki.s3.ap-northeast-2.amazonaws.com/images/${key}`;
      console.log("imageUrl", imageUrl);
      callback(imageUrl);
    } catch (error) {
      console.error("이미지 업로드 오류:", error);
      alert("이미지 업로드에 실패했습니다.");
      callback("");
    }
  };

  useImperativeHandle(ref, () => ({
    getInstance: () => ({
      getMarkdown: () => {
        if (editorRef.current) {
          return editorRef.current.getInstance().getMarkdown();
        }
        return "";
      },
      setMarkdown: (markdown: string) => {
        if (editorRef.current) {
          editorRef.current.getInstance().setMarkdown(markdown);
        }
      },
    }),
  }));

  return (
    <div className="w-full">
      <Editor
        ref={editorRef}
        height="calc(100vh - 400px)"
        initialValue=" "
        initialEditType="wysiwyg"
        previewStyle="vertical"
        hooks={{
          addImageBlobHook: handleImageUpload,
        }}
      />
    </div>
  );
});

TuiEditor.displayName = "TuiEditor";

export default TuiEditor;
