import React from "react";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "primary" | "secondary" | "outline";
  size?: "sm" | "md" | "lg";
  children: React.ReactNode;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    { variant = "primary", size = "md", className = "", children, ...props },
    ref
  ) => {
    const baseClasses =
      "inline-flex items-center justify-center rounded-lg font-medium transition-colors focus:outline-none";

    const variantClasses = {
      primary: "bg-[#00A495] text-white hover:bg-[#00A495]",
      secondary: "bg-gray-500 text-white hover:bg-gray-600",
      outline: "border border-gray-300 bg-white text-gray-700 hover:bg-gray-50",
    };

    const sizeClasses = {
      sm: "px-3 py-1.5 text-sm",
      md: "px-6 py-2 text-base",
      lg: "px-8 py-3 text-lg",
    };

    const classes = `${baseClasses} ${variantClasses[variant]} ${sizeClasses[size]} ${className}`;

    return (
      <button ref={ref} className={classes} {...props}>
        {children}
      </button>
    );
  }
);

Button.displayName = "Button";

export default Button;
