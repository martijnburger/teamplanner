export interface IItem<T> {
    items: Array<T>;
    count: number;
    pageSize: number;
    pageCount: number;
}