import { DocumentCategory } from "@/lib/types/document";

export async function getLatestDocuments(
  category: string,
  page: number = 0,
  size: number = 15
) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/v1/documents?category=${category}&page=${page}&size=${size}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-cache",
      }
    );

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}

export interface CreateDocumentRequest {
  title: string;
  category: DocumentCategory;
  author: string;
  content: string;
}

export async function createDocument(data: CreateDocumentRequest) {
  const response = await fetch(`http://localhost:8080/api/v1/documents`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "문서 작성에 실패했습니다.");
  }

  return response.json();
}

export async function getLatestDocument(documentId: number) {
  const response = await fetch(
    `http://localhost:8080/api/v1/documents/${documentId}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "문서를 불러오는데 실패했습니다.");
  }

  return response.json();
}
