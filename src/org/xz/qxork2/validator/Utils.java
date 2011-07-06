/**
 * ClassName:Utils.java
 * Authoer:ningcl
 * Date:2011-3-4 
 */
package org.xz.qxork2.validator;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.xz.qstruts.utils.StringHelp;

/**
 * @author ningcl
 * @version 1.0 
 */
public class Utils {
	
	
	public static String getBeanSetName(String src){
		String[] x = src.split("\\.");
		StringBuilder sb = new StringBuilder();		
		for(int i=0;i<x.length-1;i++){
			sb.append("get").append(StringHelp.firstLetterToUpperCase(x[i])).append("().");
		}
		sb.append("set").append(StringHelp.firstLetterToUpperCase(x[x.length-1]));
		return sb.toString();
	}
	
	public static String getBeanGetName(String src){
		String[] x = src.split("\\.");
		StringBuilder sb = new StringBuilder();		
		for(int i=0;i<x.length-1;i++){
			sb.append("get").append(StringHelp.firstLetterToUpperCase(x[i])).append("().");
		}
		sb.append("get").append(StringHelp.firstLetterToUpperCase(x[x.length-1]));
		return sb.toString();
	}
	
	public static String getBeanType(String type){
		if(String[].class.getCanonicalName().equals(type)
		|| Integer[].class.getCanonicalName().equals(type)
		|| Long[].class.getCanonicalName().equals(type)
		|| Short[].class.getCanonicalName().equals(type)
		|| Float[].class.getCanonicalName().equals(type)
		|| Double[].class.getCanonicalName().equals(type)
		|| Boolean[].class.getCanonicalName().equals(type)
		|| Date[].class.getCanonicalName().equals(type) ){
			return List.class.getCanonicalName();
		}
		
		if(Integer.class.getCanonicalName().equals(type)){
			return "int";
		}else if(Long.class.getCanonicalName().equals(type)){
			return "long";
		}else if(Short.class.getCanonicalName().equals(type)){
			return "short";
		}else if(Float.class.getCanonicalName().equals(type)){
			return "float";
		}else if(Double.class.getCanonicalName().equals(type)){
			return "double";
		}else if(Boolean.class.getCanonicalName().equals(type)){
			return "boolean";
		} 
		
		return type;
	}
	
	public static String getBeanObjectType(String type){
		if(String[].class.getCanonicalName().equals(type)
		|| Integer[].class.getCanonicalName().equals(type)
		|| Long[].class.getCanonicalName().equals(type)
		|| Short[].class.getCanonicalName().equals(type)
		|| Float[].class.getCanonicalName().equals(type)
		|| Double[].class.getCanonicalName().equals(type)
		|| Boolean[].class.getCanonicalName().equals(type)
		|| Date[].class.getCanonicalName().equals(type) ){
			return List.class.getCanonicalName();
		}
		
		return type;
	}
	
	public static void getMap(HttpServletRequest req,HashMap map){
		Enumeration e = req.getParameterNames();
		while(e.hasMoreElements()){
			String q = (String)e.nextElement();
			int i=-1;
			if((i=q.indexOf("["))>-1){
				String t = q.substring(0,i);
				ArrayList list = (ArrayList)map.get(t);
				if(list==null){
					list = new ArrayList();
					map.put(t, list);
				}
				list.add(q);
			}
		}
		Set keys = map.keySet();
		if(keys.size()==0){
			return;
		}
		for(Iterator it = map.values().iterator();it.hasNext();){
			ArrayList<String> list = (ArrayList<String>)it.next();
			Collections.sort(list);
		}
		
		return;
	}

	public static int getArrayIndex(String key){
		int st = key.indexOf("[");
		int en = key.indexOf("]");
		return Integer.parseInt(key.substring(st+1,en));
	}
	
	public static String getInnerBean(String so,String type){
		Object o = null;
		try {
			o = Class.forName(so).newInstance();
		}
		catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String types[] = type.split("\\.");
		Class c = o.getClass();
		Field f = null;
		for(int i=0;i<types.length-1;i++){
			try {
				f = c.getDeclaredField(types[i]);
				c = f.getType();
			}
			catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ParameterizedType s = (ParameterizedType)f.getGenericType();
		return s.getActualTypeArguments()[0].toString().replaceFirst("class ", "");
	}
	
	
	public static ActionMapping makeActionMapping(String[] splits,Map map){
		StringBuilder sb = new StringBuilder(20);
		for(int i=0;i<splits.length-2;i++){
			sb.append("/").append(splits[i]);
		}
		return new ActionMapping(splits[splits.length-2],sb.toString(),splits[splits.length-1],map);
	}
	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		list.add(1);
		list.add(1);

		list.add(1);
		list.set(5, 12);
		list.get(1);
	}
}
