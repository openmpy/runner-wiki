"use client";

import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import dynamic from "next/dynamic";

interface ToastViewerProps {
  initialValue: string;
}

const ToastViewerImport = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Viewer),
  {
    ssr: false,
  }
);

const ToastViewer = ({ initialValue }: ToastViewerProps) => {
  return <ToastViewerImport initialValue={initialValue} />;
};

export default ToastViewer;
