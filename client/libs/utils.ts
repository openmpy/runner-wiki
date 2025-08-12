export function formatRelativeTime(dateString: string): string {
  // 한국 시간대(KST, UTC+9) 기준으로 변환
  const date = new Date(dateString);
  const koreanDate = new Date(date.getTime() + 9 * 60 * 60 * 1000);

  const now = new Date();
  const koreanNow = new Date(now.getTime() + 9 * 60 * 60 * 1000);

  const diffInSeconds = Math.floor(
    (koreanNow.getTime() - koreanDate.getTime()) / 1000
  );

  // 10초 미만
  if (diffInSeconds < 10) {
    return "방금 전";
  }

  // 1분 미만 (60초)
  if (diffInSeconds < 60) {
    return `${diffInSeconds}초 전`;
  }

  if (diffInSeconds < 3600) {
    const minutes = Math.floor(diffInSeconds / 60);
    return `${minutes}분 전`;
  }

  // 24시간 미만 (86400초)
  if (diffInSeconds < 86400) {
    const hours = Math.floor(diffInSeconds / 3600);
    return `${hours}시간 전`;
  }

  // 24시간 이상이면 년월일 시분초 형식으로 표시 (한국 시간 기준)
  const year = koreanDate.getUTCFullYear();
  const month = String(koreanDate.getUTCMonth() + 1);
  const day = String(koreanDate.getUTCDate());
  const hours = String(koreanDate.getUTCHours());
  const minutes = String(koreanDate.getUTCMinutes());
  const seconds = String(koreanDate.getUTCSeconds());

  return `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분 ${seconds}초`;
}

export function formatRelativeTime2(dateString: string): string {
  // 한국 시간대(KST, UTC+9) 기준으로 변환
  const date = new Date(dateString);
  const koreanDate = new Date(date.getTime() + 9 * 60 * 60 * 1000);

  const now = new Date();
  const koreanNow = new Date(now.getTime() + 9 * 60 * 60 * 1000);

  const diffInSeconds = Math.floor(
    (koreanNow.getTime() - koreanDate.getTime()) / 1000
  );

  // 10초 미만
  if (diffInSeconds < 10) {
    return "방금 전";
  }

  // 1분 미만 (60초)
  if (diffInSeconds < 60) {
    return `${diffInSeconds}초 전`;
  }

  if (diffInSeconds < 3600) {
    const minutes = Math.floor(diffInSeconds / 60);
    return `${minutes}분 전`;
  }

  // 24시간 미만 (86400초)
  if (diffInSeconds < 86400) {
    const hours = Math.floor(diffInSeconds / 3600);
    return `${hours}시간 전`;
  }

  // 24시간 이상이면 년월일 형식으로 표시 (한국 시간 기준)
  const year = koreanDate.getUTCFullYear();
  const month = String(koreanDate.getUTCMonth() + 1);
  const day = String(koreanDate.getUTCDate());

  return `${year}년 ${month}월 ${day}일`;
}

export function formatKoreanDate(dateString: string): string {
  return new Date(dateString).toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}
