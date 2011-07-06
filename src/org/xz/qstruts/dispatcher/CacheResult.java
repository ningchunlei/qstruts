/**
 * ClassName:CacheResult.java
 * Authoer:ningcl
 * Date:2011-3-6 
 */
package org.xz.qstruts.dispatcher;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Inject;

/**
 * @author ningcl
 * @version 1.0 
 */
public class CacheResult implements Result{
	
	private String result = "";
	  private String defaultEncoding;
	
	@Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        defaultEncoding = val;
    }
	
	public CacheResult(String r){
		result = r;
	}
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Result#execute(com.opensymphony.xwork2.ActionInvocation)
	 */
	public void execute(ActionInvocation actioninvocation) throws Exception {
		// TODO Auto-generated method stub
		String encoding = getEncoding();
        String contentType = getContentType();
        if (encoding != null) {
            contentType = contentType + ";charset=" + encoding;
        }
        HttpServletResponse response = (HttpServletResponse)actioninvocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        response.setContentType(contentType);
        response.getWriter().write(result);
	}
	
	protected String getContentType() {
		return "text/html";
	}
	
	protected String getEncoding() {
        String encoding = defaultEncoding;
        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return encoding;
	}
}
