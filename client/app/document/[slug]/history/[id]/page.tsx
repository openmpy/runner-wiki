import ToastViewer from "@/components/common/ToastViewer";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getDocumentHistory } from "@/lib/api/document";
import { formatRelativeTime } from "@/lib/utils/date";
import { Metadata } from "next";
import Link from "next/link";
import { LuHistory, LuPlus } from "react-icons/lu";

export async function generateMetadata({
  params,
}: {
  params: Promise<{ id: string }>;
}): Promise<Metadata> {
  const { id } = await params;
  const documentHistoryId = parseInt(id);

  try {
    const data = await getDocumentHistory(documentHistoryId);

    return {
      title: `${data.title}(기록) - 런너위키`,
      description: `${data.content}`,
    };
  } catch {
    return {
      title: "런너위키",
      description: "누구나 쉽게 문서 기록을 확인할 수 있습니다.",
    };
  }
}

export default async function DocumentHistoryDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const documentHistoryId = parseInt(id);
  const data = await getDocumentHistory(documentHistoryId);

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text={data.title} />
        <div className="flex gap-2">
          <Link
            href={`/document/${data.documentId}/history`}
            className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <LuHistory className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집기록</span>
          </Link>
          <Link
            href={`/document/${data.documentId}/edit`}
            className="bg-mint font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <LuPlus className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집하기</span>
          </Link>
        </div>
      </div>
      <div className="flex flex-col">
        <ToastViewer initialValue={data.content} />
        <div className="text-xs text-gray-600 border-t border-t-gray-300 pt-3">
          마지막 편집 시간: {formatRelativeTime(data.lastModifiedAt)}
        </div>
      </div>
    </div>
  );
}
