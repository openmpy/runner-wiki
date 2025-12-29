"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import dynamic from "next/dynamic";
import { forwardRef, useImperativeHandle, useRef, useState } from "react";

const ToastEditorEx = dynamic(
  () => import("@toast-ui/react-editor").then((m) => m.Editor),
  { ssr: false }
);

interface ToastEditorProps {
  initialValue?: string;
}

type UploadResponse = {
  images: { imageId: number; url: string }[];
};

async function uploadImageToServer(blob: Blob, fileName?: string) {
  const formData = new FormData();
  formData.append("images", blob, fileName ?? "image.png");

  const response = await fetch("http://localhost:8080/api/v1/document-images", {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "이미지 업로드에 실패했습니다.");
  }

  const data: UploadResponse = await response.json();
  const url = data.images?.[0]?.url;

  if (!url) {
    throw new Error("업로드 응답에 url이 없습니다.");
  }
  return url;
}

const ToastEditor = forwardRef<Editor, ToastEditorProps>(
  ({ initialValue }, ref) => {
    const innerRef = useRef<Editor>(null);
    const [isUploading, setIsUploading] = useState(false);

    useImperativeHandle(ref, () => innerRef.current as Editor, []);

    return (
      <div className="relative">
        {/* 업로드 중 오버레이 */}
        {isUploading && (
          <div className="absolute inset-0 z-9999 flex items-center justify-center bg-white/60 backdrop-blur-sm">
            <div className="flex flex-col items-center gap-3 bg-white px-8 py-6">
              <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-300 border-t-gray-700" />
              <span className="text-sm text-gray-700 font-bmhanna">
                이미지 업로드 중
              </span>
            </div>
          </div>
        )}

        <ToastEditorEx
          ref={innerRef}
          initialValue={
            initialValue ??
            "문서 작성 시 IP가 기록되며, 악의적 이용 방지를 위해서만 사용됩니다."
          }
          previewStyle="vertical"
          height="600px"
          initialEditType="wysiwyg"
          useCommandShortcut={true}
          hooks={{
            addImageBlobHook: async (
              blob: Blob,
              callback: (url: string, altText?: string) => void
            ) => {
              setIsUploading(true);

              try {
                const url = await uploadImageToServer(
                  blob,
                  (blob as File).name
                );
                callback(url, (blob as File).name ?? "image");
              } catch (e) {
                alert("이미지 업로드에 실패했습니다.");
                console.error(e);
              } finally {
                setIsUploading(false);
              }
              return false;
            },
          }}
        />
      </div>
    );
  }
);

ToastEditor.displayName = "ToastEditor";

export default ToastEditor;
