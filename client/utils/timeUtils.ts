/**
 * 시간을 상대적으로 표시하는 유틸리티 함수들
 */

/**
 * 주어진 시간을 현재 시간과 비교하여 상대적 시간으로 표시
 * @param date - 비교할 날짜 (Date 객체 또는 ISO 문자열)
 * @returns 상대적 시간 문자열 (예: "3초 전", "5분 전", "2시간 전", "3일 전", "2024-01-15")
 */
export function getRelativeTime(date: Date | string): string {
  const now = new Date();
  const targetDate = typeof date === "string" ? new Date(date) : date;

  const diffInSeconds = Math.floor(
    (now.getTime() - targetDate.getTime()) / 1000
  );

  // 미래 시간인 경우
  if (diffInSeconds < 0) {
    return formatDate(targetDate);
  }

  // 1분 미만 (60초 미만)
  if (diffInSeconds < 60) {
    return `${diffInSeconds}초 전`;
  }

  // 1시간 미만 (60분 미만)
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`;
  }

  // 24시간 미만 (24시간 미만)
  const diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) {
    return `${diffInHours}시간 전`;
  }

  // 7일 미만
  const diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 7) {
    return `${diffInDays}일 전`;
  }

  // 7일 이상인 경우 날짜를 반환
  return formatDate(targetDate);
}

/**
 * 날짜를 포맷팅하는 함수
 * @param date - 포맷팅할 날짜
 * @returns 포맷팅된 날짜 문자열 (예: "2024-01-15")
 */
export function formatDate(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

/**
 * 시간을 포함한 상세 날짜 포맷팅
 * @param date - 포맷팅할 날짜
 * @returns 포맷팅된 날짜 시간 문자열 (예: "2024-01-15 14:30")
 */
export function formatDateTime(date: Date): string {
  const dateStr = formatDate(date);
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${dateStr} ${hours}:${minutes}`;
}

/**
 * 한국어로 상대적 시간을 표시하는 함수
 * @param date - 비교할 날짜
 * @returns 한국어 상대적 시간 문자열
 */
export function getRelativeTimeKorean(date: Date | string): string {
  const now = new Date();
  const targetDate = typeof date === "string" ? new Date(date) : date;

  const diffInSeconds = Math.floor(
    (now.getTime() - targetDate.getTime()) / 1000
  );

  // 미래 시간인 경우
  if (diffInSeconds < 0) {
    return formatDateKorean(targetDate);
  }

  // 1분 미만
  if (diffInSeconds < 60) {
    return `방금 전`;
  }

  // 1시간 미만
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`;
  }

  // 24시간 미만
  const diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) {
    return `${diffInHours}시간 전`;
  }

  // 7일 미만
  const diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 7) {
    return `${diffInDays}일 전`;
  }

  // 7일 이상
  return formatDateKorean(targetDate);
}

/**
 * 한국어 날짜 포맷팅
 * @param date - 포맷팅할 날짜
 * @returns 한국어 날짜 문자열 (예: "2024년 1월 15일")
 */
export function formatDateKorean(date: Date): string {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  return `${year}년 ${month}월 ${day}일`;
}
