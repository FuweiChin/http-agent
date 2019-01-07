package net.bldgos.httpagent.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

@WebServlet(urlPatterns="/get")
public class GetServlet extends HttpServlet {
	private static final long serialVersionUID = 3712032117611038391L;
	private List<String> ignoreRequestHeaderNames=new ArrayList<>();
	private List<String> ignoreResponseHeaderNames=new ArrayList<>();
	@Override
	public void init() throws ServletException {
		super.init();
		ignoreRequestHeaderNames.add("Connection");
		ignoreRequestHeaderNames.add("Upgrade-Insecure-Requests");
		ignoreResponseHeaderNames.add("Connection");
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url=request.getParameter("url");
		if(url==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if(url.startsWith("web+http:")||url.startsWith("web+https:")){
			url=url.substring("web+".length());
		}
		URL urlObj=new URL(url);
		HttpURLConnection conn=(HttpURLConnection)urlObj.openConnection();
		//request
		conn.setRequestMethod("GET");
		for(Enumeration<String> e=request.getHeaderNames(); e.hasMoreElements(); ) {
			String name=e.nextElement();
			if(!ignoreRequestHeaderNames.contains(name)) {
				conn.setRequestProperty(name, request.getHeader(name));
			}
		}
		conn.setRequestProperty("Connection", "close");
		//response
		response.setStatus(conn.getResponseCode());
		for(Map.Entry<String,List<String>> e:conn.getHeaderFields().entrySet()) {
			String name=e.getKey();
			if(!ignoreResponseHeaderNames.contains(name)) {
				String value=e.getValue().get(0);
				response.setHeader(name, value);
			}
		}
		String contentDisposition=response.getHeader("Content-Disposition");
		if(contentDisposition==null) {
			String filename=urlObj.getPath().substring(urlObj.getPath().lastIndexOf("/")+1);
			if(filename.length()>0) {
				String iso_8859_1_enc=new String(filename.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
				String rfc_5987_enc=URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
				response.setHeader("Content-Disposition", "attachment; filename=\""+iso_8859_1_enc+"\"; filename*=UTF-8''"+rfc_5987_enc);
			}
		}
		try(InputStream input=conn.getInputStream();
				ServletOutputStream output=response.getOutputStream();){
			IOUtils.copyLarge(input, output);
		}
	}
}
