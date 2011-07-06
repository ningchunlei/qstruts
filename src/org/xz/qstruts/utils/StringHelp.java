/**
 * ClassName:StringHelp.java
 * Authoer:ningcl
 * Date:2010-12-22 
 */
package org.xz.qstruts.utils;

import org.springframework.util.StringUtils;

/**
 * @author ningcl
 * @version 1.0 
 */
public class StringHelp extends StringUtils{
	
	public static String firstLetterToLowerCase(String src){
		src = src.substring(0,1).toLowerCase()+src.substring(1);
		return src;
	}
	
	public static String firstLetterToUpperCase(String src){
		src = src.substring(0,1).toUpperCase()+src.substring(1);
		return src;
	}
	
}
