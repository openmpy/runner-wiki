import { DocumentPage } from "@/lib/types/document";
import DocumentTableRow from "./DocumentTableRow";

interface DocumentTableProps {
  documents: DocumentPage[];
}

export default function DocumentTable({ documents }: DocumentTableProps) {
  return (
    <>
      <table className="min-w-full divide-y divide-gray-200">
        <thead>
          <tr className="font-bold">
            <td className="px-3 py-3">제목</td>
            <td className="px-3 py-3 w-1/6 text-center">카테고리</td>
            <td className="px-3 py-3 w-1/3">마지막 편집 시간</td>
          </tr>
        </thead>
        <tbody className="text-sm divide-y divide-gray-200">
          {documents.map((it: DocumentPage) => (
            <DocumentTableRow key={it.documentId} document={it} />
          ))}
        </tbody>
      </table>
    </>
  );
}
