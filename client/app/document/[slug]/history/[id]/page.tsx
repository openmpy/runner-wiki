import TableOfContents from "@/components/common/TableOfContents";
import ToastViewer from "@/components/common/ToastViewer";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getDocumentHistory } from "@/lib/api/document";
import { getTocFromMarkdown } from "@/lib/toc";
import { formatRelativeTime } from "@/lib/utils/date";
import { Metadata } from "next";
import Link from "next/link";
import { cache } from "react";
import { LuHistory, LuPlus } from "react-icons/lu";

const getDocumentHistoryCached = cache((id: number) => getDocumentHistory(id));

export async function generateMetadata({
  params,
}: {
  params: Promise<{ id: string }>;
}): Promise<Metadata> {
  const { id } = await params;
  const documentHistoryId = parseInt(id);

  try {
    const data = await getDocumentHistoryCached(documentHistoryId);

    return {
      title: `테일즈런너 | ${data.title} - 런너위키`,
      description: `${data.content}`,
    };
  } catch {
    return {
      title: "런너위키",
      description:
        "테일즈런너를 플레이하는 유저라면 누구나 문서 기록을 열람할 수 있습니다.",
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
  const data = await getDocumentHistoryCached(documentHistoryId);
  const toc = getTocFromMarkdown(data.content);

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text={data.title} />
        <div className="flex gap-2">
          <Link
            href={`/document/${data.documentId}/history`}
            className="bg-gray-400 dark:bg-zinc-500 font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <LuHistory className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집기록</span>
          </Link>
          <Link
            href={`/document/${data.documentId}/edit`}
            className="bg-mint dark:bg-zinc-700 font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <LuPlus className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집하기</span>
          </Link>
        </div>
      </div>
      <div className="flex flex-col">
        <TableOfContents items={toc} />
        <ToastViewer initialValue={data.content} />

        <div className="text-xs text-gray-600 dark:text-zinc-600 border-t border-t-gray-300 dark:border-t-zinc-700 pt-3">
          마지막 편집 시간: {formatRelativeTime(data.lastModifiedAt)}
        </div>
      </div>
    </div>
  );
}
