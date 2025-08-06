import { bmhanna } from "@/app/layout";
import TuiViewer from "@/components/toast/TuiViewer";
import Badge from "@/components/ui/Badge";
import Button from "@/components/ui/Button";
import TableOfContents from "@/components/wiki/TableOfContents";
import { formatDateTime } from "@/utils/timeUtils";
import Link from "next/link";

interface HistoryDetail {
  documentId: string;
  documentHistoryId: string;
  title: string;
  category: string;
  author: string;
  content: string;
  createdAt: string;
  updatedAt: string;
}

interface HistoryDetailPageProps {
  params: Promise<{
    slug: string;
    historyId: string;
  }>;
}

async function getHistoryDetail(
  documentId: string,
  historyId: string
): Promise<HistoryDetail> {
  const response = await fetch(
    `http://localhost:8080/api/v1/documents/histories/${historyId}`,
    {
      cache: "no-store",
    }
  );

  if (!response.ok) {
    throw new Error("히스토리를 불러올 수 없습니다.");
  }

  return response.json();
}

export default async function HistoryDetailPage({
  params,
}: HistoryDetailPageProps) {
  const { slug, historyId } = await params;
  let historyDetail: HistoryDetail;

  try {
    historyDetail = await getHistoryDetail(slug, historyId);
  } catch (error) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-red-500">
          {error instanceof Error
            ? error.message
            : "알 수 없는 오류가 발생했습니다."}
        </div>
      </div>
    );
  }

  return (
    <>
      <div className="flex items-center justify-between mb-6">
        <h1 className={`text-2xl ${bmhanna.className}`}>
          {historyDetail.title}
        </h1>
        <div className="flex gap-3">
          <Button variant="outline" size="sm">
            <Link href={`/wiki/${slug}/history`}>편집내역</Link>
          </Button>
          <Button variant="primary" size="sm">
            <Link href={`/wiki/${slug}/edit`}>편집하기</Link>
          </Button>
        </div>
      </div>

      <div className="mb-6">
        {historyDetail.category === "런너" ? (
          <Badge content={historyDetail.category} color="blue" />
        ) : (
          <Badge content={historyDetail.category} color="purple" />
        )}
      </div>

      <div>
        <TableOfContents content={historyDetail.content} />
        <TuiViewer content={historyDetail.content} />
      </div>

      <div className="mt-8 pt-6 border-t border-gray-200">
        <div className="text-sm text-gray-500">
          최근 편집: {formatDateTime(new Date(historyDetail.createdAt))}
        </div>
      </div>
    </>
  );
}
