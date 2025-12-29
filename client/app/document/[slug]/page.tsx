import ToastViewer from "@/components/common/ToastViewer";
import DocumentTitle from "@/components/document/DocumentTitle";
import { getLatestDocument } from "@/lib/api/document";
import { formatRelativeTime } from "@/lib/utils/date";
import { Metadata } from "next";
import Link from "next/link";
import { cache } from "react";
import { HiPlus } from "react-icons/hi";
import { MdHistory } from "react-icons/md";

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
      title: `${data.title} - 런너위키`,
      description: `${data.content}`,
    };
  } catch {
    return {
      title: "런너위키",
      description: "누구나 쉽게 문서를 확인할 수 있습니다.",
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

  return (
    <div>
      <div className="flex justify-between items-start">
        <DocumentTitle text={data.title} />
        <div className="flex gap-2">
          <Link
            href={`/document/${documentId}/history`}
            className="bg-gray-400 font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <MdHistory className="lg:hidden font-bold" />
            <span className="hidden lg:inline">편집기록</span>
          </Link>
          <Link
            href={`/document/${documentId}/edit`}
            className="bg-mint font-bmhanna text-white rounded-sm hover:opacity-90 transition-opacity px-3 py-2 lg:py-1"
          >
            <HiPlus className="lg:hidden font-bold" />
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
