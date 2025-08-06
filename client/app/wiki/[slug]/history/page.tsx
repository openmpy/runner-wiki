"use client";

import { bmhanna } from "@/app/layout";
import Badge from "@/components/ui/Badge";
import Button from "@/components/ui/Button";
import Pagination from "@/components/ui/Pagination";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface HistoryItem {
  documentHistoryId: string;
  author: string;
  version: number;
  size: number;
  createdAt: string;
}

interface HistoryResponse {
  content: {
    historyId: string;
    title: string;
    history: HistoryItem[];
  };
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

async function getDocumentHistory(
  documentId: string,
  page: number = 0
): Promise<HistoryResponse | null> {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents/${documentId}/histories?page=${page}`,
      {
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-store",
      }
    );

    if (!response.ok) {
      throw new Error("API 요청에 실패했습니다");
    }

    const data: HistoryResponse = await response.json();
    return data;
  } catch (error) {
    console.error("문서 히스토리를 가져오는 중 오류 발생:", error);
    return null;
  }
}

interface HistoryPageProps {
  params: Promise<{
    slug: string;
  }>;
}

export default function HistoryPage({ params }: HistoryPageProps) {
  const router = useRouter();
  const [slug, setSlug] = useState<string>("");
  const [historyData, setHistoryData] = useState<HistoryResponse | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const initializePage = async () => {
      try {
        const { slug: documentSlug } = await params;
        setSlug(documentSlug);
        await loadHistoryData(documentSlug, 0);
      } catch (err) {
        setError("페이지 초기화 중 오류가 발생했습니다.");
        setLoading(false);
        console.error(err);
      }
    };

    initializePage();
  }, [params]);

  const loadHistoryData = async (documentId: string, page: number) => {
    setLoading(true);
    setError(null);

    try {
      const data = await getDocumentHistory(documentId, page);
      if (data) {
        setHistoryData(data);
        setCurrentPage(page);
      } else {
        setError("편집 내역을 불러올 수 없습니다.");
      }
    } catch (err) {
      setError("편집 내역을 불러오는 중 오류가 발생했습니다.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (page: number) => {
    if (slug) {
      loadHistoryData(slug, page);
    }
  };

  const handleGoBack = () => {
    router.back();
  };

  const handleEdit = () => {
    router.push(`/wiki/${slug}`);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  if (loading) {
    return (
      <>
        <div className="flex justify-between items-center mb-6">
          <h1 className={`text-2xl ${bmhanna.className}`}>편집 내역</h1>
          <div className="flex gap-3">
            <Button variant="outline" size="sm" onClick={handleGoBack}>
              뒤로가기
            </Button>
            <Button variant="primary" size="sm" onClick={handleEdit}>
              편집하기
            </Button>
          </div>
        </div>
        <div className="text-center py-8 text-gray-500">
          편집 내역을 불러오는 중...
        </div>
      </>
    );
  }

  if (error || !historyData) {
    return (
      <>
        <div className="flex justify-between items-center mb-6">
          <h1 className={`text-2xl ${bmhanna.className}`}>편집 내역</h1>
          <div className="flex gap-3">
            <Button variant="outline" size="sm" onClick={handleGoBack}>
              뒤로가기
            </Button>
            <Button variant="primary" size="sm" onClick={handleEdit}>
              편집하기
            </Button>
          </div>
        </div>
        <div className="text-center py-8 text-gray-500">
          {error || "편집 내역을 불러오는 중 오류가 발생했습니다."}
        </div>
      </>
    );
  }

  return (
    <>
      <div className="flex justify-between items-center mb-2">
        <h1 className={`text-2xl ${bmhanna.className}`}>편집 내역</h1>
        <div className="flex gap-3">
          <Button variant="outline" size="sm" onClick={handleGoBack}>
            뒤로가기
          </Button>
          <Button variant="primary" size="sm" onClick={handleEdit}>
            편집하기
          </Button>
        </div>
      </div>

      <h2 className={`text-lg text-gray-600 mb-6 ${bmhanna.className}`}>
        {historyData.content.title}
      </h2>

      {/* 데스크톱 테이블 - 모바일에서 숨김 */}
      <div className="hidden md:block overflow-x-auto">
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300 w-1/6">
                버전
              </th>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300 w-2/5">
                작성자
              </th>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300">
                크기
              </th>
              <th className="px-4 py-3 text-left text-[15px] font-bold text-gray-500 uppercase tracking-wider border-b-2 border-gray-300">
                작성일시
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {historyData.content.history.length === 0 ? (
              <tr>
                <td colSpan={4} className="px-4 py-8 text-center text-gray-500">
                  편집 내역이 없습니다.
                </td>
              </tr>
            ) : (
              historyData.content.history.map((item: HistoryItem) => (
                <tr
                  key={item.documentHistoryId}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <td className="px-4 py-3 whitespace-nowrap text-[15px] font-medium text-gray-900">
                    {item.version}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-[15px] text-gray-900">
                    {item.author}
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-[15px] text-gray-900">
                    {item.size}B
                  </td>
                  <td className="px-4 py-3 whitespace-nowrap text-[15px] text-gray-500">
                    {formatDate(item.createdAt)}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* 모바일 카드 목록 - 데스크톱에서 숨김 */}
      <div className="md:hidden">
        {historyData.content.history.length === 0 ? (
          <div className="text-center text-gray-500 py-8">
            편집 내역이 없습니다.
          </div>
        ) : (
          historyData.content.history.map(
            (item: HistoryItem, index: number) => (
              <div
                key={item.documentHistoryId}
                className={`bg-white border-b border-gray-200 px-2 py-4 hover:bg-gray-50 active:bg-gray-100 transition-colors ${
                  index === 0 ? "border-t" : ""
                }`}
              >
                <div className="space-y-1">
                  {/* 작성일시 */}
                  <div className="text-xs text-gray-500">
                    {formatDate(item.createdAt)}
                  </div>
                  {/* 작성자 */}
                  <div className="text-gray-900">{item.author}</div>
                  {/* 버전 */}
                  <div className="flex items-center gap-2">
                    <Badge
                      content={`V${item.version}`}
                      size="sm"
                      color="green"
                    />
                  </div>
                </div>
              </div>
            )
          )
        )}
      </div>

      {/* 페이지네이션 */}
      <Pagination
        currentPage={currentPage}
        totalPages={historyData.totalPages}
        onPageChange={handlePageChange}
      />
    </>
  );
}
