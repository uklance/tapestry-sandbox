package com.github.uklance.web;

import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public class DelegateRequest implements Request {
	private Request request;
	
	public DelegateRequest(Request request) {
		super();
		this.request = request;
	}

	public Object getAttribute(String arg0) {
		return request.getAttribute(arg0);
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public long getDateHeader(String arg0) {
		return request.getDateHeader(arg0);
	}

	public String getHeader(String arg0) {
		return request.getHeader(arg0);
	}

	public List<String> getHeaderNames() {
		return request.getHeaderNames();
	}

	public int getLocalPort() {
		return request.getLocalPort();
	}

	public Locale getLocale() {
		return request.getLocale();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getParameter(String arg0) {
		return request.getParameter(arg0);
	}

	public List<String> getParameterNames() {
		return request.getParameterNames();
	}

	public String[] getParameters(String arg0) {
		return request.getParameters(arg0);
	}

	public String getPath() {
		return request.getPath();
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public String getServerName() {
		return request.getServerName();
	}

	public int getServerPort() {
		return request.getServerPort();
	}

	public Session getSession(boolean arg0) {
		return request.getSession(arg0);
	}

	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	public boolean isSecure() {
		return request.isSecure();
	}

	public boolean isXHR() {
		return request.isXHR();
	}

	public void setAttribute(String arg0, Object arg1) {
		request.setAttribute(arg0, arg1);
	}
}
