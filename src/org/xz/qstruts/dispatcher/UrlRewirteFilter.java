package org.xz.qstruts.dispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

public class UrlRewirteFilter implements Filter{
	
	private static Logger log = Logger.getLogger(UrlRewirteFilter.class);
	public static final String DEFAULT_WEB_CONF_PATH = "/WEB-INF/urlrewrite.xml";
	private String confPath;
	private ServletContext context;

	public void init(FilterConfig filterConfig) {
		log.debug("filter init called");
		if (filterConfig == null) {
			log.error("unable to init filter as filter config is null");
			return;
		}

		log.debug("init: calling destroy just in case we are being re-inited uncleanly");
		destroyActual();

		this.context = filterConfig.getServletContext();
		if (this.context == null) {
			log.error("unable to init as servlet context is null");
			return;
		}
		loadConf();
	}

	private void loadConf() {
		InputStream inputStream = this.context.getResourceAsStream(this.confPath);
		URL confUrl = null;
		try {
			confUrl = this.context.getResource(this.confPath);
		}
		catch (MalformedURLException e) {
			log.debug(e);
		}
		String confUrlStr = null;
		if (confUrl != null)
			confUrlStr = confUrl.toString();

		if (inputStream == null) {
			log.error("unable to find urlrewrite conf file at " + this.confPath);
		} else {
			Conf conf = new Conf(this.context, inputStream, this.confPath, confUrlStr);
			if (log.isDebugEnabled()) {
				if (conf.getRules() != null)
					log.debug("inited with " + conf.getRules().size() + " rules");

				log.debug("conf is " + ((conf.isOk()) ? "ok" : "NOT ok"));
			}
			if (conf.isOk()) {
				UrlRewriteImpl.rewrite = new UrlRewriter(conf);
				log.info("loaded (conf ok)");
			} else {
				log.error("Conf failed to load");
				log.error("unloading existing conf");
				UrlRewriteImpl.rewrite = null;
			}
		}
	}

	public void destroy() {
		log.info("destroy called");
		destroyActual();
	}

	public void destroyActual() {
		if (UrlRewriteImpl.rewrite != null) {
			UrlRewriteImpl.rewrite.destroy();
			UrlRewriteImpl.rewrite = null;
		}
		this.context = null;
		this.confPath = "/WEB-INF/urlrewrite.xml";
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsRequest = (HttpServletRequest) request;
		HttpServletResponse hsResponse = (HttpServletResponse) response;

		boolean requestRewritten = false;
		if (UrlRewriteImpl.rewrite != null) {
			requestRewritten = UrlRewriteImpl.rewrite.processRequest(hsRequest, hsResponse, chain);
		} else if (log.isDebugEnabled()) {
			log.debug("urlRewriter engine not loaded ignoring request (could be a conf file problem)");
		}

		if (!(requestRewritten))
			chain.doFilter(hsRequest, hsResponse);
	}

}
