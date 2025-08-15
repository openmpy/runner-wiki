import ActionButton from "@/components/ui/ActionButton";
import CopyableText from "@/components/ui/CopyableText";
import { DocumentHistory, PageResponse } from "@/libs/types";
import { formatDate } from "@/libs/utils";
import Link from "next/link";

const mockData: PageResponse<DocumentHistory[]> = {
  items: [
    {
      documentId: "214552394810224640",
      documentHistoryId: "214552394810224641",
      title: "가나다라마바아자차",
      author: "ㅇㅇ",
      version: "1",
      size: 2,
      clientIp: "192.168.0.1",
      createdAt: "2025-08-14T14:35:57.421296",
    },
    {
      documentId: "214254587994529792",
      documentHistoryId: "214255774922235904",
      title: "테스트",
      author: "ㅇㅇ",
      version: "2",
      size: 1,
      clientIp: "192.168.0.2",
      createdAt: "2025-08-14T14:35:57.421296",
    },
  ],
  page: 1,
  size: 100,
  totalCount: 2,
};

export default function DocumentHistoryPage() {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="font-bm-hanna text-2xl">문서 기록</h1>
      </div>

      <div className="bg-white rounded-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  ID
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  HID
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900 w-1/3">
                  제목
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900 w-20 whitespace-nowrap">
                  버전
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  IP
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900 w-32">
                  생성일
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  작업
                </th>
              </tr>
            </thead>
            <tbody className="bg-white">
              {mockData.items.map((document) => (
                <tr
                  key={document.documentHistoryId}
                  className="hover:bg-gray-50"
                >
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <CopyableText text={document.documentId} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <CopyableText text={document.documentHistoryId} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap w-1/3">
                    <Link
                      href={`${process.env.NEXT_PUBLIC_APP_URL}/document/${document.documentId}/history/${document.documentHistoryId}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="hover:underline text-sm font-medium text-gray-900"
                    >
                      {document.title}
                    </Link>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center w-20 text-sm text-gray-900">
                    {document.version}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-left text-sm text-gray-900">
                    <CopyableText text={document.clientIp} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 w-32">
                    {formatDate(document.createdAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-center text-gray-900">
                    <div className="flex space-x-2 justify-center">
                      <ActionButton variant="delete">삭제</ActionButton>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {mockData.items.length === 0 && (
          <div className="text-center py-12">
            <div className="text-gray-500 text-lg">
              문서가 존재하지 않습니다.
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
