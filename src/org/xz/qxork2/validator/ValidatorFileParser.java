/**
 * ClassName:QXworkValidatorFileParser.java
 * Authoer:ningcl
 * Date:2011-2-21 
 */
package org.xz.qxork2.validator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xz.qxork2.DomHelper;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ValidatorFileParser {
	
	public static HashMap<String,AutoFill> parseConfig(InputStream is){
		HashMap<String,AutoFill> validatorCfgs = new HashMap<String,AutoFill>();
        InputSource in = new InputSource(is);
        
        Document doc = DomHelper.parse(in, false);
        Element root = doc.getDocumentElement();
        NodeList childs = root.getChildNodes(); 
        
        for(int i=0;i<childs.getLength();i++){
        	Node eAutoFill = childs.item(i);
        	if(eAutoFill instanceof Element){
        		AutoFill autofill = new AutoFill();
            	autofill.setMethod(((Element)eAutoFill).getAttribute("method"));
            	
            	makeMapping(autofill, (Element)eAutoFill);
            	validatorCfgs.put(autofill.getMethod(), autofill);
        	}
        }
	    return validatorCfgs;    
	}
	
	private static void makeMapping(AutoFill autofill,Element eAutoFill){
		List<Mapping> list = new ArrayList<Mapping>();
		NodeList eMappings = eAutoFill.getChildNodes();
		for(int i=0;i<eMappings.getLength();i++){
			Node eMapping = eMappings.item(i);
			if(eMapping instanceof Element){
				Mapping mapping = new Mapping();
				mapping.setParameter(((Element)eMapping).getAttribute("parameter"));
				mapping.setBean(((Element)eMapping).getAttribute("bean"));
				mapping.setType(((Element)eMapping).getAttribute("type"));
				mapping.setMessage(((Element)eMapping).getAttribute("message"));
				makeValidator(mapping, ((Element)eMapping));
				list.add(mapping);
			}
		}
		autofill.setMapping(list);
	}
	
	private static void makeValidator(Mapping mapping,Element eMapping){
		List<Validator> list = new ArrayList<Validator>();
		NodeList eValidators = eMapping.getChildNodes();
		for(int i=0;i<eValidators.getLength();i++){
			Node eValidator = eValidators.item(i);
			if(eValidator instanceof Element){
				Validator validator = new Validator();
				validator.setExpression(((Element)eValidator).getAttribute("expression"));
				validator.setMessage(((Element)eValidator).getAttribute("message"));
				validator.setType(((Element)eValidator).getAttribute("type"));
				list.add(validator);
			}
		}
		mapping.setValidators(list);
	}

	public static void main(String[] args) {
		try {
			parseConfig(new FileInputStream("autofill.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
