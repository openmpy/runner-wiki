"use client";

import "@toast-ui/editor/dist/toastui-editor-viewer.css";
import { Viewer } from "@toast-ui/react-editor";
import { useEffect, useState } from "react";

interface ToastViewerProps {
  initialValue: string;
}

const ToastViewer = ({ initialValue }: ToastViewerProps) => {
  const [isClient, setIsClient] = useState(false);

  useEffect(() => {
    setIsClient(true);
  }, []);

  return isClient ? (
    <Viewer initialValue={initialValue} />
  ) : (
    <div className="prose max-w-none">
      <div className="whitespace-pre-wrap">{initialValue}</div>
    </div>
  );
};

export default ToastViewer;
