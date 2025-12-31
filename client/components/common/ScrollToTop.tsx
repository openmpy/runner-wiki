"use client";

import { FaArrowUp } from "react-icons/fa";

export default function ScrollToTop() {
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  return (
    <button
      onClick={scrollToTop}
      className="fixed bottom-6 right-6 z-50 p-3 bg-mint opacity-90 rounded-sm hover:opacity-70 transition-opacity cursor-pointer"
      aria-label="맨 위로 이동"
    >
      <FaArrowUp className="text-white text-lg" />
    </button>
  );
}
