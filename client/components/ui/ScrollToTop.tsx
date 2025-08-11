"use client";

import { IoChevronDown, IoChevronUp } from "react-icons/io5";

const ScrollToTop = () => {
  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  const scrollToBottom = () => {
    window.scrollTo({
      top: document.documentElement.scrollHeight,
      behavior: "smooth",
    });
  };

  return (
    <div className="fixed bottom-6 right-6 flex flex-col z-50">
      <button
        onClick={scrollToTop}
        className="w-10 h-10 bg-mint/80 text-white rounded-none flex items-center justify-center border border-gray-200"
        aria-label="맨 위로 이동"
      >
        <IoChevronUp size={24} />
      </button>
      <button
        onClick={scrollToBottom}
        className="w-10 h-10 bg-mint/80 text-white rounded-none flex items-center justify-center border border-gray-200 border-t-0"
        aria-label="맨 아래로 이동"
      >
        <IoChevronDown size={24} />
      </button>
    </div>
  );
};

export default ScrollToTop;
