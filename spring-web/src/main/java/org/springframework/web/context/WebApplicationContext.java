/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.context;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import javax.servlet.ServletContext;

/**
 * Interface to provide configuration for a web application. This is read-only while
 * the application is running, but may be reloaded if the implementation supports this.
 *
 * <p>This interface adds a {@code getServletContext()} method to the generic
 * ApplicationContext interface, and defines a well-known application attribute name
 * that the root context must be bound to in the bootstrap process.
 *
 * <p>Like generic application contexts, web application contexts are hierarchical.
 * There is a single root context per application, while each servlet in the application
 * (including a dispatcher servlet in the MVC framework) has its own child context.
 *
 * <p>In addition to standard application context lifecycle capabilities,
 * WebApplicationContext implementations need to detect {@link ServletContextAware}
 * beans and invoke the {@code setServletContext} method accordingly.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since January 19, 2001
 * @see ServletContextAware#setServletContext
 */
/*
提供Web应用程序配置的接口。 这在应用程序运行时是只读的，但是如果实现支持这个，可以重新加载。
该接口将一个getServletContext（）方法添加到通用ApplicationContext接口，并定义一个众所周知的应用程序属性名称，该名称在引导进程中必须绑定根上下文。

像通用应用程序上下文一样，Web应用程序上下文是分层的。 每个应用程序都有一个根上下文，而应用程序中的每个servlet（包括MVC框架中的调度器servlet）都有自己的子上下文。

除了标准的应用程序上下文生命周期功能，WebApplicationContext实现还需要检测ServletContextAware bean，并相应地调用setServletContext方法。
 */
public interface WebApplicationContext extends ApplicationContext {

	/**
	 * Context attribute to bind root WebApplicationContext to on successful startup.
	 * <p>Note: If the startup of the root context fails, this attribute can contain
	 * an exception or error as value. Use WebApplicationContextUtils for convenient
	 * lookup of the root WebApplicationContext.
	 * @see org.springframework.web.context.support.WebApplicationContextUtils#getWebApplicationContext
	 * @see org.springframework.web.context.support.WebApplicationContextUtils#getRequiredWebApplicationContext
	 */
	/*
	上下文属性绑定根WebApplicationContext在成功启动。
	注意：如果根上下文的启动失败，则此属性可能包含异常或错误值。
	使用WebApplicationContextUtils方便查找根WebApplicationContext。
	 */
	String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

	/**
	 * Scope identifier for request scope: "request".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	/*
	请求范围的范围标识符：“request”。 除了标准范围“singleton”和“prototype”以外，还支持它。
	 */
	String SCOPE_REQUEST = "request";

	/**
	 * Scope identifier for session scope: "session".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	/*
	会话范围的范围标识符：“session”。 除了标准范围“singleton”和“prototype”以外，还支持它。
	 */
	String SCOPE_SESSION = "session";

	/**
	 * Scope identifier for the global web application scope: "application".
	 * Supported in addition to the standard scopes "singleton" and "prototype".
	 */
	/*
	全局Web应用程序作用域的作用域标识符：“application”。 除了标准范围“singleton”和“prototype”以外，还支持它。
	 */
	String SCOPE_APPLICATION = "application";

	/**
	 * Name of the ServletContext environment bean in the factory.
	 * @see javax.servlet.ServletContext
	 */
	/*
	工厂中的ServletContext环境bean的名称。
	 */
	String SERVLET_CONTEXT_BEAN_NAME = "servletContext";

	/**
	 * Name of the ServletContext/PortletContext init-params environment bean in the factory.
	 * <p>Note: Possibly merged with ServletConfig/PortletConfig parameters.
	 * ServletConfig parameters override ServletContext parameters of the same name.
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 * @see javax.servlet.ServletContext#getInitParameter(String)
	 * @see javax.servlet.ServletConfig#getInitParameterNames()
	 * @see javax.servlet.ServletConfig#getInitParameter(String)
	 */
	/*
	工厂中ServletContext / PortletContext 初始化参数环境bean的名称。
	注意：可能与ServletConfig / PortletConfig参数合并。 ServletConfig参数覆盖同名的ServletContext参数。
	 */
	String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";

	/**
	 * Name of the ServletContext/PortletContext attributes environment bean in the factory.
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 * @see javax.servlet.ServletContext#getAttribute(String)
	 */
	/*
	工厂中的ServletContext / PortletContext属性环境bean的名称。
	 */
	String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";


	/**
	 * Return the standard Servlet API ServletContext for this application.
	 */
	/*
	为此应用程序返回标准的Servlet API ServletContext。
	 */
	@Nullable
	ServletContext getServletContext();

}
