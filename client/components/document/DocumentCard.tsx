import { DocumentPage } from "@/lib/types/document";
import Link from "next/link";
import DocumentCategoryBadge from "./DocumentCategoryBadge";

interface DocumentCardProps {
  document: DocumentPage;
}

export default function DocumentCard({ document }: DocumentCardProps) {
  return (
    <>
      <div className="border border-gray-200 rounded-sm px-3 py-3 hover:bg-gray-100 transition-colors">
        <div className="flex items-center justify-between mb-4">
          <Link
            href="/"
            className="block hover:underline transition-colors text-sm"
          >
            {document.title}
          </Link>
          <DocumentCategoryBadge category={document.category} />
        </div>
        <p className="text-gray-500 text-xs">{document.lastModifiedAt}</p>
      </div>
    </>
  );
}
