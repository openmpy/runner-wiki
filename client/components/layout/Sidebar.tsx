import { PopularDocument } from "@/libs/types";
import { formatRelativeTime2 } from "@/libs/utils";
import Link from "next/link";

async function getPopularDocuments(): Promise<PopularDocument[]> {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/v1/documents/popular`,
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
    return data.items || [];
  } catch (error) {
    console.error("인기 문서 조회 실패:", error);
    return [];
  }
}

const Sidebar = async () => {
  const popularDocuments = await getPopularDocuments();

  return (
    <aside
      className={`w-full lg:w-80 bg-white border-y border-mint lg:border p-5 lg:rounded-lg lg:border-mint lg:align-self-start lg:self-start lg:sticky lg:top-5`}
    >
      <div>
        <h1 className="font-bm-hanna text-2xl mb-4">인기 문서</h1>

        <div className="flex flex-col gap-4">
          {popularDocuments.length > 0 ? (
            popularDocuments.map((doc, index) => (
              <div
                key={doc.documentId}
                className="flex items-center justify-between gap-2"
              >
                <div className="flex items-center gap-2 w-3/5">
                  <span className="text-sm font-medium text-mint font-bm-hanna min-w-[10px]">
                    {index + 1}.
                  </span>
                  <Link
                    href={`/document/${doc.documentId}`}
                    className="truncate hover:underline"
                  >
                    {doc.title}
                  </Link>
                </div>
                <span className="text-xs text-gray-500">
                  {formatRelativeTime2(doc.updatedAt)}
                </span>
              </div>
            ))
          ) : (
            <p className="text-sm text-gray-500">인기 문서가 없습니다.</p>
          )}
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
