export interface Store {
    id: string;
    name: string;
}

export interface PagedModel<T> {
    content: T[];
    page: {
        size: number;
        number: number;
        totalElements: number;
        totalPages: number;
    };
}
