package com.dosth.zuulserver.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 
 * @description zuul访问过滤器
 *
 * @author guozhidong
 */
@Configuration
public class AccessFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(AccessFilter.class);
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		logger.info(String.format("%s AccessFilter request to %s", request.getMethod(), request.getRequestURI()));
		String accessToken = request.getParameter("accessToken");
		// 有权限令牌
		if (!StringUtils.isEmpty(accessToken)) {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(HttpStatus.SC_OK);
			// 可以设置一些值
			ctx.set("isSuccess", true);
		} else {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
			ctx.setResponseBody("{\"result\":\"accessToken is not correct!\"}");
			// 可以设置一些值
			ctx.set("isSuccess", false);
		}
		return null;
	}
	
	/**
	 * 过滤器类型,在zuul中定义了四种不同声明周期的过滤器类型
	 * public static final String ERROR_TYPE = ""error;
	 * public static final String POST_TYPE = "post";
	 * public static final String PRE_TYPE = "pre";
	 * public static final String ROUTE_TYPE = "route";
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}
}