interface BadgeProps {
  category: string;
  className?: string;
}

export default function Badge({ category, className = "" }: BadgeProps) {
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

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded text-xs font-medium ${getBadgeStyle(
        category
      )} ${className}`}
    >
      {category}
    </span>
  );
}
