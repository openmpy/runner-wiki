import { formatRelativeTime2 } from "@/libs/utils";
import Link from "next/link";

const Sidebar = () => {
  return (
    <aside
      className={`w-full lg:w-100 bg-white border-y border-mint lg:border p-5 lg:rounded-lg lg:border-mint lg:align-self-start lg:self-start`}
    >
      <div>
        <h1 className="font-bm-hanna text-2xl mb-4">인기 문서</h1>

        <div className="flex flex-col gap-4">
          <p className="flex items-center justify-between gap-2">
            <Link href="/wiki/1" className="truncate w-3/5 hover:underline">
              가나다라마바사아자차
            </Link>
            <span className="text-xs text-gray-500">
              {formatRelativeTime2(
                new Date("2025-08-10T10:00:00Z").toISOString()
              )}
            </span>
          </p>
          <p className="flex items-center justify-between gap-2">
            <Link href="/wiki/1" className="truncate w-3/5 hover:underline">
              가나다라마바사아자차
            </Link>
            <span className="text-xs text-gray-500">
              {formatRelativeTime2(
                new Date("2025-08-11T00:00:00Z").toISOString()
              )}
            </span>
          </p>
          <p className="flex items-center justify-between gap-2">
            <Link href="/wiki/1" className="truncate w-3/5 hover:underline">
              가나다라
            </Link>
            <span className="text-xs text-gray-500">
              {formatRelativeTime2(
                new Date("2025-08-10T10:00:00Z").toISOString()
              )}
            </span>
          </p>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
