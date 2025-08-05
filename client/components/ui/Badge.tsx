interface BadgeProps {
  category?: string;
  version?: number;
  size?: "sm" | "md" | "lg";
  className?: string;
}

export default function Badge({
  category,
  version,
  size = "md",
  className = "",
}: BadgeProps) {
  const getBadgeStyle = (category?: string, version?: number) => {
    if (version !== undefined) {
      return "bg-[#00A495] text-white border border-[#00A495]";
    }

    switch (category) {
      case "런너":
        return "bg-blue-100 text-blue-800 border border-blue-200";
      default:
        return "bg-purple-100 text-purple-800 border border-purple-200";
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

  const displayText = version !== undefined ? `V${version}` : category;

  return (
    <span
      className={`inline-flex items-center rounded font-medium ${getBadgeStyle(
        category,
        version
      )} ${getSizeStyle(size)} ${className}`}
    >
      {displayText}
    </span>
  );
}
