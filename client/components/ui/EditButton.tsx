import { FaEye, FaPlus } from "react-icons/fa";
import Button from "./Button";

interface EditButtonProps {
  onClick?: () => void;
  className?: string;
  status?: "ACTIVE" | "READ_ONLY";
}

export default function EditButton({
  onClick,
  className,
  status = "ACTIVE",
}: EditButtonProps) {
  const isDisabled = status === "READ_ONLY";

  return (
    <Button
      variant="primary"
      className={`flex items-center gap-2 ${className || ""}`}
      onClick={onClick}
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
