"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import { useEffect, useRef } from "react";

interface ToastEditorProps {
  initialValue: string;
  onChange?: (content: string) => void;
}

const ToastEditor = ({ initialValue, onChange }: ToastEditorProps) => {
  const editorRef = useRef<Editor>(null);

  useEffect(() => {
    if (editorRef.current && onChange) {
      const editor = editorRef.current.getInstance();
      editor.on("change", () => {
        const content = editor.getMarkdown();
        onChange(content);
      });
    }
  }, [onChange]);

  return (
    <Editor
      ref={editorRef}
      initialValue={initialValue}
      initialEditType="wysiwyg"
      hideModeSwitch={false}
      previewStyle="vertical"
      height="500px"
      toolbarItems={[
        ["heading", "bold", "italic", "strike"],
        ["hr", "quote"],
        ["ul", "ol", "task"],
        ["table", "image", "link"],
        ["code", "codeblock"],
        ["scrollSync"],
      ]}
    />
  );
};

export default ToastEditor;
