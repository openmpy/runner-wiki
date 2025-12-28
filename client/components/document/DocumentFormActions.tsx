interface DocumentFormActionsProps {
  onSubmit: () => void;
  onCancel: () => void;
  isSubmitting: boolean;
}

export default function DocumentFormActions({
  onSubmit,
  onCancel,
  isSubmitting,
}: DocumentFormActionsProps) {
  return (
    <div className="flex justify-end gap-2">
      <button
        className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-1"
        onClick={onCancel}
      >
        뒤로가기
      </button>
      <button
        onClick={onSubmit}
        disabled={isSubmitting}
        className="bg-mint font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-1"
      >
        작성하기
      </button>
    </div>
  );
}

