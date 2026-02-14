import { getDocumentTop10 } from "@/lib/api/document";
import { DocumentPage } from "@/lib/types/document";
import { formatRelativeTime2 } from "@/lib/utils/date";
import Link from "next/link";
import DocumentTitle from "../document/DocumentTitle";

export default async function Sidebar() {
  const data = await getDocumentTop10();
  const documents = data.documents || [];

  return (
    <div>
      <DocumentTitle text="인기문서" />

      {documents.length > 0 ? (
        <ol className="flex flex-col gap-2">
          {documents.map((document: DocumentPage, index: number) => (
            <li key={document.documentId}>
              <Link
                href={`/document/${document.documentId}`}
                className="flex items-center justify-between hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors px-1 py-1"
              >
                <div className="flex items-center gap-1">
                  <p className="text-mint text-sm font-bold dark:text-orange-400">{index + 1}.</p>
                  <p className="text-base hover:underline flex-1 truncate">
                    {document.title}
                  </p>
                </div>
                <p className="text-sm text-gray-400">
                  {formatRelativeTime2(document.lastModifiedAt)}
                </p>
              </Link>
            </li>
          ))}
        </ol>
      ) : (
        <div className="p-4">
          <div className="flex flex-col items-center">
            <p className="text-gray-400 font-bmhanna">
              데이터가 존재하지 않습니다.
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
