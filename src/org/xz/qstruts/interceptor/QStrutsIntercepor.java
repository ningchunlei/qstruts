/**
 * ClassName:QStrutsIntercepor.java
 * Authoer:ningcl
 * Date:2011-1-26 
 */
package org.xz.qstruts.interceptor;

import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author ningcl
 * @version 1.0 
 */
public abstract class QStrutsIntercepor extends AbstractInterceptor{
	
	protected Container container;
	
	@Inject
    public void setContainer(Container cont) {
        this.container = cont;
    }
	
	public String intercept(ActionInvocation ai) throws Exception {
		InterceporResult result = execute(new ActionContext(ai.getInvocationContext().getContextMap()));
		if(result.isHasNext()){
			ai.invoke();
		}else if(result.getResult()!=null){
			container.inject(result.getResult());
			result.getResult().execute(ai);
		}
		return null;
	}
	
	public abstract InterceporResult execute(ActionContext ac);
	
}
