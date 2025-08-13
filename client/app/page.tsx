import { Metadata } from "next";
import Image from "next/image";
import Link from "next/link";

export const metadata: Metadata = {
  title: "대문 - 런너위키",
  description: "누구나 자유롭게 기여할 수 있는 위키입니다.",
  openGraph: {
    title: "런너위키",
    description: "누구나 자유롭게 기여할 수 있는 위키입니다.",
    images: [
      {
        url: "/images/text.png",
        width: 1200,
        height: 630,
        alt: "런너위키",
      },
    ],
    type: "website",
    locale: "ko_KR",
  },
  twitter: {
    card: "summary_large_image",
    title: "런너위키",
    description: "누구나 자유롭게 기여할 수 있는 위키입니다.",
    images: ["/images/text.png"],
  },
};

export default function HomePage() {
  return (
    <div>
      <h1 className="font-bm-hanna text-2xl mb-4">대문</h1>

      <div className="flex flex-col gap-6">
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
            priority={true}
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
          <h2 className="font-bm-hanna text-xl mb-2">문의</h2>
          <div className="flex flex-col gap-1">
            <p className="text-sm text-gray-500">
              문의는{" "}
              <Link
                href="mailto:runnerwiki@proton.me"
                className="text-mint hover:underline"
              >
                #이메일
              </Link>
              을 통해 해주시길 바랍니다.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
