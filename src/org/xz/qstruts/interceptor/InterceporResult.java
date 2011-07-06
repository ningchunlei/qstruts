/**
 * ClassName:InterceporResult.java
 * Authoer:ningcl
 * Date:2011-1-26 
 */
package org.xz.qstruts.interceptor;

import com.opensymphony.xwork2.Result;

/**
 * @author ningcl
 * @version 1.0 
 */
public class InterceporResult {
	
	private Result result;
	private boolean hasNext;
	
	public InterceporResult(Result result,boolean hasNext){
		this.result = result;
		this.hasNext = hasNext;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
}
