/**
 * ClassName:ComplexResult.java
 * Authoer:ningcl
 * Date:2011-1-6 
 */
package org.xz.qstruts.dispatcher;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.xz.qxork2.ActionContext;
import org.xz.qxork2.ModuleRequest;
import org.xz.qxork2.ModuleResponse;


import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ComplexResult implements Result{
	
	private String location;
	private Container container;
	private ActionMapping mapping;
		
	public ComplexResult(String location){
		this.location = location;
		
	}
	
	public ComplexResult(ActionMapping mapping){
		this.mapping = mapping;
	}
	
	@Inject
    public void setContainer(Container cont) {
        this.container = cont;
    }
	
	
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Result#execute(com.opensymphony.xwork2.ActionInvocation)
	 */
	public void execute(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		if(mapping!=null){
			ModuleRequest request = new ModuleRequest(ActionContext.getHttpRequest(invocation.getInvocationContext().getContextMap()));
			HttpServletResponse parent = ActionContext.getHttpResponse(invocation.getInvocationContext().getContextMap());
			ModuleResponse response = new ModuleResponse(parent);
			Dispatcher.getInstance().serviceAction(request, response,
					ActionContext.getServletContext(invocation.getInvocationContext().getContextMap()), mapping);
			if(!parent.isCommitted() && response.getStatus()!=0){
					if(response.getErrorMessage()!=null){
						parent.setStatus(response.getStatus(), response.getErrorMessage());
					}else{
						parent.setStatus(response.getStatus());
					}
			}
			
			if(!parent.isCommitted() && response.getRedirectedUrl()!=null){
				parent.sendRedirect(response.getRedirectedUrl());
			}
			
			if(!parent.isCommitted()){
				if(StringUtils.isEmpty(response.getContentType())){
					parent.setContentType(response.getContentType());
				}
				if(StringUtils.isEmpty(response.getCharacterEncoding())){
					parent.setCharacterEncoding(response.getCharacterEncoding());
				}
				if(response.getContent().getSize()>0){
					parent.getOutputStream().write(response.getContent().toByteArray());
					parent.setContentLength(response.getContent().getSize());
				}
			}
		}else{
			if(!location.startsWith("/")){
				String pkg = (String)invocation.getInvocationContext().get(ActionContext.MODULENAME);
				String groupid = invocation.getProxy().getConfig().getPackageName();
				int inx = -1;
				if((inx=groupid.indexOf("-"))>-1){
					groupid=groupid.substring(0,inx);
				}
				location = "/modules/"+groupid+"/"+pkg+"/"+invocation.getProxy().getActionName()+"/"+location;
			}
			
			if(location.endsWith(".jsp") || location.endsWith(".mjsp")){
				new ServletDispatcherResult(location).execute(invocation);
			}else if(location.endsWith(".vm") || location.endsWith(".mvm")){
				VelocityResult r = new VelocityResult(location);
				container.inject(r);
				r.execute(invocation);
			}
		}
		
	}

}
