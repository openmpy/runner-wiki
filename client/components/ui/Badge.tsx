import { ReactNode } from "react";

type BadgeSize = "sm" | "md" | "lg";
type BadgeColor =
  | "red"
  | "orange"
  | "yellow"
  | "green"
  | "blue"
  | "indigo"
  | "purple";

interface BadgeProps {
  children: ReactNode;
  size?: BadgeSize;
  color?: BadgeColor;
  className?: string;
}

const sizeClasses = {
  sm: "px-2 py-0.5 text-xs",
  md: "px-2.5 py-1 text-sm",
  lg: "px-3 py-1.5 text-base",
};

const colorClasses = {
  red: "bg-red-100 text-red-800",
  orange: "bg-orange-100 text-orange-800",
  yellow: "bg-yellow-100 text-yellow-800",
  green: "bg-green-100 text-green-800",
  blue: "bg-blue-100 text-blue-800",
  indigo: "bg-indigo-100 text-indigo-800",
  purple: "bg-purple-100 text-purple-800",
};

export default function Badge({
  children,
  size = "md",
  color = "blue",
  className = "",
}: BadgeProps) {
  return (
    <span
      className={`
        inline-flex items-center font-semibold rounded-lg
        ${sizeClasses[size]}
        ${colorClasses[color]}
        ${className}
      `}
    >
      {children}
    </span>
  );
}
