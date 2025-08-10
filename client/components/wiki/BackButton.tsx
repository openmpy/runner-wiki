"use client";

import { useRouter } from "next/navigation";
import { FaArrowLeft } from "react-icons/fa";
import Button from "../ui/Button";

interface BackButtonProps {
  className?: string;
}

export default function BackButton({ className }: BackButtonProps) {
  const router = useRouter();

  return (
    <Button
      variant="secondary"
      className={`flex items-center gap-2 ${className || ""}`}
      onClick={() => router.back()}
    >
      <FaArrowLeft className="md:hidden" />
      <span className="hidden md:inline">뒤로가기</span>
    </Button>
  );
}
