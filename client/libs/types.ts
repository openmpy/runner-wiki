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
  category: string;
  author: string | null;
  content: string | null;
  status: "ACTIVE" | "READ_ONLY";
  createdAt: string;
  updatedAt: string;
}
