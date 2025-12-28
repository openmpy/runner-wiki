"use client";

import DocumentTitle from "@/components/document/DocumentTitle";
import { Editor } from "@toast-ui/react-editor";
import dynamic from "next/dynamic";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";

const ToastEditorEx = dynamic(() => import("@/components/common/ToastEditor"), {
  ssr: false,
});

export default function DocumentNewPage() {
  const router = useRouter();
  const editorRef = useRef<Editor>(null);

  const [category, setCategory] = useState("USER");
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [isSubmit, hasSumbit] = useState(false);

  const handleSubmit = async () => {
    if (!editorRef.current) {
      return;
    }

    const editorInstance = editorRef.current.getInstance();
    const markdown = editorInstance.getMarkdown();

    if (!title.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }
    if (!author.trim()) {
      alert("작성자를 입력해주세요.");
      return;
    }
    if (!markdown.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }

    try {
      hasSumbit(true);

      const response = await fetch(`http://localhost:8080/api/v1/documents`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          title,
          category,
          author,
          content: markdown,
        }),
      });

      if (response.ok) {
        alert("문서가 정상적으로 작성되었습니다.");
      } else {
        const error = await response.json();
        alert(`${error.message}`);
      }
    } catch (error) {
      alert("문서 작성 도중에 에러가 발생했습니다.");
      console.error(error);
    } finally {
      hasSumbit(false);
    }
  };

  return (
    <div>
      <DocumentTitle text="문서작성" />

      <div className="flex flex-col gap-4">
        <div className="flex gap-2">
          <button
            onClick={() => setCategory("USER")}
            className={`flex-1/2 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity text-lg py-1 ${
              category === "USER" ? "bg-mint" : "bg-gray-400"
            }`}
          >
            런너
          </button>
          <button
            onClick={() => setCategory("GUILD")}
            className={`flex-1/2 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity text-lg py-1 ${
              category === "GUILD" ? "bg-mint" : "bg-gray-400"
            }`}
          >
            길드
          </button>
        </div>
        <div className="flex gap-4 flex-col lg:flex-row lg:gap-2">
          <input
            type="text"
            placeholder="제목을 입력해주세요."
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="flex-2/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm"
          />
          <input
            type="text"
            placeholder="작성자를 입력해주세요."
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            className="flex-1/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm"
          />
        </div>
        <div>
          <ToastEditorEx ref={editorRef} />
        </div>
        <div className="flex justify-end gap-2">
          <button
            className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-1"
            onClick={router.back}
          >
            뒤로가기
          </button>
          <button
            onClick={handleSubmit}
            disabled={isSubmit}
            className="bg-mint font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-1"
          >
            작성하기
          </button>
        </div>
      </div>
    </div>
  );
}
