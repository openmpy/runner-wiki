import { DocumentPage } from "@/lib/types/document";
import { formatRelativeTime } from "@/lib/utils/date";
import Link from "next/link";
import DocumentCategoryBadge from "./DocumentCategoryBadge";

interface DocumentCardProps {
  document: DocumentPage;
}

export default function DocumentCard({ document }: DocumentCardProps) {
  return (
    <>
      <div className="border border-gray-200 dark:border-zinc-700 rounded-sm px-3 py-3 hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors">
        <div className="flex items-center justify-between mb-4">
          <Link
            href={`/document/${document.documentId}`}
            className="block hover:underline transition-colors text-base"
          >
            {document.title}
          </Link>
          <DocumentCategoryBadge category={document.category} />
        </div>
        <p className="text-gray-500 dark:text-zinc-500 text-sm">
          {formatRelativeTime(document.lastModifiedAt)}
        </p>
      </div>
    </>
  );
}
