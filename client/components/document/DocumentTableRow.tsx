import { DocumentPage } from "@/lib/types/document";
import { formatRelativeTime } from "@/lib/utils/date";
import Link from "next/link";
import DocumentCategoryBadge from "./DocumentCategoryBadge";

interface DocumentTableRowProps {
  document: DocumentPage;
}

export default function DocumentTableRow({ document }: DocumentTableRowProps) {
  return (
    <>
      <tr className="hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors">
        <td className="px-3 py-3">
          <Link
            href={`/document/${document.documentId}`}
            className="block hover:underline transition-colors text-base whitespace-nowrap"
          >
            {document.title}
          </Link>
        </td>
        <td className="px-3 py-3 text-center">
          <DocumentCategoryBadge category={document.category} />
        </td>
        <td className="px-3 py-3 whitespace-nowrap text-gray-500 dark:text-zinc-500 text-sm">
          {formatRelativeTime(document.lastModifiedAt)}
        </td>
      </tr>
    </>
  );
}
