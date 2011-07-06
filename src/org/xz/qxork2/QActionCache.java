/**
 * ClassName:QActionCache.java
 * Authoer:ningcl
 * Date:2011-3-6 
 */
package org.xz.qxork2;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * @author ningcl
 * @version 1.0 
 */
public interface QActionCache {
	
	public void setCache(ActionInvocation invocation,String result);
	public Result getCache(ActionInvocation invocation);
	
}
