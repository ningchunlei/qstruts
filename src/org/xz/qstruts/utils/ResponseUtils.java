/**
 * ClassName:ResponseUtils.java
 * Authoer:ningcl
 * Date:2011-3-8
 */
package org.xz.qstruts.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
import org.xz.qxork2.ModuleResponse;

/**
 * @author ningcl
 * @version 1.0
 */
public class ResponseUtils {

	public static void copyResponse(HttpServletResponse response,ModuleResponse module) throws IOException {
		if(!StringUtils.isEmpty(module.getRedirectedUrl())){
			response.sendRedirect(module.getRedirectedUrl());
			return;
		}

		if(module.getStatus()>0){
			response.sendError(module.getStatus(), module.getErrorMessage());
			return;
		}

		if(!StringUtils.isEmpty(module.getContentType())){
			response.setContentType(module.getContentType());
		}

		if(!StringUtils.isEmpty(module.getCharacterEncoding())){
			response.setCharacterEncoding(module.getCharacterEncoding());
		}

		byte[] s = module.getContent().toByteArray();
		response.setContentLength(s.length);
		response.getOutputStream().write(s);
		response.getOutputStream().flush();
	}

}
