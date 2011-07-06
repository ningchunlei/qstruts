/**
 * ClassName:EmailValidator.java
 * Authoer:ningcl
 * Date:2011-2-24 
 */
package org.xz.qxork2.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author ningcl
 * @version 1.0 
 */
public class EmptyValidator implements ValidatorInterface{

	/* (non-Javadoc)
	 * @see org.xz.qxork2.validator.ValidatorInterface#execute(java.lang.String, java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public boolean execute(ValidatorBean validator) {
		// TODO Auto-generated method stub
		Object v = validator.getValue();
		if(v==null){
			return false;
		}
		if(v instanceof String){
			if(StringUtils.isEmpty((String)v)){
				return false;
			}
		}else if(v instanceof Integer){
			return ((Integer)v)>0?true:false;
		}else if(v instanceof Float){
			return ((Float)v)>0?true:false;
		}else if(v instanceof Long){
			return ((Long)v)>0?true:false;
		}else if(v instanceof Double){
			return ((Double)v)>0?true:false;
		}else if(v instanceof Short){
			return ((Short)v)>0?true:false;
		}else if(v instanceof List){
			return (v==null||((List)v).size()==0)?false:true;
		}
		return true;
	}

}
