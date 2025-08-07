import { FaRegCopyright } from "react-icons/fa";

const Footer = () => {
  return (
    <footer className="bg-mint py-4">
      <div className="flex items-center justify-center space-x-1">
        <FaRegCopyright className="text-md text-white" />
        <span className="text-md font-bm-hanna text-white">
          2025 레디스. All rights reserved.
        </span>
      </div>
    </footer>
  );
};

export default Footer;
