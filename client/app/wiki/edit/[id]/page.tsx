import Editor from "@/components/wiki/Editor";
import { DocumentHistory } from "@/libs/types";
import { Metadata } from "next";

type Params = Promise<{ id: string }>;

export const metadata: Metadata = {
  title: "문서 편집",
};

async function getDocumentHistory(
  documentHistoryId: string
): Promise<DocumentHistory | null> {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/v1/documents/histories/${documentHistoryId}`,
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

export default async function WikiEditPage({ params }: { params: Params }) {
  const { id } = await params;
  const documentHistory = await getDocumentHistory(id);

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <div className="flex flex-col gap-2">
          <h1 className="font-bm-hanna text-2xl">문서 편집</h1>
          <p className="text-sm text-red-500">
            문서 편집 시 IP가 기록되며, 악의적 이용 방지를 위해서만 사용됩니다.
          </p>
        </div>
      </div>
      {documentHistory ? (
        <Editor documentHistory={documentHistory} />
      ) : (
        <div>
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <div className="text-red-800 font-medium">
              데이터를 불러오는데 실패했습니다.
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
