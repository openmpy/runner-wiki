import DocumentCards from "@/components/document/DocumentCards";
import DocumentCategoryMenu from "@/components/document/DocumentCategoryMenu";
import DocumentTable from "@/components/document/DocumentTable";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getLatestDocuments } from "@/lib/api/document";

export default async function DocumentPage({
  searchParams,
}: {
  searchParams: Promise<{ category?: string }>;
}) {
  const params = await searchParams;
  const selectedCategory = params.category || "ALL";
  const data = await getLatestDocuments(selectedCategory);

  return (
    <div>
      <DocumentTitle text="최근편집" />

      <div className="mb-4">
        <DocumentCategoryMenu selectedCategory={selectedCategory} />
      </div>

      {/* 데스크탑 */}
      {data ? (
        <>
          <div className="hidden lg:block">
            <DocumentTable documents={data.payload} />

            {data.payload.length === 0 && (
              <div className="p-4">
                <div className="flex flex-col items-center">
                  <p className="text-gray-600">데이터가 존재하지 않습니다.</p>
                </div>
              </div>
            )}
          </div>
        </>
      ) : (
        <div className="hidden lg:block p-4">
          <div className="flex flex-col items-center">
            <p className="text-red-600">에러가 발생했습니다.</p>
          </div>
        </div>
      )}

      {/* 모바일 */}
      {data ? (
        <div className="lg:hidden">
          <DocumentCards documents={data.payload} />

          {data.payload.length === 0 && (
            <div className="p-4">
              <div className="flex flex-col items-center">
                <p className="text-gray-600">데이터가 존재하지 않습니다.</p>
              </div>
            </div>
          )}
        </div>
      ) : (
        <div className="lg:hidden p-4">
          <div className="flex flex-col items-center">
            <p className="text-red-600">에러가 발생했습니다.</p>
          </div>
        </div>
      )}
    </div>
  );
}
