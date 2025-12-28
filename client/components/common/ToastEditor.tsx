"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import { forwardRef } from "react";

interface ToastEditorProps {
  initialValue?: string;
}

const ToastEditor = forwardRef<Editor, ToastEditorProps>(
  ({ initialValue }, ref) => {
    return (
      <Editor
        ref={ref}
        initialValue={
          initialValue ??
          "문서 작성 시 IP가 기록되며, 악의적 이용 방지를 위해서만 사용됩니다."
        }
        previewStyle="vertical"
        height="600px"
        initialEditType="wysiwyg"
        useCommandShortcut={true}
      />
    );
  }
);

ToastEditor.displayName = "ToastEditor";

export default ToastEditor;
