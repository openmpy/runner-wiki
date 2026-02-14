import { DocumentHistoryPage } from "@/lib/types/document";
import { formatRelativeTime } from "@/lib/utils/date";
import { formatFileSize } from "@/lib/utils/utils";
import Link from "next/link";

interface DocumentHistoryTableRowProps {
  documentHistory: DocumentHistoryPage;
}

export default function DocumentHistoryTableRow({
  documentHistory,
}: DocumentHistoryTableRowProps) {
  return (
    <>
      <tr className="hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors">
        <td className="px-3 py-3 text-center text-base">{documentHistory.version}</td>
        <td className="px-3 py-3 text-center">
          <Link
            href={`/document/${documentHistory.documentId}/history/${documentHistory.documentHistoryId}`}
            className="block hover:underline transition-colors text-base whitespace-nowrap"
          >
            {documentHistory.author}
          </Link>
        </td>
        <td className="px-3 py-3 text-center text-base">
          {formatFileSize(documentHistory.size)}
        </td>
        <td className="px-3 py-3 whitespace-nowrap text-gray-500 dark:text-zinc-500 text-sm">
          {formatRelativeTime(documentHistory.createdAt)}
        </td>
      </tr>
    </>
  );
}
