"use client";

import "@toast-ui/editor/toastui-editor.css";
import dynamic from "next/dynamic";

const Editor = dynamic(
  () => import("@toast-ui/react-editor").then((mod) => mod.Editor),
  {
    ssr: false,
    loading: () => (
      <div className="text-center text-gray-500">에디터 로딩 중...</div>
    ),
  }
);

export default function TuiEditor() {
  return (
    <div className="w-full">
      <Editor
        height="calc(100vh - 400px)"
        initialValue=" "
        initialEditType="wysiwyg"
        hideModeSwitch={true}
      />
    </div>
  );
}
