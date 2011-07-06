package org.xz.qstruts.components;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tuckey.web.filters.urlrewrite.OutboundRule;
import org.tuckey.web.filters.urlrewrite.RewrittenOutboundUrl;
import org.xz.qstruts.dispatcher.UrlRewriteImpl;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;

public class ModuleUrlRewrite extends Component{
	
	private String url="";
	
	/**
	 * @param stack
	 */
	public ModuleUrlRewrite(ValueStack stack) {
		super(stack);
		// TODO Auto-generated constructor stub
	}
	
	public boolean end(Writer writer, String body) {
        // attempt to match the rules
		HttpServletResponse rep = ActionContext.getHttpResponse(stack.getContext());
		HttpServletRequest req = ActionContext.getHttpRequest(stack.getContext());
        String finalToUrl = null;
        final List outboundRules = UrlRewriteImpl.rewrite.getConf().getOutboundRules();
        try {
            for (int i = 0; i < outboundRules.size(); i++) {
                final OutboundRule outboundRule = (OutboundRule) outboundRules.get(i);
                final RewrittenOutboundUrl rewrittenUrl = outboundRule.execute(url, req, rep);
                if (rewrittenUrl != null) {
                    finalToUrl = rewrittenUrl.getTarget();
                    if (outboundRule.isLast()) {
                        break;
                    }
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if(finalToUrl!=null){
        	return super.end(writer, finalToUrl);
        }else{
        	return super.end(writer, url);
        }
	}
	
	@Override
	public void copyParams(Map params) {
		// TODO Auto-generated method stub
		url = ((String)params.get("url"));
	}
	
	
}
