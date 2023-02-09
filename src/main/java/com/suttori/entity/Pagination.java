package com.suttori.entity;

public class Pagination {
    int page;
    int offset;
    int ordersOnPage = 6;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOrdersOnPage() {
        return ordersOnPage;
    }
}
