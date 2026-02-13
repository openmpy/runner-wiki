import { DocumentCategory } from "@/lib/types/document";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export const MAX_PAGE = 10000;

export async function getLatestDocuments(
  category: string,
  page: number = 0,
  size: number = 15
) {
  try {
    page = Math.min(Math.max(0, page), MAX_PAGE);

    const response = await fetch(
      `${API_BASE_URL}/v2/documents?category=${category}&page=${page}&size=${size}`,
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

export interface PresignImageResponse {
  uploadUrl: string;
  imageUrl: string;
}

export async function getPresignImageUrl(
  contentType: string
): Promise<PresignImageResponse> {
  const response = await fetch(
    `${API_BASE_URL}/v1/document-images/presign`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ contentType }),
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Presigned URL 발급에 실패했습니다.");
  }

  return response.json();
}

export interface CreateDocumentRequest {
  title: string;
  category: DocumentCategory;
  author: string;
  content: string;
  imageUrls?: string[];
  token: string;
}

export async function createDocument(data: CreateDocumentRequest) {
  const response = await fetch(`${API_BASE_URL}/v1/documents`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    if (response.status == 403) {
      throw new Error("봇 방지 인증에 실패했습니다.");
    }

    const error = await response.json();
    throw new Error(error.message || "문서 작성에 실패했습니다.");
  }

  return response.json();
}

export async function getLatestDocument(documentId: number) {
  const response = await fetch(`${API_BASE_URL}/v1/documents/${documentId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    cache: "no-cache",
  });

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
    page = Math.min(Math.max(0, page), MAX_PAGE);
    
    const response = await fetch(
      `${API_BASE_URL}/v1/documents/${documentHistoryId}/histories?page=${page}&size=${size}`,
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
    `${API_BASE_URL}/v1/document-histories/${documentHistoryId}`,
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
  imageUrls?: string[];
  token: string;
}

export async function updateDocument(
  documentId: number,
  data: UpdateDocumentRequest
) {
  const response = await fetch(`${API_BASE_URL}/v1/documents/${documentId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    if (response.status == 403) {
      throw new Error("봇 방지 인증에 실패했습니다.");
    }
    
    const error = await response.json();
    throw new Error(error.message || "문서 편집에 실패했습니다.");
  }

  return response.json();
}

export async function searchDocuments(
  keyword: string,
  cursorId: number | null = null,
  size: number = 15
) {
  try {
    const params = new URLSearchParams({
      keyword,
      size: String(size),
    });
    if (cursorId != null) {
      params.set("cursorId", String(cursorId));
    }
    const response = await fetch(
      `${API_BASE_URL}/v2/documents/search?${params.toString()}`,
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
    const response = await fetch(`${API_BASE_URL}/v1/documents/top10`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      next: { revalidate: 300 },
    });

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}

export async function getShuffleDocument() {
  try {
    const response = await fetch(`${API_BASE_URL}/v1/documents/shuffle`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();
    return data;
  } catch (error) {
    console.error(error);
  }
}
