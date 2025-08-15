"use client";

import ActionButton from "@/components/ui/ActionButton";
import Badge from "@/components/ui/Badge";
import CopyableText from "@/components/ui/CopyableText";
import { DocumentHistory, PageResponse } from "@/libs/types";
import { formatDate } from "@/libs/utils";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { AiOutlineLoading3Quarters } from "react-icons/ai";

export default function DocumentHistoryPage() {
  const router = useRouter();
  const [documentHistories, setDocumentHistories] = useState<PageResponse<
    DocumentHistory[]
  > | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchDocumentHistories = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/documents/histories?page=1`,
        {
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 200) {
        const data = await response.json();
        setDocumentHistories(data);
      } else {
        setError("문서 기록 목록을 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error(error);
      setError("문서 기록 목록을 불러오는데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const updateDocumentHistoryStatus = async (
    documentHistoryId: string,
    status: string
  ) => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/documents/histories/${documentHistoryId}?status=${status}`,
        {
          method: "PATCH",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 204) {
        setDocumentHistories((prevHistories) => {
          if (!prevHistories) return prevHistories;

          return {
            ...prevHistories,
            items: prevHistories.items.map((history) =>
              history.documentHistoryId === documentHistoryId
                ? {
                    ...history,
                    status: status === "RECOVER" ? "ACTIVE" : "DELETE",
                  }
                : history
            ),
          };
        });
      } else {
        setError("문서 기록 상태 변경에 실패했습니다.");
      }
    } catch (error) {
      console.error(error);
      setError("문서 기록 상태 변경에 실패했습니다.");
    }
  };

  useEffect(() => {
    const checkHealth = async () => {
      try {
        const response = await fetch(
          `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/health`,
          {
            credentials: "include",
          }
        );

        if (response.status !== 200) {
          router.push("/login");
          return;
        }

        await fetchDocumentHistories();
      } catch (error) {
        console.error(error);
        router.push("/login");
      }
    };

    checkHealth();
  }, [router]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="flex flex-col items-center space-y-4">
          <AiOutlineLoading3Quarters className="animate-spin text-4xl text-mint" />
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-red-500 text-lg">{error}</div>
      </div>
    );
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="font-bm-hanna text-2xl">문서 기록</h1>
      </div>

      <div className="bg-white rounded-lg">
        <div className="overflow-x-auto">
          <table className="min-w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  ID
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  HID
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900 w-1/3">
                  제목
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900 w-20 whitespace-nowrap">
                  버전
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900 w-20 whitespace-nowrap">
                  상태
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  IP
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900 w-32">
                  생성일
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  작업
                </th>
              </tr>
            </thead>
            <tbody className="bg-white">
              {documentHistories?.items.map((document) => (
                <tr
                  key={document.documentHistoryId}
                  className="hover:bg-gray-50"
                >
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <CopyableText text={document.documentId} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <CopyableText text={document.documentHistoryId} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap w-1/3">
                    <Link
                      href={`${process.env.NEXT_PUBLIC_APP_URL}/document/${document.documentId}/history/${document.documentHistoryId}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="hover:underline text-sm font-medium text-gray-900"
                    >
                      {document.title}
                    </Link>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center w-20 text-sm text-gray-900">
                    {document.version}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <Badge
                      color={document.status === "ACTIVE" ? "green" : "red"}
                      size="sm"
                    >
                      {document.status === "ACTIVE" ? "활성" : "삭제"}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-left text-sm text-gray-900">
                    <CopyableText text={document.clientIp} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 w-32">
                    {formatDate(document.createdAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-center text-gray-900">
                    <div className="flex space-x-2 justify-center">
                      <ActionButton
                        variant="active"
                        onClick={() =>
                          updateDocumentHistoryStatus(
                            document.documentHistoryId,
                            "RECOVER"
                          )
                        }
                      >
                        복구
                      </ActionButton>
                      <ActionButton
                        variant="delete"
                        onClick={() =>
                          updateDocumentHistoryStatus(
                            document.documentHistoryId,
                            "DELETE"
                          )
                        }
                      >
                        삭제
                      </ActionButton>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {(!documentHistories || documentHistories.items.length === 0) && (
          <div className="text-center py-12">
            <div className="text-gray-500 text-lg">
              문서 기록이 존재하지 않습니다.
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
