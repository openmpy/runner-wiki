import Badge from "@/components/ui/Badge";
import { Document, PageResponse } from "@/libs/types";
import { formatRelativeTime } from "@/libs/utils";
import Link from "next/link";

async function getDocumentHistories(): Promise<PageResponse<
  Document[]
> | null> {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/v1/documents/latest?page=1`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-store",
      }
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export default async function WikiPage() {
  const histories = await getDocumentHistories();

  return (
    <div>
      <h1 className="font-bm-hanna text-2xl mb-6">최근 편집</h1>

      {histories ? (
        <div className="bg-white rounded-lg overflow-hidden">
          {/* 모바일 카드 뷰 */}
          <div className="block md:hidden">
            <div className="space-y-4">
              {histories.items.map((item: Document) => (
                <div
                  key={item.documentId}
                  className="bg-white border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-start justify-between mb-3">
                    <Link
                      href={`/wiki/${item.documentId}`}
                      className="text-lg font-medium text-gray-900 hover:underline flex-1 mr-3"
                    >
                      {item.title}
                    </Link>
                    {item.category === "런너" ? (
                      <Badge size="sm" color="blue">
                        {item.category}
                      </Badge>
                    ) : (
                      <Badge size="sm" color="purple">
                        {item.category}
                      </Badge>
                    )}
                  </div>
                  <div className="text-sm text-gray-500">
                    {formatRelativeTime(item.updatedAt)}
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* 데스크톱 테이블 뷰 */}
          <div className="hidden md:block">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th className="px-6 py-3 text-left text-gray-900 font-bold tracking-wider w-1/2">
                      제목
                    </th>
                    <th className="px-6 py-3 text-center text-gray-900 font-bold tracking-wider">
                      카테고리
                    </th>
                    <th className="px-6 py-3 text-left text-gray-900 font-bold tracking-wider">
                      편집 시간
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {histories.items.map((item: Document) => (
                    <tr
                      key={item.documentId}
                      className="hover:bg-gray-50 transition-colors"
                    >
                      <td className="px-6 py-3 whitespace-nowrap">
                        <Link
                          href={`/wiki/${item.documentId}`}
                          className="block text-sm font-medium text-gray-900 hover:underline"
                        >
                          {item.title}
                        </Link>
                      </td>
                      <td className="px-6 py-3 whitespace-nowrap text-center">
                        {item.category === "런너" ? (
                          <Badge size="sm" color="blue">
                            {item.category}
                          </Badge>
                        ) : (
                          <Badge size="sm" color="purple">
                            {item.category}
                          </Badge>
                        )}
                      </td>
                      <td className="px-6 py-3 whitespace-nowrap text-sm text-gray-500">
                        {formatRelativeTime(item.updatedAt)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {histories.items.length === 0 && (
            <div className="px-6 py-8 text-center text-gray-500">
              편집된 문서가 존재하지 않습니다.
            </div>
          )}
        </div>
      ) : (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
          <div className="text-red-800 font-medium">
            데이터를 불러오는데 실패했습니다.
          </div>
        </div>
      )}
    </div>
  );
}
