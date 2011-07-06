/**
 * ClassName:DesignTagHandler.java
 * Authoer:ningcl
 * Date:2011-3-21 
 */
package org.xz.xdoclet.design;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xdoclet.DocletSupport;
import xdoclet.XDocletException;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.util.LogUtil;
import xjavadoc.XClass;
import xjavadoc.XField;
import xjavadoc.XMethod;
import xjavadoc.XTag;

/**
 * @author ningcl
 * @version 1.0 
 * @xdoclet.taghandler   namespace="Design"
 */
public class DesignTagHandler extends AbstractProgramElementTagsHandler{
	
	private static final String Design_System = "design.system";
	private static final String Design_Module = "design.module";
	private static final String Design_Type = "design.type";
	private static final String Design_Version = "design.version";
	private static final String Design_Function = "design.function";
	
	
	Logger logger = Logger.getLogger(DesignTagHandler.class);
	
	private JSONArray sysArray = null;
	private XTag sysObj = null;
	private XTag sysMod = null;
	private XTag sysFn = null;
	int isysFn = 0;
	private XTag sysType = null;
	private XTag sysVersion = null;
	private XClass sysClass = null;
	private List<XTag> sysVersions = null;
	int isysVersion = 0;
	private List sysFns = null;
	private XMethod  sysMethod = null;
	private XField  sysField = null;
	
	private LinkedHashMap<String,LinkedHashMap> m_sysMap = new LinkedHashMap<String,LinkedHashMap>();
	private LinkedHashMap<String,LinkedHashMap> m_Tree = new LinkedHashMap<String,LinkedHashMap>();
	
	
	/**
	 * Itrator modules 
	 * 
	 * @param template
	 * @param attributes
	 * @throws XDocletException
	 * 
	 * @doc.tag				 type="block"
	 * @doc.param            name="name" optional="false" 
	 */
	public void forAllModules(String template, Properties attributes) throws XDocletException{
		LinkedHashMap mods = m_sysMap.get(sysObj.getAttributeValue("name"));
		for(Iterator it = mods.keySet().iterator();it.hasNext();){
    		String mod = (String)it.next();
			LinkedHashMap xsys = m_Tree.get(sysObj.getAttributeValue("name"));
			if(xsys==null){
				continue;
			}
			LinkedHashMap xmods = (LinkedHashMap)xsys.get(mod);
			if(xmods==null){
				continue;
			}
			sysMod = (XTag)xmods.get(Design_Module);
			logger.info(xmods);
			generate(template);
		}
	}
	
	public void forAllClass(String template, Properties attributes) throws XDocletException{
		String typeName = attributes.getProperty("type");
        if(typeName==null){
        	return;
        }
		LinkedHashMap fns = (LinkedHashMap)m_Tree.get(sysObj.getAttributeValue("name")).get(sysMod.getAttributeValue("name"));
		LinkedHashMap clazz = (LinkedHashMap)fns.get(typeName);
		if(clazz!=null){
			for(Iterator it = clazz.values().iterator();it.hasNext();){
				List list = (List)it.next();
				sysClass = (XClass)list.get(2);
				sysType = (XTag)list.get(0);
				sysVersions = (List)list.get(1);
				sysFns = list.subList(3,list.size());
				System.out.println(sysFns+","+typeName);
				generate(template);
			}
		}
	}
	
	public void ifVersionSize(String template, Properties attributes) throws XDocletException{
		if(sysVersions!=null && sysVersions.size()>0){
			generate(template);
		}
	}
	
	/**
	 * test 
	 * 
	 * @param template
	 * @param attributes
	 * @throws XDocletException
	 * @doc.tag			type="block"
	 * @doc.param		name="type" optional="false"
	 */
	public void ifFunctionSize(String template, Properties attributes) throws XDocletException{
		String typeName = attributes.getProperty("type");
        if(typeName==null){
        	return;
        }
		LinkedHashMap fns = (LinkedHashMap)m_Tree.get(sysObj.getAttributeValue("name")).get(sysMod.getAttributeValue("name"));
		List list = (List)fns.get(typeName);
		if(list!=null && list.size()>0){
			generate(template);
		}
	}
	
	/**
	 * Iterator Function by type
	 * 
	 * @param template
	 * @param attributes
	 * @throws XDocletException
	 * @doc.tag			type="block"
	 * @doc.param		name="type" optional="false"
	 */
	public void forAllFunction(String template)throws XDocletException{
		for(int i=0;sysFns!=null && i<sysFns.size();i++){
			Object[] t = (Object[])sysFns.get(i);
			isysFn = i;
			sysFn = (XTag)t[0];
			sysMethod=(XMethod)t[1];
			sysField = (XField)t[2];
			generate(template);
		}
	}
	
	/**
	 * Return value of Function
	 * 
	 * @param attributes
	 * @return
	 * @throws XDocletException
	 * @doc.tag			type="content"
	 * @doc.param		name="name"	 optional="false"
	 */
	public String fn(Properties attributes) throws XDocletException{
		String typeName = attributes.getProperty("name");
        if(typeName==null){
        	return "";
        }
        String r = sysFn.getAttributeValue(typeName)==null?"":sysFn.getAttributeValue(typeName); 
		r = r.replaceAll("<inline/> ", "");
		return r;
	}
	
	public void forAllVersion(String template, Properties attributes)throws XDocletException{
		for(int i=0;sysVersions!=null && i<sysVersions.size();i++){
			sysVersion = sysVersions.get(i);
			isysVersion=i;
			generate(template);
		}
	}
	
	public String oddoreven(Properties attributes) throws XDocletException{
		String odd = attributes.getProperty("odd");
		String even = attributes.getProperty("even");
		String t = attributes.getProperty("type");
		if(t.equals("fn")){
			return isysFn%2==0?even:odd;
		}
		return isysVersion%2==0?even:odd;
	}
	
	public String version(Properties attributes) throws XDocletException{
		String typeName = attributes.getProperty("name");
        if(typeName==null){
        	return "";
        }
        String r = sysVersion.getAttributeValue(typeName)==null?"":sysVersion.getAttributeValue(typeName); 
		r = r.replaceAll("<inline/> ", "");
		return r;
	}
	
	
	/**
	 * Return value of Type
	 * 
	 * @param attributes
	 * @return
	 * @throws XDocletException
	 * @doc.tag			type="content"
	 * @doc.param		name="name"	 optional="false"
	 */
	public String type(Properties attributes) throws XDocletException{
		String typeName = attributes.getProperty("name");
        if(typeName==null){
        	return "";
        }
		return sysType.getAttributeValue(typeName)==null?"":sysType.getAttributeValue(typeName);
	}
	
	/**
	 * Return Name of Module
	 * @return
	 * @throws XDocletException
	 * @doc.tag 		type="content"
	 */
	public String moduleTitle() throws XDocletException
	{
		return sysMod.getAttributeValue("name");
	}
	
	/**
	 * Return Name of System
	 * @return
	 * @throws XDocletException
	 * @doc.tag			type="content"
	 */
	public String systemTitle() throws XDocletException
    {
        return sysObj.getAttributeValue("name");
    }
	
	/**
	 * 
	 * @param template
	 * @param attributes
	 * @throws XDocletException
	 * @doc.tag			type="block"
	 * @doc.param		name="type" optional="false"
	 */
    public void forAllSystems(String template, Properties attributes) throws XDocletException
    {
    	
    	Log log = LogUtil.getLog(DesignTagHandler.class, "forAllSystems");        
        String typeName = attributes.getProperty("type");
        if(typeName==null){
        	return;
        }
        try {
			pushJSON(typeName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	registerClass();
    	for(Iterator it = m_sysMap.keySet().iterator();it.hasNext();){
    		String sysName = (String)it.next();
    		LinkedHashMap lMap = (LinkedHashMap)m_Tree.get(sysName);
    		if(lMap!=null){
    			sysObj = (XTag)lMap.get(Design_System);
    			generate(template);
    		}
    	}
    	
    }
    
    private void pushJSON(String t) throws JSONException{
    	JSONObject root = new JSONObject(t);
    	sysArray = root.getJSONArray("m");
    	
    	for(int i=0;i<sysArray.length();i++){
    		LinkedHashMap sys = m_sysMap.get(sysArray.getJSONObject(i).getString("id"));
    		if(sys==null){
    			sys = new LinkedHashMap();
    			m_sysMap.put(sysArray.getJSONObject(i).getString("id"), sys);
    		}
    		JSONObject json = sysArray.getJSONObject(i);
    		for(int j=0;json.get("m")!=null && 
    			j<json.getJSONArray("m").length();j++){
    			sys.put(json.getJSONArray("m").getJSONObject(j).getString("id"), json.getJSONArray("m").getJSONObject(j));
    		}
    	}
    }
    
    private void registerClass(){
    	Log log = LogUtil.getLog(DesignTagHandler.class, "forAllSystems");
    	for (Iterator i = getAllClasses().iterator(); i.hasNext(); ) {
	   		XClass currentClass = (XClass) i.next();
	
	        setCurrentClass(currentClass);
	        log.info("currentClass=" + currentClass);
	   		
	   		if (DocletSupport.isDocletGenerated(getCurrentClass())) {
               log.debug("isDocletGenerated or isAbstract");
               continue;
	   		}
	   		
	   		String cName = currentClass.getQualifiedName();
	   		XTag sysTag = null;
	   		if((sysTag=currentClass.getDoc().getTag(Design_System))==null){
	   			log.debug(cName+" isn't design");
	            continue;
	   		}
	   		if(m_sysMap.get(sysTag.getAttributeValue("name"))==null){
	   			log.info(cName+"doesn't support this system");
	   			continue;
	   		}
	   		
	   		XTag typeTag = null;
	   		if((typeTag=currentClass.getDoc().getTag(Design_Type))==null){
	   			log.info(cName+"doesn't support type");
	   			continue;
	   		}
	   		
	   		XTag modTag = null;
	   		if((modTag=currentClass.getDoc().getTag(Design_Module))==null){
	   			log.info(cName+"doesn't support module");
	   			continue;
	   		}
	   		
	   		List<XTag> vTag = currentClass.getDoc().getTags(Design_Version);
	   		
	   		registerService(sysTag,typeTag,modTag,vTag,getCurrentClass());
    	}
    }
    
    private void registerService(XTag sys,XTag type,XTag mod,List<XTag> v,XClass xclazz){
    	Log log = LogUtil.getLog(DesignTagHandler.class, "forAllSystems");
    	String cName = xclazz.getQualifiedName();
    	List<XField> fields = xclazz.getFields();
		for(int f = 0;f<fields.size();f++){
			XField xField = fields.get(f);
			XTag xFn = xField.getDoc().getTag(Design_Function);
			registerDoc(sys,type,mod,v,xclazz,xFn,null,xField);
		}
		List<XMethod> methods = xclazz.getMethods();
		for(int f = 0;f<methods.size();f++){
			XMethod xmethod = methods.get(f);
			log.info(xmethod);
			XTag xFn = xmethod.getDoc().getTag(Design_Function);
			registerDoc(sys,type,mod,v,xclazz,xFn,xmethod,null);
		}
    }
    
    private void registerDoc(XTag sys,XTag type,XTag mod,List<XTag> v,XClass xclazz,XTag xFn,XMethod m,XField f){
    	if(xFn==null){
			return;
		}
		String sysStr = sys.getAttributeValue("name");
		String modStr = mod.getAttributeValue("name");
		
		LinkedHashMap sysTree = m_Tree.get(sysStr);
		if(sysTree==null){
			sysTree = new LinkedHashMap();
			sysTree.put(Design_System, sys);
			m_Tree.put(sysStr, sysTree);
		}
		
		LinkedHashMap modTree = (LinkedHashMap)sysTree.get(modStr);
		if(modTree==null){
			modTree = new LinkedHashMap();
			modTree.put(Design_Module, mod);
			sysTree.put(modStr, modTree);
		}
		
		LinkedHashMap clazzTree = (LinkedHashMap)modTree.get(type.getAttributeValue("name"));
		if(clazzTree==null){
			clazzTree = new LinkedHashMap();
			modTree.put(type.getAttributeValue("name"), clazzTree);
		} 
		
		List fnTree = (List)clazzTree.get(xclazz.getQualifiedName());
		if(fnTree==null){
			fnTree = new ArrayList();
			fnTree.add(type);
			fnTree.add(v);
			fnTree.add(xclazz);
			clazzTree.put(xclazz.getQualifiedName(), fnTree);
		} 
		
		Object xlist[] = new Object[3];
		xlist[0]=xFn;
		xlist[1]=m;
		xlist[2]=f;
		fnTree.add(xlist);
    }
    
}
