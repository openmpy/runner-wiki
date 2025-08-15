"use client";

import ActionButton from "@/components/ui/ActionButton";
import Badge from "@/components/ui/Badge";
import CopyableText from "@/components/ui/CopyableText";
import Pagination from "@/components/ui/Pagination";
import { Document, PageResponse } from "@/libs/types";
import { formatDate } from "@/libs/utils";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { AiOutlineLoading3Quarters } from "react-icons/ai";

export default function DocumentPage() {
  const router = useRouter();
  const [documents, setDocuments] = useState<PageResponse<Document[]> | null>(
    null
  );
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);

  const fetchDocuments = async (page: number = 1) => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/documents?page=${page}&size=10`,
        {
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 200) {
        const data = await response.json();
        setDocuments(data);
      } else {
        setError("문서 목록을 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error(error);
      setError("문서 목록을 불러오는데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const updateDocumentStatus = async (documentId: string, status: string) => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/documents/${documentId}?status=${status}`,
        {
          method: "PATCH",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 204) {
        // 상태 변경 성공 시 해당 문서의 상태만 업데이트
        setDocuments((prevDocuments) => {
          if (!prevDocuments) return prevDocuments;

          return {
            ...prevDocuments,
            items: prevDocuments.items.map((doc) =>
              doc.documentId === documentId
                ? {
                    ...doc,
                    status: status === "ACTIVE" ? "ACTIVE" : "READ_ONLY",
                  }
                : doc
            ),
          };
        });
      } else {
        setError("문서 상태 변경에 실패했습니다.");
      }
    } catch (error) {
      console.error(error);
      setError("문서 상태 변경에 실패했습니다.");
    }
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const deleteDocument = async (documentId: string) => {
    if (!confirm("정말로 이 문서를 삭제하시겠습니까?")) {
      return;
    }

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/admin/documents/${documentId}`,
        {
          method: "DELETE",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 204) {
        setDocuments((prevDocuments) => {
          if (!prevDocuments) return prevDocuments;

          return {
            ...prevDocuments,
            items: prevDocuments.items.filter(
              (doc) => doc.documentId !== documentId
            ),
            totalCount: prevDocuments.totalCount - 1,
          };
        });
      } else {
        setError("문서 삭제에 실패했습니다.");
      }
    } catch (error) {
      console.error(error);
      setError("문서 삭제에 실패했습니다.");
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

        await fetchDocuments(currentPage);
      } catch (error) {
        console.error(error);
        router.push("/login");
      }
    };

    checkHealth();
  }, [router, currentPage]);

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
        <h1 className="font-bm-hanna text-2xl">문서</h1>
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
                  제목
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  분류
                </th>

                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  상태
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  생성일
                </th>
                <th className="px-6 py-3 text-left font-bold text-gray-900">
                  수정일
                </th>
                <th className="px-6 py-3 text-center font-bold text-gray-900">
                  작업
                </th>
              </tr>
            </thead>
            <tbody className="bg-white">
              {documents?.items.map((document) => (
                <tr key={document.documentId} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <CopyableText text={document.documentId} />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Link
                      href={`${process.env.NEXT_PUBLIC_APP_URL}/document/${document.documentId}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="hover:underline text-sm font-medium text-gray-900"
                    >
                      {document.title}
                    </Link>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <Badge
                      color={document.category === "런너" ? "blue" : "purple"}
                      size="sm"
                    >
                      {document.category}
                    </Badge>
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-center">
                    <Badge
                      color={document.status === "ACTIVE" ? "green" : "orange"}
                      size="sm"
                    >
                      {document.status === "ACTIVE" ? "활성" : "읽기"}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {formatDate(document.createdAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {formatDate(document.updatedAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-center">
                    <div className="flex space-x-2 justify-center">
                      <ActionButton
                        variant="active"
                        onClick={() =>
                          updateDocumentStatus(document.documentId, "ACTIVE")
                        }
                      >
                        활성
                      </ActionButton>
                      <ActionButton
                        variant="read"
                        onClick={() =>
                          updateDocumentStatus(document.documentId, "READ_ONLY")
                        }
                      >
                        읽기
                      </ActionButton>
                      <ActionButton
                        variant="delete"
                        onClick={() => deleteDocument(document.documentId)}
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

        {(!documents || documents.items.length === 0) && (
          <div className="text-center py-12">
            <div className="text-gray-500 text-lg">
              문서가 존재하지 않습니다.
            </div>
          </div>
        )}

        {/* 페이지네이션 */}
        {documents && documents.totalCount > 0 && (
          <div className="mt-6">
            <Pagination
              currentPage={currentPage}
              totalPages={Math.ceil(documents.totalCount / documents.size)}
              onPageChange={handlePageChange}
            />
          </div>
        )}
      </div>
    </div>
  );
}
