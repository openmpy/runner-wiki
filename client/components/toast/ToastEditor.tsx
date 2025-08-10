"use client";

import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";

interface ToastEditorProps {
  initialValue: string;
}

const ToastEditor = ({ initialValue }: ToastEditorProps) => {
  return (
    <Editor
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
      placeholder="내용을 입력하세요."
    />
  );
};

export default ToastEditor;
