interface BadgeProps {
  category: string;
  size?: "sm" | "md" | "lg";
  className?: string;
}

export default function Badge({
  category,
  size = "md",
  className = "",
}: BadgeProps) {
  const getBadgeStyle = (category: string) => {
    switch (category) {
      case "런너":
        return "bg-blue-100 text-blue-800";
      case "러너":
        return "bg-blue-100 text-blue-800";
      default:
        return "bg-purple-100 text-purple-800";
    }
  };

  const getSizeStyle = (size: string) => {
    switch (size) {
      case "sm":
        return "px-1.5 py-0.5 text-xs";
      case "md":
        return "px-2.5 py-0.5 text-xs";
      case "lg":
        return "px-3 py-1 text-sm";
      default:
        return "px-2.5 py-0.5 text-xs";
    }
  };

  return (
    <span
      className={`inline-flex items-center rounded font-medium ${getBadgeStyle(
        category
      )} ${getSizeStyle(size)} ${className}`}
    >
      {category}
    </span>
  );
}
