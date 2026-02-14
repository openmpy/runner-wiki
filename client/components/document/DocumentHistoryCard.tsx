import { DocumentHistoryPage } from "@/lib/types/document";
import { formatRelativeTime } from "@/lib/utils/date";
import { formatFileSize } from "@/lib/utils/utils";
import Link from "next/link";

interface DocumentHistoryCardProps {
  documentHistory: DocumentHistoryPage;
}

export default function DocumentHistoryCard({
  documentHistory,
}: DocumentHistoryCardProps) {
  return (
    <>
      <div className="border border-gray-200 dark:border-zinc-700 rounded-sm px-3 py-3 hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors">
        <div className="flex items-center justify-between mb-4">
          <Link
            href={`/document/${documentHistory.documentId}/history/${documentHistory.documentHistoryId}`}
            className="block hover:underline transition-colors text-base"
          >
            {documentHistory.author}
          </Link>
          <span className="rounded-sm font-bold px-2 py-0.5 text-sm bg-orange-100 text-orange-700 dark:bg-zinc-500 dark:text-zinc-200">
            버전 {documentHistory.version}
          </span>
        </div>
        <p className="text-gray-700 dark:text-zinc-500 text-sm mb-1">
          {formatFileSize(documentHistory.size)}
        </p>
        <p className="text-gray-500 dark:text-zinc-600 text-sm">
          {formatRelativeTime(documentHistory.createdAt)}
        </p>
      </div>
    </>
  );
}
