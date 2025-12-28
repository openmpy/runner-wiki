export type DocumentCategory = "USER" | "GUILD";

export interface DocumentPage {
  documentId: number;
  title: string;
  category: DocumentCategory;
  lastModifiedAt: string;
}

export interface DocumentHistoryPage {
  title: string;
  documentHistoryId: number;
  author: string;
  version: number;
  size: number;
  createdAt: string;
}
