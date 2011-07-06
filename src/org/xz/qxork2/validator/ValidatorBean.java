/**
 * ClassName:ValidatorBean.java
 * Authoer:ningcl
 * Date:2011-2-24 
 */
package org.xz.qxork2.validator;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ValidatorBean {
	
	private String message;
	private String expression;
	private Object value;
	private Object bean;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	
	
}
