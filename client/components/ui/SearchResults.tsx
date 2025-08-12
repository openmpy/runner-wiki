import { SearchResult } from "@/libs/types";
import Link from "next/link";
import Badge from "./Badge";

interface SearchResultsProps {
  results: SearchResult[];
  isVisible: boolean;
  onResultClick: () => void;
}

const SearchResults = ({
  results,
  isVisible,
  onResultClick,
}: SearchResultsProps) => {
  if (!isVisible || results.length === 0) return null;

  return (
    <div className="absolute top-full left-0 right-0 mt-2 bg-white border border-gray-200 shadow-md rounded-lg z-50 max-h-96 overflow-y-auto">
      {results.map((result) => (
        <Link
          key={result.id}
          href={`/document/${result.id}`}
          onClick={onResultClick}
          className="block px-4 py-3 hover:bg-gray-50 border-b border-gray-100 last:border-b-0 transition-colors duration-200"
        >
          <div className="flex items-center justify-between">
            <div className="flex-1">
              <h4 className="text-gray-900 font-medium text-sm">
                {result.title}
              </h4>
            </div>
            <div className="ml-4">
              {result.category === "런너" ? (
                <Badge color="blue" className="text-xs">
                  {result.category}
                </Badge>
              ) : (
                <Badge color="purple" className="text-xs">
                  {result.category}
                </Badge>
              )}
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
};

export default SearchResults;
