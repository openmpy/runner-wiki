interface DocumentFormActionsProps {
  onSubmit: () => void;
  onCancel: () => void;
  isSubmitting: boolean;
  disabled?: boolean;
}

export default function DocumentFormActions({
  onSubmit,
  onCancel,
  isSubmitting,
  disabled = false,
}: DocumentFormActionsProps) {
  const isDisabled = isSubmitting || disabled;

  return (
    <div className="flex justify-end gap-2">
      <button
        className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-1 cursor-pointer"
        onClick={onCancel}
        type="button"
      >
        뒤로가기
      </button>

      <button
        onClick={onSubmit}
        disabled={isDisabled}
        type="button"
        className={`bg-mint font-bmhanna text-white rounded-sm px-3 py-1
          ${
            isDisabled
              ? "opacity-50 cursor-not-allowed"
              : "hover:opacity-90 cursor-pointer"
          }`}
      >
        제출하기
      </button>
    </div>
  );
}
