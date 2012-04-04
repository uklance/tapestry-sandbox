package com.github.uklance.mode;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;

import com.github.uklance.web.DelegateRequest;

public class ModeComponentEventLinkEncoder implements ComponentEventLinkEncoder {
	private static final Pattern URL_PATTERN = Pattern.compile("/([^/]*)(/.*)");
	private static final Pattern BASE_PATH_PATTERN = Pattern.compile("/([^/]*)/(.*)"); 
	private ComponentEventLinkEncoder delegate;
	private Set<String> specialPrefixes;
	private Environment environment;
	
	public ModeComponentEventLinkEncoder(ComponentEventLinkEncoder delegate, Environment environment, Set<String> specialPrefixes) {
		super();
		this.delegate = delegate;
		this.environment = environment;
		this.specialPrefixes = specialPrefixes;
	}
	
	public Link createPageRenderLink(PageRenderRequestParameters parameters) {
		return transform(delegate.createPageRenderLink(parameters));
	}
	public Link createComponentEventLink(ComponentEventRequestParameters parameters, boolean forForm) {
		return transform(delegate.createComponentEventLink(parameters, forForm));
	}
	public ComponentEventRequestParameters decodeComponentEventRequest(Request request) {
		return delegate.decodeComponentEventRequest(transform(request));
	}
	public PageRenderRequestParameters decodePageRenderRequest(Request request) {
		return delegate.decodePageRenderRequest(transform(request));
	}
	
	private Link transform(Link link) {
		Mode mode = environment.peek(Mode.class);
		Link transformed = link;
		if (mode != null && mode.getMode() != null) {
			Matcher matcher = BASE_PATH_PATTERN.matcher(link.getBasePath());
			if (!matcher.matches()) {
				throw new RuntimeException("Illegal path " + link.getBasePath());
			}
			String newPath = String.format("/%s/%s/%s", matcher.group(1), mode.getMode(), matcher.group(2));
			transformed = link.copyWithBasePath(newPath);
		}
		return transformed;
	}

	private Request transform(Request request) {
		Request transformed = request;
		Matcher matcher = URL_PATTERN.matcher(request.getPath());
		Mode mode = ModeImpl.DEFAULT;
		if (matcher.matches()) {
			String prefix = matcher.group(1);
			if (specialPrefixes.contains(prefix)) {
				mode = new ModeImpl(prefix);
				final String newPath = matcher.group(2);
				transformed = new DelegateRequest(request) {
					public String getPath() {
						return newPath;
					}
				};
			}
		}
		environment.push(Mode.class, mode);
		return transformed;
	}
}
