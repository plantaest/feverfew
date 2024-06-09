export interface ListResponse<T> {
  currentItemCount: number;
  itemsPerPage: number;
  totalItems: number;
  pageIndex: number;
  totalPages: number;
  items: T[];
}
