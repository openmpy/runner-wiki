import Image from "next/image";
import Link from "next/link";

export default function HomePage() {
  return (
    <div>
      <h1 className="font-bm-hanna text-2xl mb-4">대문</h1>

      <div className="flex flex-col gap-4">
        <Link
          href="https://tr.rhaon.co.kr/home"
          target="_blank"
          className="mb-2"
        >
          <Image
            src="/images/home.jpg"
            alt="런너위키"
            width={1002}
            height={400}
          />
        </Link>

        <div className="flex flex-col">
          <h2 className="font-bm-hanna text-xl mb-2">런너위키란?</h2>
          <div className="flex flex-col gap-1">
            <p className="text-sm text-gray-500">
              누구나 자유롭게 기여할 수 있는 위키입니다.
            </p>
            <p className="text-sm text-gray-500">
              검증되지 않았거나 편향된 내용이 있을 수 있습니다.
            </p>
            <p className="text-sm text-gray-500">
              공익성과 학습 목적으로 제작되었으며, 어떠한 수익성도 추구하지
              않습니다.
            </p>
          </div>
        </div>
        <div className="flex flex-col">
          <h2 className="font-bm-hanna text-xl mb-2">주의점</h2>
          <div className="flex flex-col gap-1">
            <p className="text-sm text-gray-500">
              부적절한 내용을 작성할 경우 삭제될 수 있습니다.
            </p>
            <p className="text-sm text-gray-500">
              문서 작업 시 IP가 기록되며, 악의적 이용 방지를 위해서만
              사용됩니다.
            </p>
          </div>
        </div>
        <div className="flex flex-col">
          <h2 className="font-bm-hanna text-xl mb-2">문의 방법</h2>
          <div className="flex flex-col gap-1">
            <p className="text-sm text-gray-500">
              문의 사항은 런너위키 디스코드 서버의{" "}
              <span className="text-mint font-bold">#문의</span> 채널로
              문의해주세요.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
