interface DocumentFormInputsProps {
  title: string;
  disabledTitle?: boolean;
  author: string;
  disabledAuthor?: boolean;
  onTitleChange: (title: string) => void;
  onAuthorChange: (author: string) => void;
}

export default function DocumentFormInputs({
  title,
  disabledTitle = false,
  author,
  disabledAuthor = false,
  onTitleChange,
  onAuthorChange,
}: DocumentFormInputsProps) {
  return (
    <div className="flex gap-4 flex-col lg:flex-row lg:gap-2">
      <input
        type="text"
        placeholder="제목을 입력해주세요."
        value={title}
        disabled={disabledTitle}
        onChange={(e) => onTitleChange(e.target.value)}
        className={`flex-2/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm transition-opacity ${
          disabledTitle
            ? "opacity-50 cursor-not-allowed bg-gray-100"
            : "bg-white"
        }`}
      />
      <input
        type="text"
        placeholder="작성자를 입력해주세요."
        value={author}
        disabled={disabledAuthor}
        onChange={(e) => onAuthorChange(e.target.value)}
        className={`flex-1/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm transition-opacity ${
          disabledAuthor
            ? "opacity-50 cursor-not-allowed bg-gray-100"
            : "bg-white"
        }`}
      />
    </div>
  );
}
