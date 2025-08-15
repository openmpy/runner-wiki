interface ButtonProps {
  children: React.ReactNode;
  type?: "button" | "submit" | "reset";
  onClick?: () => void;
  className?: string;
  disabled?: boolean;
}

export default function Button({
  children,
  type = "button",
  onClick,
  className = "",
  disabled = false,
}: ButtonProps) {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`px-4 py-2 bg-mint text-white rounded-md hover:bg-mint/80 disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-bm-hanna ${className}`}
    >
      {children}
    </button>
  );
}
