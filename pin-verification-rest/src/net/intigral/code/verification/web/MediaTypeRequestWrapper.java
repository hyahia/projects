package net.intigral.code.verification.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
 
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
 
public class MediaTypeRequestWrapper extends HttpServletRequestWrapper {
 
    /**
     * Constructor. 
     * 
     * @param request HttpServletRequest.
     */
	private String mediaTypeValue = "application/json";
    public MediaTypeRequestWrapper(HttpServletRequest request, String mediaTypeValue) {
        super(request);
        this.mediaTypeValue = mediaTypeValue;
    }
     
    public String getHeader(String name) {
    	System.out.println("++++++++++++ Inside Wrapper +++++++++++");
        //get the request object and cast it
        HttpServletRequest request = (HttpServletRequest)getRequest();
         
        //if we are looking for the "Accept" request header
        if("Accept".equals(name)) {
            return mediaTypeValue;
        }
         
        //otherwise fall through to wrapped request object
        return request.getHeader(name);
    }
     
    public Enumeration getHeaderNames() {
        //create an enumeration of the request headers
        //additionally, add the "Accept" request header
         
        //create a list
        List list = new ArrayList();
         
        //loop over request headers from wrapped request object
        HttpServletRequest request = (HttpServletRequest)getRequest();
        Enumeration e = request.getHeaderNames();
        while(e.hasMoreElements()) {
            //add the names of the request headers into the list
            String n = (String)e.nextElement();
            list.add(n);
        }
         
        //additionally, add the "Accept" to the list of request header names
        list.add("Accept");
         
        //create an enumeration from the list and return
        Enumeration en = Collections.enumeration(list);
        return en;
    }
 
}