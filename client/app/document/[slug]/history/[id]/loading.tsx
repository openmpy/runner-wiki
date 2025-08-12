export default function Loading() {
  return (
    <div>
      <div className="animate-pulse">
        <div className="h-8 bg-gray-200 rounded w-1/2 mb-6"></div>
      </div>
      <div className="bg-white">
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
          <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
          <div className="h-4 bg-gray-200 rounded w-1/4"></div>
        </div>
      </div>
    </div>
  );
}
