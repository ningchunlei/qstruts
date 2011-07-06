/**
 * ClassName:RuntimeProvider.java
 * Authoer:ningcl
 * Date:2010-12-21 
 */
package org.xz.qstruts.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.util.location.LocatableProperties;

/**
 * @author ningcl
 * @version 1.0 
 */
public class RuntimeProvider implements ConfigurationProvider{
	
	public static ArrayList<PackageConfig.Builder> list = new ArrayList<PackageConfig.Builder>();
	private Configuration configuration;
	private static HashMap<String,PackageConfig.Builder> map = new HashMap();
	
	public static void addPackageConfig(String groupid,String appid,String springId,String actionName,List<String[]> methods){
		PackageConfig.Builder cfg = null;
		boolean flag = false;
		if((cfg=map.get(groupid+"-"+appid))==null){
			cfg = new PackageConfig.Builder(groupid+"-"+appid)
	         .namespace("/"+appid)
	         .isAbstract(false)
	         .location(null);
			flag = true;
			map.put(groupid+"-"+appid, cfg);
		}
		 
		if(methods.size()>0){
			 ActionConfig.Builder actionConfig = new ActionConfig.Builder(cfg.getName(),actionName, springId);
			 for(int i=0;methods!=null && i<methods.size();i++){
				 String methodName = methods.get(i)[0];
				 actionConfig.addAllowedMethod(methodName).location(null);
			 }
			 cfg.addActionConfig(actionName, actionConfig.build());
		}
		 if(flag){
			 list.add(cfg);
		 }
	}
	
	public void destroy() {
    }

    public void init(Configuration configuration) throws ConfigurationException {
    	this.configuration = configuration;
    }

    public void register(ContainerBuilder builder, LocatableProperties props)
            throws ConfigurationException {
  
    }

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.ContainerProvider#needsReload()
	 */
	public boolean needsReload() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.PackageProvider#loadPackages()
	 */
	public void loadPackages() throws ConfigurationException {
		// TODO Auto-generated method stub
		for(int i=0;i<list.size();i++){
    		PackageConfig cfg = list.get(i).build();
    		if(configuration.getPackageConfig(cfg.getName())==null){
    			configuration.addPackageConfig(cfg.getName(), cfg);
    		}
    	}
    	map.clear();
    	list.clear();
	}
	
}
