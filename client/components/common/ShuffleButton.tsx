"use client";

import { getShuffleDocument } from "@/lib/api/document";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { FaRandom } from "react-icons/fa";

export default function ShuffleButton() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const handleShuffle = async () => {
    if (isLoading) return;

    setIsLoading(true);
    try {
      const data = await getShuffleDocument();
      if (data && data.documentId) {
        router.push(`/document/${data.documentId}`);
      }
    } catch (error) {
      alert("문서를 불러오는데 실패했습니다.");
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <button
      onClick={handleShuffle}
      disabled={isLoading}
      className="px-2.5 py-2.5 bg-white rounded-sm cursor-pointer hover:opacity-90 transition-opacity disabled:opacity-50 disabled:cursor-not-allowed"
    >
      <FaRandom className="text-mint" />
    </button>
  );
}
