import Link from "next/link";

export default function DocumentPage() {
  return (
    <div>
      <h1 className="font-bmhanna text-xl lg:text-2xl mb-4">최근편집</h1>
      <div className="flex gap-2 mb-4">
        <button className="font-bmhanna bg-mint text-white px-4 rounded-sm">
          전체
        </button>
        <button className="font-bmhanna bg-gray-400 text-white px-4 rounded-sm">
          런너
        </button>
        <button className="font-bmhanna bg-gray-400 text-white px-4 rounded-sm">
          길드
        </button>
      </div>

      {/* 데스크탑 */}
      <div className="hidden lg:block">
        <table className="min-w-full divide-y divide-gray-200">
          <thead>
            <tr className="font-bold">
              <td className="px-3 py-3">제목</td>
              <td className="px-3 py-3 w-1/6 text-center">카테고리</td>
              <td className="px-3 py-3 w-1/3">마지막 편집 시간</td>
            </tr>
          </thead>
          <tbody className="text-sm divide-y divide-gray-200">
            <tr className="hover:bg-gray-100 transition-colors">
              <td className="px-3 py-3">
                <Link
                  href="/"
                  className="block hover:underline transition-colors text-sm whitespace-nowrap"
                >
                  제목제목제목제목제목
                </Link>
              </td>
              <td className="px-3 py-3 text-center">
                <span className="rounded-sm bg-blue-100 text-blue-700 font-bold px-2 py-0.5 text-xs">
                  런너
                </span>
              </td>
              <td className="px-3 py-3 whitespace-nowrap text-gray-500 text-xs">
                2025년 11월 28일 15시 57분 13초
              </td>
            </tr>
            <tr className="hover:bg-gray-100 transition-colors">
              <td className="px-3 py-3 whitespace-nowrap">
                <Link
                  href="/"
                  className="block hover:underline transition-colors"
                >
                  제목제목제목제목제목
                </Link>
              </td>
              <td className="px-3 py-3 text-center">
                <span className="rounded-sm bg-purple-100 text-purple-700 font-bold px-2 py-0.5 text-xs">
                  길드
                </span>
              </td>
              <td className="px-3 py-3 whitespace-nowrap text-gray-500 text-xs">
                2025년 11월 28일 15시 57분 13초
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      {/* 모바일 */}
      <div className="lg:hidden">
        <div className="flex flex-col gap-2">
          <div className="border border-gray-200 rounded-sm px-3 py-3 hover:bg-gray-100 transition-colors">
            <div className="flex items-center justify-between mb-4">
              <Link
                href="/"
                className="block hover:underline transition-colors text-sm"
              >
                제목제목제목제목제목
              </Link>
              <span className="rounded-sm bg-blue-100 text-blue-700 font-bold px-2 py-0.5 text-xs">
                런너
              </span>
            </div>
            <p className="text-gray-500 text-xs">
              2025년 11월 28일 15시 57분 13초
            </p>
          </div>
          <div className="border border-gray-200 rounded-sm px-3 py-3 hover:bg-gray-100 transition-colors">
            <div className="flex items-center justify-between mb-4">
              <Link
                href="/"
                className="block hover:underline transition-colors text-sm"
              >
                제목제목제목제목제목
              </Link>
              <span className="rounded-sm bg-purple-100 text-purple-700 font-bold px-2 py-0.5 text-xs">
                길드
              </span>
            </div>
            <p className="text-gray-500 text-xs">
              2025년 11월 28일 15시 57분 13초
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
