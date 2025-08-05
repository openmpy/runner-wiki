import { bmhanna } from "@/app/layout";
import Tiptap from "@/components/tiptap/Tiptap";

export default async function NewPage() {
  return (
    <>
      <h1 className={`text-2xl mb-6 ${bmhanna.className}`}>문서 작성</h1>
      <Tiptap />
    </>
  );
}
