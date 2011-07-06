/**
 * ClassName:QStrutsClassLoader.java
 * Authoer:ningcl
 * Date:2010-12-21 
 */
package org.xz.qstruts.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.xz.qstruts.annotation.Method;
import org.xz.qstruts.annotation.Namespace;
import org.xz.qstruts.config.RuntimeProvider;
import org.xz.qstruts.utils.StringHelp;
import org.xz.qxork2.validator.AutoFill;
import org.xz.qxork2.validator.Utils;
import org.xz.qxork2.validator.ValidatorFileParser;

import com.opensymphony.xwork2.util.ClassLoaderUtil;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * @author ningcl
 * @version 1.0 
 */
public class QStrutsClassLoader extends ClassLoader {
	
	private static ClassPool _pool;
	
	public QStrutsClassLoader(ClassLoader parent){
		super(parent);
		_pool = ClassPool.getDefault();
		_pool.insertClassPath(new ClassClassPath(this.getClass()));
	}
	
	public Class findClass(String name) throws ClassNotFoundException{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
	{
		 
		Class c = findLoadedClass(name);
		if(c!=null){
			return c; 
		}
		
		String[] packages = name.split("\\.");
		if(!name.startsWith("org.xz.qstruts") && packages.length>3){
			try{
				CtClass cc = _pool.get(name);
				if(cc!=null){
					String appid = "";
					for(int i=2;i<packages.length-2;i++){
						appid = appid + "/" + packages[i];
					}
					appid=appid.substring(1);
					boolean writeF = registerPackage(cc,appid,name,packages[1]);
					if(writeF){
						String resource = name.replace(".", "/") + ".class";
						resource = _pool.find(name).getFile().replace(resource, "");
						cc.writeFile(resource);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return super.loadClass(name, resolve);
	}
	
	public boolean registerPackage(CtClass cc,String appid,String name,String groupid) throws Exception{
		Object[] ans = cc.getAvailableAnnotations();
		int flag = 0;
		String service = null;
		Namespace namespace = null;
		for(int i=0;ans!=null && i<ans.length;i++){
			if(ans[i] instanceof Namespace){
				namespace = (Namespace)ans[i];
				flag++;
			}else if(ans[i] instanceof Service){
				service = ((Service)ans[i]).value();
				flag ++;
			}else if(ans[i] instanceof Controller){
				service = ((Controller)ans[i]).value();
				flag ++;
			}
		}
		int modifyCount = 0;
		if(flag==2){
			String strNamespace = namespace.value();
			String strSpringId = service;
			if(StringUtils.isEmpty(strNamespace)){
				strNamespace = cc.getSimpleName();
				if(strNamespace.endsWith("Action")){
					strNamespace = strNamespace.substring(0,strNamespace.lastIndexOf("Action"));
				}
				strNamespace = strNamespace.substring(0,1).toLowerCase()+
				strNamespace.substring(1);
			}else if(strNamespace.startsWith("/")){
				strNamespace = strNamespace.substring(1);
			}

			if(StringUtils.isEmpty(strSpringId)){
				strSpringId = cc.getSimpleName();
				strSpringId = strSpringId.substring(0,1).toLowerCase()+
				strSpringId.substring(1);
			}
			CtMethod[] methods = cc.getDeclaredMethods();
			ArrayList<String[]> arrayMethods = new ArrayList<String[]>();
			
			HashMap<String,AutoFill> autofills = makeAutoFill(cc);
			
			for(int i=0;methods!=null && i<methods.length;i++){
				if(methods[i].getModifiers()==Modifier.PUBLIC 
						&& !methods[i].getName().startsWith("_") && !methods[i].getName().startsWith("convert") &&
						!methods[i].getName().startsWith("validate") && !methods[i].getName().equals("getCache")
						&& !methods[i].getName().equals("setCache"))
				{
					if(methods[i].getName().startsWith("get") || methods[i].getName().startsWith("set") 
							&& methods[i].getName().length()>3){
						String temp = methods[i].getName().substring(3);
						CtField f = null;
						try{
							f = cc.getField(temp);
						}catch(Exception e){
							try{
								f = cc.getField(StringHelp.firstLetterToLowerCase(temp));
							}catch(Exception ex){
								
							}
						}
						if(f!=null){
							continue;
						}
					}
					
					String[] strMethods = new String[2];
					strMethods[0] = methods[i].getName();
					Object[] annmethod = methods[i].getAvailableAnnotations();
					for(int j=0;annmethod!=null && j<annmethod.length;j++){
						if(annmethod[i] instanceof Method){
							strMethods[1] = ((Method)annmethod[i]).value();
							break;
						}
					}
					if(StringUtils.isEmpty(strMethods[1])){
						strMethods[1] = strMethods[0];
					}
					boolean fl = addActionMethod(cc,methods[i],autofills);
					if(fl){
						modifyCount++;
					}
					arrayMethods.add(strMethods);
				}
			}
			RuntimeProvider.addPackageConfig(groupid,appid, strSpringId, strNamespace, arrayMethods);
		}
		return modifyCount==0?false:true;
	}
	
	
	public boolean addActionMethod(CtClass cc,CtMethod ccMethod,HashMap<String,AutoFill> autofills) throws Exception{
		String methodName = ccMethod.getName();
		
		try{
			CtMethod m = cc.getDeclaredMethod("_"+methodName);
			if(m!=null){
				return false;
			}
		}catch(Exception e){
			
		}
		
		CtMethod convertMethod = null;
		CtMethod validateMethod = null;
		try{
			convertMethod = cc.getDeclaredMethod("convert_"+StringHelp.firstLetterToUpperCase(methodName));
		}catch(Exception e){
			
		}
		try{
			validateMethod = cc.getDeclaredMethod("validate_"+StringHelp.firstLetterToUpperCase(methodName));
		}catch(Exception e){
			
		}
		
		StringBuilder strMethod = new StringBuilder();
		
		AutoFill autoFill = autofills.get(methodName);
		if(convertMethod==null && autoFill!=null){
			StringBuilder convertBuilder = new StringBuilder();
			makeConvertMethod(convertBuilder,cc,ccMethod,autoFill);
			cc.addMethod(CtNewMethod.make(convertBuilder.toString(),cc));
		}
		
		if(validateMethod==null && autoFill!=null){
			StringBuilder validatorBuilder = new StringBuilder();
			makeValidatorMethod(validatorBuilder,cc,ccMethod,autoFill);
			cc.addMethod(CtNewMethod.make(validatorBuilder.toString(),cc));
		}
		
		try{
			convertMethod = cc.getDeclaredMethod("convert_"+StringHelp.firstLetterToUpperCase(methodName));
		}catch(Exception e){
			
		}
		try{
			validateMethod = cc.getDeclaredMethod("validate_"+StringHelp.firstLetterToUpperCase(methodName));
		}catch(Exception e){
			
		}
		
		
		CtClass retType = ccMethod.getReturnType();
		if(retType!=CtClass.voidType){
			strMethod.append("public Object _"+methodName+"(){");
		}else{
			strMethod.append("public void _"+methodName+"(){");
		}
		
		if(convertMethod!=null){
			if(convertMethod.getReturnType()!=CtClass.voidType){
				strMethod.append(convertMethod.getReturnType().getName()+" obj = "+convertMethod.getName()+"();");
			}else{
				strMethod.append(convertMethod.getName()+"();");
			}
		}
		
		if(validateMethod!=null){
			if(ccMethod.getReturnType()!=CtClass.voidType){
				strMethod.append(validateMethod.getName()+"(obj);");
			}else{
				strMethod.append(validateMethod.getName()+"();");
			}
		}
		
		if(ccMethod.getParameterTypes()!=null && ccMethod.getParameterTypes().length>0){
			if(ccMethod.getReturnType()!=CtClass.voidType){
				strMethod.append("return "+methodName+"(obj);");
			}else{
				strMethod.append(methodName+"(obj);");
			}
		}else{
			if(ccMethod.getReturnType()!=CtClass.voidType){
				strMethod.append("return "+methodName+"();");
			}else{
				strMethod.append(methodName+"();");
			}
		}
		strMethod.append("}");
		
		cc.addMethod(CtNewMethod.make(strMethod.toString(),cc));
		return true;
	}
	
	public HashMap<String,AutoFill> makeAutoFill(CtClass cc){
		try{
			String autoFillURL = cc.getName().replaceAll("\\.", "/")+".xml";
			URL url = ClassLoaderUtil.getResource(autoFillURL, QStrutsClassLoader.class);
			System.out.println(url+autoFillURL);
			if(url==null){
				return new HashMap(0);
			}
			File autoFillFile = new File(url.getFile());
			if(!autoFillFile.exists()){
				return new HashMap(0);
			}
			HashMap<String,AutoFill> autoFills = ValidatorFileParser.parseConfig(new FileInputStream(autoFillFile));
			return autoFills;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void makeConvertMethod(StringBuilder sb,CtClass cc,CtMethod method,AutoFill autofill) throws Exception{
		CtClass parameters[] = method.getParameterTypes();
		if(parameters.length==0){
			return;
		}
		CtClass parameter = parameters[0];
		
		Properties p = new Properties();
        applyDefaultConfiguration(p);
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(p);
        VelocityContext vc = new VelocityContext();
        vc.put("utils", new Utils());        
        vc.put("autofill", autofill);
        vc.put("cl", cc);
		vc.put("methodName", StringHelp.firstLetterToUpperCase(method.getName()));
        vc.put("clbean", parameter.getName());
		
        StringWriter writer = new StringWriter();
        Template t = velocityEngine.getTemplate("org/xz/qstruts/classloader/qstrutsloader-autofill-convert.vm");
        t.merge(vc, writer);
        writer.close();
		
        sb.append(writer.toString());
		
	}
	
	public void makeValidatorMethod(StringBuilder sb,CtClass cc,CtMethod method,AutoFill autofill) throws Exception{
		CtClass parameters[] = method.getParameterTypes();
		if(parameters.length==0){
			return;
		}
		CtClass parameter = parameters[0];
		
		Properties p = new Properties();
        applyDefaultConfiguration(p);
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(p);
        VelocityContext vc = new VelocityContext();
        vc.put("utils", new Utils());        
        vc.put("autofill", autofill);
    	vc.put("methodName", StringHelp.firstLetterToUpperCase(method.getName()));
        vc.put("cl", cc);
        vc.put("clbean", parameter.getName());
        
        StringWriter writer = new StringWriter();
        Template t = velocityEngine.getTemplate("org/xz/qstruts/classloader/qstrutsloader-autofill-validator.vm");
        t.merge(vc, writer);
        writer.close();
		
        sb.append(writer.toString());
		
	}
	
	private void applyDefaultConfiguration(Properties p) {
        // ensure that caching isn't overly aggressive

        /**
         * Load a default resource loader definition if there isn't one present.
         * Ben Hall (22/08/2003)
         */
        
        p.setProperty(Velocity.RESOURCE_LOADER, "strutsclass");
        

                /**
         * Refactored the Velocity templates for the Struts taglib into the classpath from the web path.  This will
         * enable Struts projects to have access to the templates by simply including the Struts jar file.
         * Unfortunately, there does not appear to be a macro for the class loader keywords
         * Matt Ho - Mon Mar 17 00:21:46 PST 2003
         */
        p.setProperty("strutsclass.resource.loader.description", "Velocity Classpath Resource Loader");
        p.setProperty("strutsclass.resource.loader.class", "org.apache.struts2.views.velocity.StrutsResourceLoader");
        p.setProperty("strutsclass.resource.loader.modificationCheckInterval", "2");
        p.setProperty("strutsclass.resource.loader.cache", "true");

    }
	
	public Class findClass(byte[] b) {
		return defineClass(null, b, 0, b.length);
	}
		
}