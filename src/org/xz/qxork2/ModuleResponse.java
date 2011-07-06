/**
 * ClassName:ModuleResponse.java
 * Authoer:ningcl
 * Date:2011-1-14 
 */
package org.xz.qxork2;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.util.FastByteArrayOutputStream;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.xz.qstruts.dispatcher.Dispatcher;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleResponse implements HttpServletResponse{
	
	 private HttpServletResponse response;
	 protected PrintWriter pagePrintWriter;
     private PageOutputStream pageOutputStream = null;
     private static final String CHARSET_PREFIX = "charset=";
     private boolean outputStreamAccessAllowed;
     private boolean writerAccessAllowed;
     private String characterEncoding;
     private PrintWriter writer;
     private int contentLength;
     private String contentType;
     private int bufferSize;
     private boolean committed;
     private Locale locale;
     private int status;
     private String errorMessage;
     private String redirectedUrl;
     private String forwardedUrl;
     private String includedUrl;

     
     
     static final class PageOutputStream extends ServletOutputStream {

         private FastByteArrayOutputStream buffer;


         public PageOutputStream() {
             buffer = new FastByteArrayOutputStream();
         }


         /**
          * Return all data that has been written to this OutputStream.
          */
         public FastByteArrayOutputStream getBuffer() throws IOException {
             flush();

             return buffer;
         }

         public void close() throws IOException {
             buffer.close();
         }

         public void flush() throws IOException {
             buffer.flush();
         }

         public void write(byte[] b, int o, int l) throws IOException {
             buffer.write(b, o, l);
         }

         public void write(int i) throws IOException {
             buffer.write(i);
         }

         public void write(byte[] b) throws IOException {
             buffer.write(b);
         }
     }
     
     static final class HeaderValueHolder
     {

         HeaderValueHolder()
         {
         }

         public void setValue(Object value)
         {
             values.clear();
             values.add(value);
         }

         public void addValue(Object value)
         {
             values.add(value);
         }

         public void addValues(Collection values)
         {
             this.values.addAll(values);
         }

         public void addValueArray(Object values)
         {
             CollectionUtils.mergeArrayIntoCollection(values, this.values);
         }

         public List getValues()
         {
             return Collections.unmodifiableList(values);
         }

         public Object getValue()
         {
             return values.isEmpty() ? null : values.get(0);
         }

         public static HeaderValueHolder getByName(Map headers, String name)
         {
             Assert.notNull(name, "Header name must not be null");
             for(Iterator it = headers.keySet().iterator(); it.hasNext();)
             {
                 String headerName = (String)it.next();
                 if(headerName.equalsIgnoreCase(name))
                     return (HeaderValueHolder)headers.get(headerName);
             }

             return null;
         }

         private final List values = new LinkedList();
     }
     
     /**
	 * @param response
	 */
	public ModuleResponse(HttpServletResponse response) {
		this.response = response;
		// TODO Auto-generated constructor stub
	}
	
	
	/**
     * Return the content buffered inside the {@link PageOutputStream}.
     *
     * @return
     * @throws IOException
     */
    public FastByteArrayOutputStream getContent() throws IOException {
        //if we are using a writer, we need to flush the
        //data to the underlying outputstream.
        //most containers do this - but it seems Jetty 4.0.5 doesn't
        if (pagePrintWriter != null) {
            pagePrintWriter.flush();
        }

        return ((PageOutputStream) getOutputStream()).getBuffer();
    }

    /**
     * Return instance of {@link PageOutputStream}
     * allowing all data written to stream to be stored in temporary buffer.
     */
    public ServletOutputStream getOutputStream() throws IOException {
        if (pageOutputStream == null) {
            pageOutputStream = new PageOutputStream();
        }

        return pageOutputStream;
    }

    /**
     * Return PrintWriter wrapper around PageOutputStream.
     */
    public PrintWriter getWriter() throws IOException {
        if (pagePrintWriter == null) {
            pagePrintWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
        }

        return pagePrintWriter;
    }
    
    public void flushBuffer() throws IOException{
    	
    }
    
    public int getBufferSize(){
    	try{
    		return pageOutputStream.getBuffer().getSize();
    	}catch(Exception e){
    		
    	}
    	return 0; 
    }
    
	

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		response.addCookie(cookie);
	}


	 public void setDateHeader(String name, long value)
	    {
	        this.response.setDateHeader(name, value);
	    }

	    public void addDateHeader(String name, long value)
	    {
	    	this.response.addDateHeader(name, value);
	    }

	    public void setHeader(String name, String value)
	    {
	        this.response.setHeader(name, value);
	    }

	    public void addHeader(String name, String value)
	    {
	        this.response.addHeader(name, value);
	    }

	    public void setIntHeader(String name, int value)
	    {
	        this.response.setIntHeader(name, value);
	    }

	    public void addIntHeader(String name, int value)
	    {
	    	this.response.addIntHeader(name, value);
	    }

	    public void setStatus(int status)
	    {
	        this.status = status;
	    }

	    public void setStatus(int status, String errorMessage)
	    {
	        this.status = status;
	        this.errorMessage = errorMessage;
	    }

	    public int getStatus()
	    {
	        return status;
	    }

	    public String getErrorMessage()
	    {
	        return errorMessage;
	    }
	


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
	 */
	public boolean containsHeader(String s) {
		// TODO Auto-generated method stub
		return response.containsHeader(s);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
	 */
	public String encodeRedirectURL(String s) {
		// TODO Auto-generated method stub
		return response.encodeRedirectURL(s);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
	 */
	public String encodeRedirectUrl(String s) {
		// TODO Auto-generated method stub
		return response.encodeRedirectUrl(s);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
	 */
	public String encodeURL(String s) {
		// TODO Auto-generated method stub
		return response.encodeURL(s);
	}


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
	 */
	public String encodeUrl(String s) {
		// TODO Auto-generated method stub
		return response.encodeUrl(s);
	}


	public void sendError(int status, String errorMessage)
    throws IOException
    {
        this.status = status;
        this.errorMessage = errorMessage;
        return;
	}
	
	public void sendError(int status)
	    throws IOException
	{
	        this.status = status;
	        return;
	}
	
	public void sendRedirect(String url)
	    throws IOException
	{
		redirectedUrl = url;
	   
	}
	
	public String getRedirectedUrl()
	{
	    return redirectedUrl;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		if(characterEncoding!=null){
			return characterEncoding;
		}
		return Dispatcher.getInstance().getEncoding();
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		return contentType;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#getLocale()
	 */
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return locale;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#isCommitted()
	 */
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#reset()
	 */
	public void reset() {
		// TODO Auto-generated method stub
		resetBuffer();
        characterEncoding = null;
        contentLength = 0;
        contentType = null;
        locale = null;
        status = 200;
        errorMessage = null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#resetBuffer()
	 */
	public void resetBuffer() {
		// TODO Auto-generated method stub
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setBufferSize(int)
	 */
	public void setBufferSize(int i) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String s) {
		// TODO Auto-generated method stub
		this.characterEncoding = s;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentLength(int)
	 */
	public void setContentLength(int i) {
		// TODO Auto-generated method stub
		this.contentLength = i;
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
	 */
	public void setContentType(String s) {
		// TODO Auto-generated method stub
		this.contentType = s;
        if(contentType != null)
        {
            int charsetIndex = contentType.toLowerCase().indexOf(CHARSET_PREFIX);
            if(charsetIndex != -1)
            {
                String encoding = contentType.substring(charsetIndex + CHARSET_PREFIX.length());
                setCharacterEncoding(encoding);
            }
        }
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		// TODO Auto-generated method stub
		this.locale = locale;
	}
	
}
