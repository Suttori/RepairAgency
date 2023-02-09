package com.suttori.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(EncodingFilter.class);
    private static final String ENCODING_UTF_8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info( "The filter: {} has begun its work");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(ENCODING_UTF_8);
        }

        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding(ENCODING_UTF_8);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info( "The filter: {} has finished its work");
    }
}