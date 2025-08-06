interface BadgeProps {
  content: string;
  size?: "sm" | "md" | "lg";
  color?: "red" | "orange" | "yellow" | "green" | "blue" | "indigo" | "purple";
}

export default function Badge({
  content,
  size = "md",
  color = "purple",
}: BadgeProps) {
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

  const getColorStyle = (color: string) => {
    switch (color) {
      case "red":
        return "bg-red-100 text-red-800 border border-red-200";
      case "orange":
        return "bg-orange-100 text-orange-800 border border-orange-200";
      case "yellow":
        return "bg-yellow-100 text-yellow-800 border border-yellow-200";
      case "green":
        return "bg-green-100 text-green-800 border border-green-200";
      case "blue":
        return "bg-blue-100 text-blue-800 border border-blue-200";
      case "indigo":
        return "bg-indigo-100 text-indigo-800 border border-indigo-200";
      case "purple":
        return "bg-purple-100 text-purple-800 border border-purple-200";
      default:
        return "bg-purple-100 text-purple-800 border border-purple-200";
    }
  };

  return (
    <span
      className={`inline-flex items-center rounded font-medium ${getColorStyle(
        color
      )} ${getSizeStyle(size)}`}
    >
      {content}
    </span>
  );
}
