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
      <tr className="hover:bg-gray-100 transition-colors">
        <td className="px-3 py-3 text-center">{documentHistory.version}</td>
        <td className="px-3 py-3 text-center">
          <Link
            href={`/document/${documentHistory.documentId}/history/${documentHistory.documentHistoryId}`}
            className="block hover:underline transition-colors text-sm whitespace-nowrap"
          >
            {documentHistory.author}
          </Link>
        </td>
        <td className="px-3 py-3 text-center">
          {formatFileSize(documentHistory.size)}
        </td>
        <td className="px-3 py-3 whitespace-nowrap text-gray-500">
          {formatRelativeTime(documentHistory.createdAt)}
        </td>
      </tr>
    </>
  );
}
