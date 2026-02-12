export interface Page<T> {
  payload: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

/** 커서 기반 페이지네이션 응답 (검색 등) */
export interface CursorPage<T> {
  payload: T[];
  nextId: number;
  hasNext: boolean;
}
