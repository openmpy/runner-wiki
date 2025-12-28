interface DocumentFormInputsProps {
  title: string;
  author: string;
  onTitleChange: (title: string) => void;
  onAuthorChange: (author: string) => void;
}

export default function DocumentFormInputs({
  title,
  author,
  onTitleChange,
  onAuthorChange,
}: DocumentFormInputsProps) {
  return (
    <div className="flex gap-4 flex-col lg:flex-row lg:gap-2">
      <input
        type="text"
        placeholder="제목을 입력해주세요."
        value={title}
        onChange={(e) => onTitleChange(e.target.value)}
        className="flex-2/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm"
      />
      <input
        type="text"
        placeholder="작성자를 입력해주세요."
        value={author}
        onChange={(e) => onAuthorChange(e.target.value)}
        className="flex-1/3 border border-gray-300 px-3 py-2 rounded-sm focus:outline-none text-sm"
      />
    </div>
  );
}

