import { DocumentPage } from "@/lib/types/document";
import DocumentCard from "./DocumentCard";

interface DocumentCardsProps {
  documents: DocumentPage[];
}

export default function DocumentCards({ documents }: DocumentCardsProps) {
  return (
    <>
      <div className="flex flex-col gap-2">
        {documents.map((it: DocumentPage) => (
          <DocumentCard key={it.documentId} document={it} />
        ))}
      </div>
    </>
  );
}
