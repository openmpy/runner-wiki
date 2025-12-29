import DocumentTitle from "@/components/document/DocumentTitle";
import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div>
      <DocumentTitle text="대문" />

      <div className="flex flex-col gap-6">
        <div>
          <Link
            href="https://tr.rhaon.co.kr/home"
            target="_blank"
            className="mb-2 inline-block"
          >
            <Image
              src="/images/banner.jpg"
              alt="테일즈런너 배너"
              width={1002}
              height={400}
              priority
            />
          </Link>
        </div>

        <div className="flex flex-col gap-2">
          <h2 className="text-lg lg:text-xl font-bmhanna">런너위키란?</h2>
          <p className="text-sm leading-relaxed">
            누구나 익명으로 문서를 작성하고 편집할 수 있으며 유저, 길드에 대한
            정보를 자유롭게 기록할 수 있습니다.
          </p>
        </div>

        <div className="flex flex-col gap-2">
          <h2 className="text-lg lg:text-xl font-bmhanna">안내</h2>
          <p className="text-sm leading-relaxed">
            문서 작업 시 <strong>IP가 기록</strong>되며 이는 스팸, 악성
            이용, 시스템 악용 방지를 위한 용도로만 사용됩니다.
            <br />
            해당 정보는 그 외의 목적으로 절대 활용되지 않습니다.
          </p>
        </div>

        {/* 문의 */}
        <div className="flex flex-col gap-2">
          <h2 className="text-lg lg:text-xl font-bmhanna">문의</h2>
          <p className="text-sm leading-relaxed">
            서비스 관련 문의, 오류 제보, 문서 삭제 요청 등은{" "}
            <Link href="mailto:runnerwiki@proton.me" className="text-mint">
              #이메일
            </Link>
            을 통해 연락해 주시기 바랍니다.
          </p>
        </div>
      </div>
    </div>
  );
}
