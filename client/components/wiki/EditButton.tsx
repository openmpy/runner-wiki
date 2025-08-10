"use client";

import { useRouter } from "next/navigation";
import { FaEye, FaPlus } from "react-icons/fa";
import Button from "../ui/Button";

interface EditButtonProps {
  className?: string;
  status?: "ACTIVE" | "READ_ONLY";
  documentHistoryId?: string;
}

export default function EditButton({
  className,
  status = "ACTIVE",
  documentHistoryId,
}: EditButtonProps) {
  const isDisabled = status === "READ_ONLY";
  const router = useRouter();

  const handleClick = () => {
    if (documentHistoryId) {
      router.push(`/wiki/edit/${documentHistoryId}`);
    }
  };

  return (
    <Button
      variant="primary"
      className={`flex items-center gap-2 ${className || ""}`}
      onClick={handleClick}
      disabled={isDisabled}
    >
      {status === "READ_ONLY" ? (
        <FaEye className="md:hidden" />
      ) : (
        <FaPlus className="md:hidden" />
      )}

      <span className="hidden md:inline">
        {status === "READ_ONLY" ? "읽기모드" : "편집하기"}
      </span>
    </Button>
  );
}
