export type DocumentStatus = "ACTIVE" | "READ_ONLY";
export type DocumentHistoryStatus = "ACTIVE" | "DELETE";
export type DocumentCategory = "런너" | "길드";

export interface PageResponse<T> {
  items: T;
  page: number;
  size: number;
  totalCount: number;
}

export interface Document {
  documentId: string;
  documentHistoryId: string | null;
  title: string;
  category: DocumentCategory;
  author: string | null;
  content: string | null;
  status: DocumentStatus;
  createdAt: string;
  updatedAt: string;
}

export interface DocumentHistory {
  documentId: string;
  documentHistoryId: string;
  title: string;
  author: string;
  version: string;
  size: number;
  clientIp: string;
  status: DocumentHistoryStatus;
  createdAt: string;
}
