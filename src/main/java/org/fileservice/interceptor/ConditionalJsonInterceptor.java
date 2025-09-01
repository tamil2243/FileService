package org.fileservice.interceptor;

import org.apache.struts2.ActionInvocation;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.AbstractInterceptor;
import org.apache.struts2.json.JSONInterceptor;

import jakarta.servlet.http.HttpServletRequest;

public class ConditionalJsonInterceptor extends AbstractInterceptor{
    
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        if ("PUT".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod())) {
            JSONInterceptor jsonInterceptor = new JSONInterceptor();
            return jsonInterceptor.intercept(invocation);
        } else {
            
            return invocation.invoke();
        }
    }
}
