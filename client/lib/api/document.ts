import { DocumentCategory } from "@/lib/types/document";

export async function getLatestDocuments(
  category: string,
  page: number = 0,
  size: number = 15
) {
  try {
    const response = await fetch(
      `https://api.runner.wiki/api/v1/documents?category=${category}&page=${page}&size=${size}`,
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
  const response = await fetch(`https://api.runner.wiki/api/v1/documents`, {
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
    `https://api.runner.wiki/api/v1/documents/${documentId}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-cache",
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "문서를 불러오는데 실패했습니다.");
  }

  return response.json();
}

export async function getHistories(
  documentHistoryId: number,
  page: number = 0,
  size: number = 15
) {
  try {
    const response = await fetch(
      `https://api.runner.wiki/api/v1/documents/${documentHistoryId}/histories?page=${page}&size=${size}`,
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

export async function getDocumentHistory(documentHistoryId: number) {
  const response = await fetch(
    `https://api.runner.wiki/api/v1/document-histories/${documentHistoryId}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      cache: "no-cache",
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "문서를 불러오는데 실패했습니다.");
  }

  return response.json();
}

export interface UpdateDocumentRequest {
  author: string;
  content: string;
}

export async function updateDocument(
  documentId: number,
  data: UpdateDocumentRequest
) {
  const response = await fetch(
    `https://api.runner.wiki/api/v1/documents/${documentId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "문서 편집에 실패했습니다.");
  }

  return response.json();
}

export async function searchDocuments(
  title: string,
  page: number = 0,
  size: number = 10
) {
  try {
    const response = await fetch(
      `https://api.runner.wiki/api/v1/documents/search?title=${title}&page=${page}&size=${size}`,
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

export async function getDocumentTop10() {
  try {
    const response = await fetch(
      `https://api.runner.wiki/api/v1/documents/top10`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        next: { revalidate: 300 },
      }
    );

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}
