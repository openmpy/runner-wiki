export type DocumentStatus = "ACTIVE" | "READ_ONLY";
export type DocumentCategory = "런너" | "길드";

export interface PageResponse<T> {
  items: T;
  page: number;
  size: number;
  totalCount: number;
}

export interface Document {
  documentId: string;
  documentHistoryId: string;
  title: string;
  category: DocumentCategory;
  author: string;
  content: string;
  status: DocumentStatus;
  createdAt: string;
  updatedAt: string;
}

export interface PopularDocument {
  documentId: string;
  title: string;
  updatedAt: string;
}

export interface DocumentHistory {
  documentId: string;
  documentHistoryId: string;
  title: string;
  category: DocumentCategory;
  author: string;
  content: string;
  status: DocumentStatus;
  createdAt: string;
  updatedAt: string;
}

export interface DocumentHistoryItem {
  documentHistoryId: string;
  author: string;
  version: string;
  size: number;
  createdAt: string;
}

export interface SearchResult {
  id: string;
  title: string;
  category: DocumentCategory;
}

export interface SearchResponse {
  hits: SearchResult[];
  processingTimeMs: number;
  query: string;
  offset: number;
  limit: number;
  estimatedTotalHits: number;
}
