import DocumentHistoryCards from "@/components/document/DocumentHistoryCards";
import DocumentHistoryTable from "@/components/document/DocumentHistoryTable";
import DocumentTitle from "@/components/document/DocumentTitle";
import Pagination from "@/components/document/Pagination";
import { getHistories } from "@/lib/api/document";
import { Metadata } from "next";
import Link from "next/link";
import { cache } from "react";
import { HiArrowLeft, HiPlus } from "react-icons/hi";

const getHistoriesCached = cache((id: number, page: number) =>
  getHistories(id, page)
);

export async function generateMetadata({
  params,
  searchParams,
}: {
  params: Promise<{ slug: string }>;
  searchParams: Promise<{ page?: string }>;
}): Promise<Metadata> {
  const { slug } = await params;
  const searchParam = await searchParams;
  const currentPage = searchParam.page ? parseInt(searchParam.page, 10) : 0;
  const documentId = parseInt(slug);

  try {
    const data = await getHistoriesCached(documentId, currentPage);

    return {
      title: `${data.payload[0].title}(편집기록) - 런너위키`,
      description: "누구나 쉽게 문서 편집 기록을 확인할 수 있습니다.",
    };
  } catch {
    return {
      title: "편집기록 - 런너위키",
      description: "누구나 쉽게 문서 편집 기록을 확인할 수 있습니다.",
    };
  }
}

export default async function DocumentHistoryPage({
  params,
  searchParams,
}: {
  params: Promise<{ slug: string }>;
  searchParams: Promise<{ page?: string }>;
}) {
  const { slug } = await params;
  const searchParam = await searchParams;
  const currentPage = searchParam.page ? parseInt(searchParam.page, 10) : 0;

  const documentId = parseInt(slug);
  const data = await getHistoriesCached(documentId, currentPage);

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text="편집기록" />
        <div className="flex gap-2">
          <Link
            href={`/document/${documentId}`}
            className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <HiArrowLeft className="lg:hidden" />
            <span className="hidden lg:inline">뒤로가기</span>
          </Link>
          <Link
            href={`/document/${documentId}/edit`}
            className="bg-mint font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <HiPlus className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집하기</span>
          </Link>
        </div>
      </div>
      <div>
        <p className="font-bmhanna text-lg text-gray-500 mb-2">
          {data.payload[0].title}
        </p>

        {/* 데스크탑 */}
        {data ? (
          <>
            <div className="hidden lg:block">
              <DocumentHistoryTable documentHistories={data.payload} />

              {data.payload.length === 0 && (
                <div className="p-4">
                  <div className="flex flex-col items-center">
                    <p className="text-gray-400 font-bmhanna">
                      데이터가 존재하지 않습니다.
                    </p>
                  </div>
                </div>
              )}
            </div>
          </>
        ) : (
          <div className="hidden lg:block p-4">
            <div className="flex flex-col items-center">
              <p className="text-red-600 font-bmhanna">에러가 발생했습니다.</p>
            </div>
          </div>
        )}

        {/* 모바일 */}
        {data ? (
          <div className="lg:hidden">
            <DocumentHistoryCards documentHistories={data.payload} />

            {data.payload.length === 0 && (
              <div className="p-4">
                <div className="flex flex-col items-center">
                  <p className="text-gray-400 font-bmhanna">
                    데이터가 존재하지 않습니다.
                  </p>
                </div>
              </div>
            )}
          </div>
        ) : (
          <div className="lg:hidden p-4">
            <div className="flex flex-col items-center">
              <p className="text-red-600 font-bmhanna">에러가 발생했습니다.</p>
            </div>
          </div>
        )}

        {/* 페이지네이션 */}
        {data && <Pagination pagination={data} basePath={`history`} />}
      </div>
    </div>
  );
}
