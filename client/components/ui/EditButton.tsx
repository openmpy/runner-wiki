import { FaPlus } from "react-icons/fa";
import Button from "./Button";

interface EditButtonProps {
  onClick?: () => void;
  className?: string;
}

export default function EditButton({ onClick, className }: EditButtonProps) {
  return (
    <Button
      variant="primary"
      className={`flex items-center gap-2 ${className || ""}`}
      onClick={onClick}
    >
      <FaPlus className="md:hidden" />
      <span className="hidden md:inline">편집하기</span>
    </Button>
  );
}
