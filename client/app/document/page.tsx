import DocumentCards from "@/components/document/DocumentCards";
import DocumentTable from "@/components/document/DocumentTable";
import DocumentTitle from "@/components/document/DocumentTitle";

async function getLatestDocuments() {
  try {
    const response = await fetch(
      "http://localhost:8080/api/v1/documents?category=all&page=0",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-cache",
      }
    );

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}

export default async function DocumentPage() {
  const data = await getLatestDocuments();

  return (
    <div>
      <DocumentTitle text="최근편집" />

      <div className="mb-4">
        <div className="flex gap-2">
          <button className="font-bmhanna bg-mint text-white px-4 rounded-sm">
            전체
          </button>
          <button className="font-bmhanna bg-gray-400 text-white px-4 rounded-sm">
            런너
          </button>
          <button className="font-bmhanna bg-gray-400 text-white px-4 rounded-sm">
            길드
          </button>
        </div>
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
