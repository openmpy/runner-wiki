import { DocumentHistoryPage } from "@/lib/types/document";
import DocumentHistoryCard from "./DocumentHistoryCard";

interface DocumentHistoryCardsProps {
  documentHistories: DocumentHistoryPage[];
}

export default function DocumentHistoryCards({
  documentHistories,
}: DocumentHistoryCardsProps) {
  return (
    <>
      <div className="flex flex-col gap-2">
        {documentHistories.map((it: DocumentHistoryPage) => (
          <DocumentHistoryCard
            key={it.documentHistoryId}
            documentHistory={it}
          />
        ))}
      </div>
    </>
  );
}
