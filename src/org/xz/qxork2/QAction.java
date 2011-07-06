/**
 * ClassName:QAction.java
 * Authoer:ningcl
 * Date:2011-1-11 
 */
package org.xz.qxork2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xz.qstruts.dispatcher.Dispatcher;
import org.xz.qstruts.impl.QStrutsActionInvocation;
import org.xz.qxork2.validator.EmptyValidator;
import org.xz.qxork2.validator.Utils;
import org.xz.qxork2.validator.ValidatorBean;
import org.xz.qxork2.validator.ValidatorInterface;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.ValidationAwareSupport;

/**
 * @author ningcl
 * @version 1.0 
 */
public class QAction implements ActionContextProvider,StrutsStatics,QActionCache{
	
	private static final String STRUTS_VALUESTACK_KEY = "struts.valueStack";	
	private static final String ACTION_MAPPING = "struts.actionMapping";	
	private static final String STRUTS_VALIDATION_KEY = "com.opensymphony.xwork2.ValidationAware";
	private static final EmptyValidator emptyValidator = new EmptyValidator();
	
	private ThreadLocal<ActionContext> threadLocal = new ThreadLocal<ActionContext>();
	
	/* (non-Javadoc)
	 * @see org.xz.qxork2.ActionContextProvider#setActionContext(org.xz.qxork2.ActionContext)
	 */
	public void setActionContext(ActionContext c) {
		// TODO Auto-generated method stub
		threadLocal.set(c);
	}
	
	private ActionContext getActionContext(){
		return threadLocal.get();
	}
	
	public String getHttpParameter(String param){
		HttpServletRequest req = getRequest();
		if(req!=null){
			return req.getParameter(param);
		}
		return null;
	}
	
	public String[] getHttpParameters(String param){
		
		HttpServletRequest req = getRequest();
		if(req!=null){
			return req.getParameterValues(param);
		}
		return null;
	}
	
	public Object getHttpAttribute(String key){
		HttpServletRequest req = getRequest();
		if(req!=null){
			return req.getAttribute(key);
		}
		return null;
	}
	
	public boolean setHttpAttribute(String key,Object obj){
		HttpServletRequest req = getRequest();
		if(req!=null){
			req.setAttribute(key, obj);
			return true;
		}
		return false;
	}
	
	public ServletContext getServletContext(){
		return (ServletContext)(getActionContext().get(SERVLET_CONTEXT));
	}
	
	public String getServletContextName(){
		return getServletContext().getServletContextName();
	}
	
	/**
     * Gets the HTTP servlet request object.
     *
     * @return the HTTP servlet request object.
     */
    public  HttpServletRequest getRequest() {
        return (HttpServletRequest)( getActionContext().get(HTTP_REQUEST));
    }
    
    public  HttpServletResponse getResponse() {
        return (HttpServletResponse) (getActionContext().get(HTTP_RESPONSE));
    }
	
	public void setValidationAware(ValidationAware vAware){
		getActionContext().put(STRUTS_VALIDATION_KEY, vAware);
	}
	
	public  ValidationAware getValidationAware(){
		ValidationAware v = (ValidationAware)(getActionContext().get(STRUTS_VALIDATION_KEY));
		if(v==null){
			v = new ValidationAwareSupport();
			setValidationAware(v);
		}
		return v;
	}
	
	public Object getAppObject(String key){
		Map map = (Map)getHttpAttribute(ActionContext.APPOBJECT+getActionContext().getModuleName());
		if(map!=null){
			return map.get(key);
		}
		return null;
	}
	
	public void setAppObject(String key,Object o){
		Map map = (Map)getHttpAttribute(ActionContext.APPOBJECT+getActionContext().getModuleName());
		if(map==null){
			map = new HashMap();
			setHttpAttribute(ActionContext.APPOBJECT+getActionContext().getModuleName(), map);
		}
		map.put(key, o);
	}
	
	public long getMallId(){
		return (Long)getHttpAttribute("mall_id");
	}
	
	public long getParameterLong(String key){
		String temp = getHttpParameter(key);
		if(temp==null){
			return 0;
		}
		try{
			return Long.parseLong(temp);
		}catch(Exception e){
			
		}
		return 0;
	}
	
	public int getParameterInt(String key){
		String temp = getHttpParameter(key);
		if(temp==null){
			return 0;
		}
		try{
			return Integer.parseInt(temp);
		}catch(Exception e){
			
		}
		return 0;
	}
	
	/**
	 * 是否开启进销存系统 
	 * return:
	 * false--不开启（使用商品订单管理）
	 * true--开启
	 */
	public  boolean isMallErp() {
		int mall_erp = getParameterInt("mall_erp");
		return  (mall_erp == 1);
	}
	
	public boolean getParameterBoolean(String key){
		String temp = getHttpParameter(key);
		if(temp==null){
			return false;
		}
		try{
			if(temp.equals("1")){
				temp = "true";
			}else if(temp.equals("0")){
				temp = "false";
			}
			return Boolean.parseBoolean(temp);
		}catch(Exception e){
			
		}
		return false;
	}
	
	public float getParameterFloat(String key){
		String temp = getHttpParameter(key);
		if(temp==null){
			return 0;
		}
		try{
			return Float.parseFloat(temp);
		}catch(Exception e){
			
		}
		return 0;
	}
	
	public double getParameterDouble(String key){
		String temp = getHttpParameter(key);
		if(temp==null){
			return 0;
		}
		try{
			return Double.parseDouble(temp);
		}catch(Exception e){
			
		}
		return 0;
	}
	
	public Object getReponseContent(ModuleResponse response) throws IOException{
		String encoding = response.getCharacterEncoding();
		if(encoding==null){
			encoding = Dispatcher.getInstance().getEncoding();
		}
		return new String(response.getContent().toByteArray(),encoding);
	}
	
	public ActionProxy getActionProxy(){
		return ((QStrutsActionInvocation)getActionContext().get(ActionContext.ACTION_INVOCATION)).getProxy();
	}
	
	public void serviceAction(String module) throws ServletException, IOException{
		String[] splits = null;
		if(module!=null && (splits=module.split("\\.")).length==3){
			ModuleResponse r = serviceAction(Utils.makeActionMapping(splits, null));
			setHttpAttribute(module, getReponseContent(r));
		}
	}
	
	public ModuleResponse serviceAction(ActionMapping mapping) throws ServletException{
		ModuleRequest request = new ModuleRequest(getRequest());
		ModuleResponse response = new ModuleResponse(getResponse());
		Dispatcher.getInstance().serviceAction(request, response, getServletContext(), mapping);
		return response;
	}
	
	public  Object getParameter(String parameter,String type,String message){
		String param = getHttpParameter(parameter);
		if(param==null){
			return null;
		}
		try{
			if(String.class.getCanonicalName().equals(type)){
				return param;
			}else if(Integer.class.getCanonicalName().equals(type)){
				return Integer.parseInt(param);
			}else if(Long.class.getCanonicalName().equals(type)){
				return Long.parseLong(param);
			}else if(Short.class.getCanonicalName().equals(type)){
				return Short.parseShort(param);
			}else if(Float.class.getCanonicalName().equals(type)){
				return Float.parseFloat(param);
			}else if(Double.class.getCanonicalName().equals(type)){
				return Double.parseDouble(param);
			}else if(Boolean.class.getCanonicalName().equals(type)){
				int v = Integer.parseInt(param);
				if(v==0){
					return false;
				}else{
					return true;
				}
			}else if(Date.class.getCanonicalName().equals(type)){
				return new SimpleDateFormat("yyyy.MM.dd").parse(param);
			}else if(String[].class.getCanonicalName().equals(type)){
				String[] d = getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(d[i]);
					}
				}
				return d;
			}else if(Integer[].class.getCanonicalName().equals(type)){
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(Integer.parseInt(d[i]));
					}
					return x;
				}
				return d;
			}else if(Long[].class.getCanonicalName().equals(type)){
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(Long.parseLong(d[i]));
					}
					return x;
				}
				return d;
			}else if(Short[].class.getCanonicalName().equals(type)){
				
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(Short.parseShort(d[i]));
					}
					return x;
				}
				return d;
				
			}else if(Float[].class.getCanonicalName().equals(type)){
				
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(Float.parseFloat(d[i]));
					}
					return x;
				}
				return d;
				
				
			}else if(Double[].class.getCanonicalName().equals(type)){

				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(Double.parseDouble(d[i]));
					}
					return x;
				}
				return d;
				
			}else if(Boolean[].class.getCanonicalName().equals(type)){
				
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						int v = Integer.parseInt(d[i]);
						x.add(v==0?false:true);
					}
					return x;
				}
				return d;
				
			}else if(Date[].class.getCanonicalName().equals(type)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
				String[] d =  getRequest().getParameterValues(param);
				if(d!=null){
					ArrayList x = new ArrayList(d.length);
					for(int i=0;i<d.length;i++){
						x.add(sdf.parse(d[i]));
					}
					return x;
				}
				return d;
				
			}
		}catch(NumberFormatException ex){
			
		}catch(Exception e){
			getValidationAware().addFieldError(parameter, message);
		}
		return null;
	}
	
	
	public void executeValidator(Object bean,Object value,String parameter,String message,String expression,String type){
		ValidatorBean validator = new ValidatorBean();
		validator.setBean(bean);
		validator.setValue(value);
		validator.setExpression(expression);
		validator.setMessage(message);
		boolean result = false;
		if(type.equals("empty")){
			result = emptyValidator.execute(validator);
		}else if(type.startsWith("spring:")){
			type = type.substring(7);
			ValidatorInterface v = (ValidatorInterface)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(type);
			result = v.execute(validator);
		}
		if(!result){
			getValidationAware().addFieldError(parameter, message);
		}
	}

	

	/* (non-Javadoc)
	 * @see org.xz.qxork2.QActionCache#setCache(com.opensymphony.xwork2.ActionInvocation, java.lang.String)
	 */
	public void setCache(ActionInvocation invocation, String result) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.xz.qxork2.QActionCache#getCache(com.opensymphony.xwork2.ActionInvocation)
	 */
	public Result getCache(ActionInvocation invocation) {
		// TODO Auto-generated method stub
		return null;
	}
}
