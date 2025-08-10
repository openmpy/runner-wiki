import ToastViewer from "@/components/toast/ToastViwer";
import Button from "@/components/ui/Button";
import EditButton from "@/components/wiki/EditButton";
import { Document } from "@/libs/types";
import { formatKoreanDate } from "@/libs/utils";
import Link from "next/link";
import { BsClock } from "react-icons/bs";
import { FaHistory } from "react-icons/fa";

type Params = Promise<{ slug: string }>;

async function getDocument(documentId: string): Promise<Document | null> {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents/${documentId}`,
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

export default async function WikiSlugPage({ params }: { params: Params }) {
  const { slug } = await params;
  const document = await getDocument(slug);

  return (
    <div>
      {document ? (
        <div>
          <div className="flex justify-between items-center mb-6">
            <h1 className="font-bm-hanna text-2xl">{document.title}</h1>
            <div className="flex gap-2">
              <Link href={`/wiki/${slug}/history`}>
                <Button variant="secondary" className="flex items-center gap-2">
                  <FaHistory className="md:hidden" />
                  <span className="hidden md:inline">편집기록</span>
                </Button>
              </Link>
              <EditButton
                status={document.status}
                documentHistoryId={document.documentHistoryId}
              />
            </div>
          </div>
          <div className="prose max-w-none mb-8">
            <ToastViewer initialValue={document.content || ""} />
          </div>
          <div className="border-t border-gray-200 pt-4">
            <div className="flex items-center gap-2 text-sm text-gray-500">
              <BsClock />
              <span>수정 일자: {formatKoreanDate(document.updatedAt)}</span>
            </div>
          </div>
        </div>
      ) : (
        <div>
          <h1 className="font-bm-hanna text-2xl mb-6">문서 정보</h1>
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
