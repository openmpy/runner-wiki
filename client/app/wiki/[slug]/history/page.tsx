import Badge from "@/components/ui/Badge";
import BackButton from "@/components/wiki/BackButton";
import EditButton from "@/components/wiki/EditButton";
import { DocumentStatus, PageResponse } from "@/libs/types";
import { formatRelativeTime } from "@/libs/utils";
import Link from "next/link";

type Params = Promise<{ slug: string }>;

interface DocumentHistoryItem {
  documentHistoryId: string;
  author: string;
  version: number;
  size: number;
  createdAt: string;
}

interface DocumentHistory {
  historyId: string;
  title: string;
  status: DocumentStatus;
  histories: DocumentHistoryItem[];
}

async function getDocumentHistories(
  documentId: string,
  page: number
): Promise<PageResponse<DocumentHistory> | null> {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents/${documentId}/histories?page=${page}`,
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

export default async function WikiHistoryPage({ params }: { params: Params }) {
  const { slug } = await params;
  const histories = await getDocumentHistories(slug, 1);

  return (
    <div>
      <div className="flex justify-between items-center mb-2">
        <h1 className="font-bm-hanna text-2xl">편집 기록</h1>
        <div className="flex gap-2">
          <BackButton />
          <EditButton
            status={histories?.items.status || "READ_ONLY"}
            documentHistoryId={histories?.items.histories[0].documentHistoryId}
          />
        </div>
      </div>
      {histories ? (
        <div className="bg-white rounded-lg overflow-hidden">
          <div className="flex justify-between items-center mb-6">
            <h1 className="font-bm-hanna text-xl text-gray-500">
              {histories.items.title}
            </h1>
          </div>
          {/* 모바일 카드 뷰 */}
          <div className="block md:hidden">
            <div className="space-y-4">
              {histories.items.histories.map((item: DocumentHistoryItem) => (
                <div
                  key={item.documentHistoryId}
                  className="bg-white border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
                >
                  <div className="flex items-start justify-between mb-3">
                    <Link
                      href={`/wiki/${slug}/history/${item.documentHistoryId}`}
                      className="text-lg font-medium text-gray-900 hover:underline flex-1 mr-3 block w-full h-full"
                    >
                      {item.author}
                    </Link>
                    <div className="text-lg font-medium text-gray-900">
                      <Badge size="sm" color="orange">
                        V{item.version}
                      </Badge>
                    </div>
                  </div>
                  <div className="text-sm text-gray-500">{item.size} bytes</div>
                  <div className="text-sm text-gray-500 mt-1">
                    {formatRelativeTime(item.createdAt)}
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
                    <th className="px-6 py-3 text-center text-gray-900 font-bold tracking-wider w-1/12">
                      버전
                    </th>
                    <th className="px-6 py-3 text-center text-gray-900 font-bold tracking-wider w-1/4">
                      작성자
                    </th>
                    <th className="px-6 py-3 text-center text-gray-900 font-bold tracking-wider w-1/8">
                      크기
                    </th>
                    <th className="px-6 py-3 text-left text-gray-900 font-bold tracking-wider w-1/6">
                      편집 시간
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {histories.items.histories.map(
                    (item: DocumentHistoryItem) => (
                      <tr
                        key={item.documentHistoryId}
                        className="hover:bg-gray-50 transition-colors"
                      >
                        <td className="px-6 py-3 whitespace-nowrap text-center">
                          <span className="text-sm font-medium text-gray-900">
                            {item.version}
                          </span>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap text-center">
                          <Link
                            href={`/wiki/${slug}/history/${item.documentHistoryId}`}
                            className="text-sm font-medium text-gray-900 hover:underline transition-all block w-full h-full"
                          >
                            {item.author}
                          </Link>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap text-sm text-gray-500 text-center">
                          {item.size} bytes
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap text-sm text-gray-500">
                          {formatRelativeTime(item.createdAt)}
                        </td>
                      </tr>
                    )
                  )}
                </tbody>
              </table>
            </div>
          </div>

          {histories.items.histories.length === 0 && (
            <div className="px-6 py-8 text-center text-gray-500">
              편집된 문서가 존재하지 않습니다.
            </div>
          )}
        </div>
      ) : (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 mt-4">
          <div className="text-red-800 font-medium">
            데이터를 불러오는데 실패했습니다.
          </div>
        </div>
      )}
    </div>
  );
}
