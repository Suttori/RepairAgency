package com.suttori.service;

import com.suttori.entity.Pagination;
import com.suttori.entity.Sort;
import com.suttori.entity.User;

import javax.servlet.http.HttpServletRequest;

public class PaginationService {
    Sort sort = new Sort();
    Pagination pagination = new Pagination();

    public Sort setSortParams(HttpServletRequest req) {
        sort.setUser((User) req.getSession().getAttribute("user"));
        sort.setMasterId(req.getParameter("master"));
        sort.setStatus(req.getParameter("status"));
        sort.setSort(req.getParameter("sort"));
        sort.setEmail(req.getParameter("email"));
        return sort;
    }

    public Pagination getLimitOffsetPage(HttpServletRequest req) {
        pagination.setPage(1);
        if (req.getParameter("page") != null) {
            pagination.setPage(Integer.parseInt(req.getParameter("page")));
        }
        pagination.setOffset(pagination.getPage() * pagination.getOrdersOnPage() - pagination.getOrdersOnPage());
        return pagination;
    }
}
