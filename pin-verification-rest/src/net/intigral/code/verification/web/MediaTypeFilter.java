package net.intigral.code.verification.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MediaTypeFilter implements Filter {

	@Override
	public void destroy() {
		// ...
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//
	}

	@Override
	public void doFilter(ServletRequest request,
               ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
    	System.out.println("++++++++++++ Inside Filter +++++++++++");

		try {
			 String url = ((HttpServletRequest)request).getRequestURL().toString();
			 HttpServletRequest httpServletRequest = (HttpServletRequest)request;
	         //create the FakeHeadersRequest object to wrap the HttpServletRequest
			 MediaTypeRequestWrapper request1 = null;
			 if(url.endsWith("csv")){
			    	System.out.println("++++++++++++ Inside Filter +++++++++++CSV FOUND");

				 request1 = new MediaTypeRequestWrapper(httpServletRequest,"*/*");
			 }
			 else
				 request1 = new MediaTypeRequestWrapper(httpServletRequest,url.substring(url.lastIndexOf('.')+1));
	         //continue on in the filter chain with the FakeHeaderRequest and ServletResponse objects
	         chain.doFilter(request1, response);

		} catch (Exception ex) {
			ex.printStackTrace();
//			request.setAttribute("errorMessage", ex);
//			request.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp")
//                               .forward(request, response);
		}

	}

}