import ActionButton from "@/components/ui/ActionButton";
import Badge from "@/components/ui/Badge";
import { Document, PageResponse } from "@/libs/types";
import { formatDate } from "@/libs/utils";

const mockData: PageResponse<Document[]> = {
  items: [
    {
      documentId: "214254638691082240",
      documentHistoryId: null,
      title: "가나다라마바사아자차",
      category: "런너",
      author: null,
      content: null,
      status: "ACTIVE",
      createdAt: "2025-08-14T14:31:26.522798",
      updatedAt: "2025-08-14T14:31:26.522823",
    },
    {
      documentId: "214254587994529792",
      documentHistoryId: null,
      title: "테스트",
      category: "길드",
      author: null,
      content: null,
      status: "READ_ONLY",
      createdAt: "2025-08-14T14:31:14.436",
      updatedAt: "2025-08-14T14:31:20.753028",
    },
  ],
  page: 1,
  size: 100,
  totalCount: 2,
};

export default function DocumentPage() {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="font-bm-hanna text-2xl">문서</h1>
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
                  제목
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  분류
                </th>

                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  상태
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  생성일
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  수정일
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  작업
                </th>
              </tr>
            </thead>
            <tbody className="bg-white">
              {mockData.items.map((document) => (
                <tr key={document.documentId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {document.documentId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900">
                      {document.title}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <Badge
                      color={document.category === "런너" ? "blue" : "purple"}
                      size="sm"
                    >
                      {document.category}
                    </Badge>
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <Badge
                      color={document.status === "ACTIVE" ? "green" : "orange"}
                      size="sm"
                    >
                      {document.status === "ACTIVE" ? "활성" : "읽기"}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {formatDate(document.createdAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {formatDate(document.updatedAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-center">
                    <div className="flex space-x-2 justify-center">
                      <ActionButton variant="active">활성</ActionButton>
                      <ActionButton variant="read">읽기</ActionButton>
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
