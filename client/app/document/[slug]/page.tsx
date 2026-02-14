import TableOfContents from "@/components/common/TableOfContents";
import ToastViewer from "@/components/common/ToastViewer";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getLatestDocument } from "@/lib/api/document";
import { getTocFromMarkdown } from "@/lib/toc";
import { formatRelativeTime } from "@/lib/utils/date";
import { Metadata } from "next";
import Link from "next/link";
import { cache } from "react";
import { FaHistory, FaPlus } from "react-icons/fa";

const getLatestDocumentCached = cache((id: number) => getLatestDocument(id));

export async function generateMetadata({
  params,
}: {
  params: Promise<{ slug: string }>;
}): Promise<Metadata> {
  const { slug } = await params;
  const documentId = parseInt(slug);

  try {
    const data = await getLatestDocumentCached(documentId);

    return {
      title: `${data.title} - 런너위키 (테일즈런너)`,
      description: `${data.content}`,
    };
  } catch {
    return {
      title: "런너위키 (테일즈런너)",
      description:
        "테일즈런너를 플레이하는 유저라면 누구나 문서를 열람할 수 있습니다.",
    };
  }
}

export default async function DocumentDetailPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = await params;
  const documentId = parseInt(slug);
  const data = await getLatestDocumentCached(documentId);
  const toc = getTocFromMarkdown(data.content);

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text={data.title} />
        <div className="flex gap-2">
          <Link
            href={`/document/${documentId}/history`}
            className="bg-gray-400 dark:bg-zinc-500 font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 transition-opacity px-4 py-2 lg:py-1"
          >
            <FaHistory className="lg:hidden" />
            <span className="hidden lg:inline">편집기록</span>
          </Link>
          <Link
            href={`/document/${documentId}/edit`}
            className="bg-mint dark:bg-zinc-700 font-bmhanna text-white dark:text-zinc-200 rounded-sm hover:opacity-90 transition-opacity px-4 py-2 lg:py-1"
          >
            <FaPlus className="lg:hidden" />
            <span className="hidden lg:inline">편집하기</span>
          </Link>
        </div>
      </div>

      <div className="flex flex-col">
        <TableOfContents items={toc} />
        <ToastViewer initialValue={data.content} />

        <div className="text-sm text-gray-600 dark:text-zinc-600 border-t border-t-gray-300 dark:border-t-zinc-700 pt-3">
          마지막 편집 시간: {formatRelativeTime(data.lastModifiedAt)}
        </div>
      </div>
    </div>
  );
}
