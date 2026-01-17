"use client";

import { useTheme } from "next-themes";
import { MdDarkMode, MdLightMode } from "react-icons/md";

export default function ThemeToggle() {
  const { resolvedTheme, setTheme } = useTheme();
  const isDark = resolvedTheme === "dark";

  console.log(isDark);

  return (
    <button
      type="button"
      onClick={() => setTheme(isDark ? "light" : "dark")}
      className="px-2.5 py-2.5 bg-white dark:bg-zinc-700 rounded-sm cursor-pointer hover:opacity-90 transition-opacity"
    >
      {isDark ? (
        <MdLightMode className="text-mint dark:text-zinc-200" />
      ) : (
        <MdDarkMode className="text-mint" />
      )}
    </button>
  );
}
