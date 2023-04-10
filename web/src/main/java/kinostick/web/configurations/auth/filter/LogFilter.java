package kinostick.web.configurations.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
//public class LogFilter implements Filter {
//
//    private Logger logger = LoggerFactory.getLogger(LogFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        logger.info("Hello from: " + request.getLocalAddr());
//
//
////((HttpServletResponse) response).setStatus(403);
//       // chain.doFilter(request, response);
//    }
//
//}