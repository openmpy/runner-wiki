"use client";

import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import GithubSlugger from "github-slugger";
import dynamic from "next/dynamic";
import { useEffect, useRef } from "react";

const Viewer = dynamic(
  () => import("@toast-ui/react-editor").then((m) => m.Viewer),
  { ssr: false }
);

interface ToastViewerProps {
  initialValue: string;
}

const ToastViewer = ({ initialValue }: ToastViewerProps) => {
  const wrapperRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const root = wrapperRef.current;
    if (!root) return;

    const applyHeadingIds = () => {
      const slugger = new GithubSlugger();
      const headings = root.querySelectorAll("h1, h2, h3, h4, h5, h6");

      headings.forEach((h) => {
        const text = (h.textContent ?? "").trim();
        if (!text) return;

        const id = slugger.slug(text);
        h.id = id;
      });
    };

    // 1) 즉시 1번
    applyHeadingIds();
    // 2) 렌더 타이밍 이슈 대비 다음 프레임에 1번 더
    requestAnimationFrame(applyHeadingIds);
  }, [initialValue]);

  return (
    <div ref={wrapperRef}>
      <Viewer initialValue={initialValue} />
    </div>
  );
};

ToastViewer.displayName = "ToastViewer";
export default ToastViewer;
