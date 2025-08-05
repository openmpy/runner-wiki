import { bmhanna } from "@/app/layout";
import Badge from "@/components/ui/Badge";
import { getRelativeTime } from "@/utils/timeUtils";
import Link from "next/link";

interface Document {
  documentId: string;
  documentHistoryId: string | null;
  title: string;
  category: string;
  author: string | null;
  content: string | null;
  createdAt: string | null;
  updatedAt: string;
}

interface ApiResponse {
  content: Document[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

async function getDocuments(): Promise<Document[]> {
  try {
    const response = await fetch(
      "http://localhost:8080/api/v1/documents/latest?page=0&sort=updatedAt",
      {
        headers: {
          "Content-Type": "application/json",
        },
        // 서버 사이드에서 캐시 설정
        next: { revalidate: 60 }, // 60초마다 재검증
      }
    );

    if (!response.ok) {
      throw new Error("API 요청에 실패했습니다");
    }

    const data: ApiResponse = await response.json();
    return data.content;
  } catch (error) {
    console.error("문서 목록을 가져오는 중 오류 발생:", error);
    return [];
  }
}

export default async function RecentPage() {
  const documents = await getDocuments();

  return (
    <>
      <h1 className={`text-2xl mb-6 ${bmhanna.className}`}>최근 편집내역</h1>

      {/* 데스크톱 테이블 - 모바일에서 숨김 */}
      <div className="hidden md:block overflow-x-auto">
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300 w-2/3">
                문서
              </th>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300 w-1/6">
                분류
              </th>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300 w-1/6">
                편집 시간
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {documents.length === 0 ? (
              <tr>
                <td colSpan={3} className="px-4 py-8 text-center text-gray-500">
                  문서가 없습니다.
                </td>
              </tr>
            ) : (
              documents.map((doc) => (
                <tr
                  key={doc.documentId}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <td className="px-4 py-3 whitespace-nowrap text-[15px]">
                    <Link
                      href={`/wiki/${doc.documentId}`}
                      className="text-gray-900 font-medium hover:underline cursor-pointer"
                    >
                      {doc.title}
                    </Link>
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-[15px]">
                    <Badge category={doc.category} className="text-[15px]" />
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-[15px] text-gray-500">
                    {doc.updatedAt ? getRelativeTime(doc.updatedAt) : "-"}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* 모바일 카드 목록 - 데스크톱에서 숨김 */}
      <div className="md:hidden">
        {documents.length === 0 ? (
          <div className="text-center text-gray-500 py-8">문서가 없습니다.</div>
        ) : (
          documents.map((doc, index) => (
            <div
              key={doc.documentId}
              className={`bg-white border-b border-gray-200 px-2 py-4 hover:bg-gray-50 active:bg-gray-100 transition-colors ${
                index === 0 ? "border-t" : ""
              }`}
            >
              <div className="space-y-1">
                {/* 편집 시간 */}
                <div className="text-xs text-gray-500">
                  {doc.updatedAt ? getRelativeTime(doc.updatedAt) : "-"}
                </div>
                {/* 제목 */}
                <div>
                  <Link
                    href={`/wiki/${doc.documentId}`}
                    className="text-gray-900 font-medium text-lg hover:underline cursor-pointer"
                  >
                    {doc.title}
                  </Link>
                </div>

                {/* 카테고리 */}
                <div>
                  <Badge category={doc.category} />
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </>
  );
}
