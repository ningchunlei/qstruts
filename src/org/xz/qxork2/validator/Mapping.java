/**
 * ClassName:Mapping.java
 * Authoer:ningcl
 * Date:2011-2-21 
 */
package org.xz.qxork2.validator;

import java.util.List;

/**
 * @author ningcl
 * @version 1.0 
 */
public class Mapping {
	
	private String parameter = "";
	private String bean;
	private String type="";
	private String message = "";
	private List<Validator> validators;
	

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}
	
}
