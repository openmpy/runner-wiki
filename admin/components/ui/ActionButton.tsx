interface ActionButtonProps {
  children: React.ReactNode;
  variant?: "active" | "read" | "delete" | "custom";
  customColor?: {
    bg: string;
    text: string;
    border: string;
    hover: string;
  };
  onClick?: () => void;
  className?: string;
}

export default function ActionButton({
  children,
  variant = "custom",
  customColor,
  onClick,
  className = "",
}: ActionButtonProps) {
  const getVariantStyles = () => {
    switch (variant) {
      case "active":
        return "border-green-200 bg-green-100 text-green-800 hover:bg-green-200";
      case "read":
        return "border-orange-200 bg-orange-100 text-orange-800 hover:bg-orange-200";
      case "delete":
        return "border-red-200 bg-red-100 text-red-800 hover:bg-red-200";
      case "custom":
        return customColor
          ? `${customColor.border} ${customColor.bg} ${customColor.text} hover:${customColor.hover}`
          : "border-gray-200 bg-gray-100 text-gray-800 hover:bg-gray-200";
      default:
        return "border-gray-200 bg-gray-100 text-gray-800 hover:bg-gray-200";
    }
  };

  return (
    <button
      onClick={onClick}
      className={`
        px-2 py-0.5 text-xs font-semibold rounded border transition-colors
        ${getVariantStyles()}
        ${className}
      `}
    >
      {children}
    </button>
  );
}
