import BackButton from "@/components/wiki/BackButton";
import EditButton from "@/components/wiki/EditButton";
import HistoryList from "@/components/wiki/HistoryList";
import {
  DocumentHistoryItem,
  DocumentStatus,
  PageResponse,
} from "@/libs/types";

type Params = Promise<{ slug: string }>;

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
            documentHistoryId={histories?.items.histories[0]?.documentHistoryId}
          />
        </div>
      </div>

      {histories ? (
        <div>
          <div className="bg-white rounded-lg overflow-hidden">
            <div className="flex justify-between items-center mb-6">
              <h1 className="font-bm-hanna text-xl text-gray-500">
                {histories.items.title}
              </h1>
            </div>
            <HistoryList initialData={histories} documentId={slug} />
          </div>
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
