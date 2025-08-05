"use client";

import { bmhanna } from "@/app/layout";
import TuiEditor from "@/components/toast/TuiEditor";
import Button from "@/components/ui/Button";
import CategorySelector from "@/components/wiki/CategorySelector";

export default function NewPage() {
  return (
    <>
      <h1 className={`text-2xl mb-6 ${bmhanna.className}`}>문서 작성</h1>
      <CategorySelector />
      <TuiEditor />

      {/* 버튼 영역 */}
      <div className="flex gap-4 mt-4 justify-end">
        <Button variant="outline" onClick={() => window.history.back()}>
          뒤로가기
        </Button>
        <Button
          variant="primary"
          onClick={() => {
            // TODO: 작성하기 로직 구현
            console.log("문서 작성 완료");
          }}
        >
          작성하기
        </Button>
      </div>
    </>
  );
}
