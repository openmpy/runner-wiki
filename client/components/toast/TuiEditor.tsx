"use client";

import "@toast-ui/editor/toastui-editor.css";
import dynamic from "next/dynamic";
import { forwardRef } from "react";

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
}

interface EditorInstance {
  getInstance: () => {
    getMarkdown: () => string;
  };
}

const TuiEditor = forwardRef<EditorInstance, TuiEditorProps>((props, ref) => {
  return (
    <div className="w-full">
      <Editor
        ref={ref}
        height="calc(100vh - 400px)"
        initialValue=" "
        initialEditType="wysiwyg"
        hideModeSwitch={true}
      />
    </div>
  );
});

TuiEditor.displayName = "TuiEditor";

export default TuiEditor;
