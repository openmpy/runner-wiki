export default function Loading() {
  return (
    <div>
      <h1 className="font-bm-hanna text-2xl mb-6">최근 편집</h1>
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
