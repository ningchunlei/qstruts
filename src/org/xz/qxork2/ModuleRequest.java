/**
 * ClassName:ModuleRequest.java
 * Authoer:ningcl
 * Date:2011-1-14
 */
package org.xz.qxork2;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author ningcl
 * @version 1.0
 */
public class ModuleRequest extends HttpServletRequestWrapper{

	private HttpServletRequest request;
	private HashMap map = new HashMap();

	public ModuleRequest(HttpServletRequest request){
		super(request);
		this.request = request;
	}

	public String getParameter(String name) {
		Map parameters = (Map)map.get(ActionContext.REQUESTPARAMETERCONTEXT);
		String r = null;
		if(parameters!=null){
			r = (String)parameters.get(name);
		}
		if(r==null){
			return super.getParameter(name);
		}
		return r;
    }

	public Object getAttribute(String name)
    {
		Object x = map.get(name);
        if(x==null && name!=null){
        	if(name.startsWith("javax.servlet") || name.startsWith("org.apache.catalina")
        			|| name.startsWith(ActionContext.APPOBJECT) || name.equals("theme")
        			|| name.equals("cdncss") || name.equals("cdnimg") ||  name.equals("sys_adminpath")
        			|| name.equals("mall_id") || name.equals("mall_domain") || name.equals("mall_erp") || name.equals("is_admin") || name.equals("admin_id") || name.equals("admin_nick")){
        		return request.getAttribute(name);
        	}
        }

        return x;
    }

    public Enumeration getAttributeNames()
    {
        return Collections.enumeration(map.keySet());
    }

    public void setAttribute(String name, Object o)
    {
        if(name.startsWith(ActionContext.APPOBJECT)){
        	request.setAttribute(name, o);
        	return;
        }
    	map.put(name, o);
    }

    public void removeAttribute(String name)
    {
        map.remove(name);
    }
}
