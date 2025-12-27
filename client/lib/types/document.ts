export type DocumentCategory = "USER" | "GUILD";

export interface DocumentPage {
  documentId: number;
  title: string;
  category: DocumentCategory;
  lastModifiedAt: string;
}
