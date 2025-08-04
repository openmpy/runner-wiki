import { bmhanna } from "./layout";

export default function Home() {
  return (
    <div>
      <h1 className={`text-2xl mb-4 ${bmhanna.className}`}>대문</h1>
      <div className="space-y-4">
        <p className="text-gray-700">
          여기에 메인 콘텐츠가 들어갑니다. 이 영역은 전체 레이아웃의 80%를
          차지합니다.
        </p>
        <div className="bg-gray-100 p-4 rounded-lg">
          <h2 className="text-lg font-semibold mb-2">콘텐츠 섹션</h2>
          <p className="text-gray-600">
            다양한 콘텐츠를 이곳에 배치할 수 있습니다.
          </p>
        </div>
      </div>
    </div>
  );
}
