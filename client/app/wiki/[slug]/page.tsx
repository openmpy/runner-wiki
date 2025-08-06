import { bmhanna } from "@/app/layout";
import TuiViewer from "@/components/toast/TuiViewer";
import Badge from "@/components/ui/Badge";
import Button from "@/components/ui/Button";
import TableOfContents from "@/components/wiki/TableOfContents";
import { formatDateTime } from "@/utils/timeUtils";
import Link from "next/link";

interface DocumentData {
  documentId: string;
  documentHistoryId: string;
  title: string;
  category: string;
  author: string;
  content: string;
  createdAt: string;
  updatedAt: string;
}

interface WikiPageProps {
  params: Promise<{
    slug: string;
  }>;
}

async function getDocument(documentId: string): Promise<DocumentData> {
  const response = await fetch(
    `http://localhost:8080/api/v1/documents/${documentId}`,
    {
      cache: "no-store", // 캐시 비활성화
    }
  );

  if (!response.ok) {
    throw new Error("문서를 불러올 수 없습니다.");
  }

  return response.json();
}

export default async function WikiPage({ params }: WikiPageProps) {
  const { slug } = await params;
  let document: DocumentData;

  try {
    document = await getDocument(slug);
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
        <h1 className={`text-2xl ${bmhanna.className}`}>{document.title}</h1>
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
        {document.category === "런너" ? (
          <Badge content={document.category} color="blue" />
        ) : (
          <Badge content={document.category} color="purple" />
        )}
      </div>

      <div>
        <TableOfContents content={document.content} />
        <TuiViewer content={document.content} />
      </div>

      <div className="mt-8 pt-6 border-t border-gray-200">
        <div className="text-sm text-gray-500">
          최근 편집: {formatDateTime(new Date(document.updatedAt))}
        </div>
      </div>
    </>
  );
}
