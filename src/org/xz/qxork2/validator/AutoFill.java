/**
 * ClassName:AutoFill.java
 * Authoer:ningcl
 * Date:2011-2-21 
 */
package org.xz.qxork2.validator;

import java.util.HashMap;
import java.util.List;

/**
 * @author ningcl
 * @version 1.0 
 */
public class AutoFill {
	
	private List<Mapping> mapping;
	private String method = "";
	
	public List<Mapping> getMapping() {
		return mapping;
	}

	public void setMapping(List<Mapping> mapping) {
		this.mapping = mapping;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
