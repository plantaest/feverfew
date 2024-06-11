export interface ListResponse<T> {
  pageIndex: number;
  itemsPerPage: number;
  totalItems: number;
  totalPages: number;
  currentItemCount: number;
  items: T[];
}
