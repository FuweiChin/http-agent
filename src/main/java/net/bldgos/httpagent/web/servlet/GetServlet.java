package net.bldgos.httpagent.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
		//response
		response.setStatus(conn.getResponseCode());
		for(Map.Entry<String,List<String>> e:conn.getHeaderFields().entrySet()) {
			String name=e.getKey();
			if(!ignoreResponseHeaderNames.contains(name)) {
				String value=e.getValue().get(0);
				response.setHeader(name, value);
			}
		}
		try(InputStream input=conn.getInputStream();
				ServletOutputStream output=response.getOutputStream();){
			IOUtils.copyLarge(input, output);
		}
	}
}
