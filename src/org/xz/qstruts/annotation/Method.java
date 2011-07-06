/**
 * ClassName:Method.java
 * Authoer:ningcl
 * Date:2010-12-21 
 */
package org.xz.qstruts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ningcl
 * @version 1.0 
 */
@Retention(RetentionPolicy.RUNTIME)    
@Target(ElementType.METHOD)
public @interface Method {
	String value() default "";
}
