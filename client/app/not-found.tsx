import Button from "@/components/ui/Button";
import Link from "next/link";

export default function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center gap-4 py-28">
      <h2 className="text-2xl font-bm-hanna">페이지를 찾을 수 없습니다.</h2>
      <Link href="/" className="text-mint">
        <Button>홈으로</Button>
      </Link>
    </div>
  );
}
