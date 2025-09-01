package org.fileservice.interceptor;

import org.apache.struts2.ActionInvocation;
import org.apache.struts2.interceptor.AbstractInterceptor;

public class TimingInterceptor extends AbstractInterceptor{

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        String actionName = invocation.getProxy().getActionName();
        

        long startTime = System.currentTimeMillis();


        System.out.println("TimingInterceptor: Starting execution of action '" + actionName + "'");
	String result=null;
	try{

        	result = invocation.invoke();
	}catch(Exception e){

		System.err.println("TimingInterceptor: Exception caught during invocation of action '" + actionName + "': " + e.getMessage());
            	e.printStackTrace(); 
		throw e;
	}finally {
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("TimingInterceptor: Action '" + actionName + "' executed in " + totalTime + " ms. Result was: " + result);
        }




        return result;
    }
}
