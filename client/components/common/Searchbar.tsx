import { FaSearch } from "react-icons/fa";

export default function Searchbar() {
  return (
    <div className="relative flex items-center">
      <input
        type="text"
        placeholder="검색할 문서 제목을 입력해주세요."
        className="w-full lg:w-80 px-4 py-2 pl-4 pr-12 bg-white rounded-sm focus:outline-none text-sm"
      />
      <FaSearch className="absolute right-4 text-gray-400" />
    </div>
  );
}
