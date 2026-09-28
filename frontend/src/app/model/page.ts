export interface Page<T> {
  items: T[];
  count: number;
  pageSize: number;
  pageCount: number;
}
