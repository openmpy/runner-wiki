import { DocumentHistoryPage } from "@/lib/types/document";
import DocumentHistoryTableRow from "./DocumentHistoryTableRow";

interface DocumentTableProps {
  documentHistories: DocumentHistoryPage[];
}

export default function DocumentHistoryTable({
  documentHistories,
}: DocumentTableProps) {
  return (
    <>
      <table className="min-w-full divide-y divide-gray-200 dark:divide-zinc-700">
        <thead>
          <tr className="font-bold">
            <td className="px-3 py-3 w-1/12 text-center whitespace-nowrap">
              버전
            </td>
            <td className="px-3 py-3 text-center">작성자</td>
            <td className="px-3 py-3 w-1/6 text-center">크기</td>
            <td className="px-3 py-3 w-1/3">편집 시간</td>
          </tr>
        </thead>
        <tbody className="text-sm divide-y divide-gray-200 dark:divide-zinc-700">
          {documentHistories.map((it: DocumentHistoryPage) => (
            <DocumentHistoryTableRow
              key={it.documentHistoryId}
              documentHistory={it}
            />
          ))}
        </tbody>
      </table>
    </>
  );
}
