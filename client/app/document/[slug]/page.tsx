import ToastViewer from "@/components/common/ToastViewer";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getLatestDocument } from "@/lib/api/document";
import { formatRelativeTime } from "@/lib/utils/date";
import Link from "next/link";
import { LuHistory, LuPlus } from "react-icons/lu";

export default async function DocumentDetailPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = await params;
  const documentId = parseInt(slug);
  const data = await getLatestDocument(documentId);

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text={data.title} />
        <div className="flex gap-2">
          <Link
            href={`/document/${documentId}/history`}
            className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <LuHistory className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집기록</span>
          </Link>
          <Link
            href={`/document/${documentId}/edit`}
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
