"use client";

import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import dynamic from "next/dynamic";

const Viewer = dynamic(
  () => import("@toast-ui/react-editor").then((m) => m.Viewer),
  { ssr: false }
);

interface ToastViewerProps {
  initialValue: string;
}

const ToastViewer = ({ initialValue }: ToastViewerProps) => {
  return <Viewer initialValue={initialValue} />;
};

ToastViewer.displayName = "ToastViewer";

export default ToastViewer;
