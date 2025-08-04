import { MdLocalFireDepartment } from "react-icons/md";
import { pretendard } from "../../layout";

export default function Sidebar() {
  const popularDocuments = [
    {
      id: 1,
      title: "초보자 가이드",
      category: "런너",
      date: "2024-01-15",
    },
    {
      id: 2,
      title: "길드 시스템 완전 정복",
      category: "길드",
      date: "2024-01-12",
    },
    {
      id: 3,
      title: "고급 런너 전략",
      category: "런너",
      date: "2024-01-10",
    },
    {
      id: 4,
      title: "길드 운영 가이드",
      category: "길드",
      date: "2024-01-08",
    },
    {
      id: 5,
      title: "런너 스킬 트리",
      category: "런너",
      date: "2024-01-05",
    },
  ];

  return (
    <div className="border-t border-b md:border border-[#00A495] px-4 sm:px-8 md:px-6 py-4 h-fit rounded-none md:rounded-lg w-full md:min-w-[300px] md:w-[300px]">
      <h2 className="text-lg font-semibold mb-4 text-white bg-[#00A495] p-3 rounded-none md:rounded-t flex items-center gap-2 -mx-4 sm:-mx-8 md:-mx-6 -mt-4 px-4 sm:px-8 md:px-6 pt-4">
        <MdLocalFireDepartment className="text-white" />
        인기 문서
      </h2>
      <div className={`bg-white ${pretendard.className}`}>
        <table className="w-full">
          <tbody>
            {popularDocuments.map((doc) => (
              <tr
                key={doc.id}
                className="cursor-pointer transition-colors group"
              >
                <td className="py-1 text-sm text-black group-hover:underline font-bold">
                  {doc.title}
                </td>
                <td className="py-1 text-xs text-gray-500 text-right">
                  {doc.date}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
