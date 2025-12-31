"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import dynamic from "next/dynamic";
import {
  forwardRef,
  useEffect,
  useImperativeHandle,
  useMemo,
  useRef,
  useState,
} from "react";

const ToastEditorEx = dynamic(
  () => import("@toast-ui/react-editor").then((m) => m.Editor),
  { ssr: false }
);

export interface ToastEditorHandle {
  getMarkdown: () => string | null;
  isReady: () => boolean;
}

interface ToastEditorProps {
  initialValue?: string;
  onReady?: () => void;
  onImageUploaded?: (imageId: number) => void;
}

type UploadResponse = {
  images: { imageId: number; url: string }[];
};

const TOOLBAR_DESKTOP = [
  ["heading", "bold", "italic", "strike"],
  ["hr", "quote"],
  ["ul", "ol", "task"],
  ["table", "link", "image"],
  ["code", "codeblock"],
];

const TOOLBAR_MOBILE = [
  ["bold", "italic", "strike"],
  ["ul", "ol"],
  ["link", "image"],
];

const ToastEditor = forwardRef<ToastEditorHandle, ToastEditorProps>(
  ({ initialValue, onReady, onImageUploaded }, ref) => {
    const innerRef = useRef<Editor>(null);
    const readyFiredRef = useRef(false);

    const [isUploading, setIsUploading] = useState(false);
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
      const check = () => setIsMobile(window.innerWidth <= 768);
      check();
      window.addEventListener("resize", check);
      return () => window.removeEventListener("resize", check);
    }, []);

    const toolbarItems = useMemo(
      () => (isMobile ? TOOLBAR_MOBILE : TOOLBAR_DESKTOP),
      [isMobile]
    );

    useImperativeHandle(
      ref,
      () => ({
        isReady: () => !!innerRef.current?.getInstance?.(),
        getMarkdown: () => {
          const inst = innerRef.current?.getInstance?.();
          return inst ? inst.getMarkdown() : null;
        },
      }),
      []
    );

    useEffect(() => {
      let cancelled = false;

      const tick = () => {
        if (cancelled) return;

        const inst = innerRef.current?.getInstance?.();
        if (inst && !readyFiredRef.current) {
          readyFiredRef.current = true;
          onReady?.();
          return;
        }

        setTimeout(tick, 50);
      };

      tick();
      return () => {
        cancelled = true;
      };
    }, [onReady]);

    return (
      <div className="relative">
        {isUploading && (
          <div className="absolute inset-0 z-50 flex items-center justify-center bg-white/60 backdrop-blur-sm">
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
          toolbarItems={toolbarItems}
          hooks={{
            addImageBlobHook: async (
              blob: Blob,
              callback: (url: string, altText?: string) => void
            ) => {
              setIsUploading(true);

              try {
                const formData = new FormData();
                formData.append(
                  "images",
                  blob,
                  (blob as File).name ?? "image.png"
                );

                const response = await fetch(
                  `${process.env.NEXT_PUBLIC_API_BASE_URL}/v1/document-images`,
                  { method: "POST", body: formData }
                );

                if (!response.ok) {
                  const error = await response.json();
                  throw new Error(
                    error.message || "이미지 업로드에 실패했습니다."
                  );
                }

                const data: UploadResponse = await response.json();
                const imageData = data.images?.[0];

                if (!imageData?.url)
                  throw new Error("업로드 응답에 url이 없습니다.");

                callback(imageData.url, (blob as File).name ?? "image");

                if (imageData.imageId && onImageUploaded) {
                  onImageUploaded(imageData.imageId);
                }
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
