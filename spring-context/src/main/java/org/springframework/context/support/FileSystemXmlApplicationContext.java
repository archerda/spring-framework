/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

/**
 * Standalone XML application context, taking the context definition files
 * from the file system or from URLs, interpreting plain paths as relative
 * file system locations (e.g. "mydir/myfile.txt"). Useful for test harnesses
 * as well as for standalone environments.
 *
 * <p><b>NOTE:</b> Plain paths will always be interpreted as relative
 * to the current VM working directory, even if they start with a slash.
 * (This is consistent with the semantics in a Servlet container.)
 * <b>Use an explicit "file:" prefix to enforce an absolute file path.</b>
 *
 * <p>The config location defaults can be overridden via {@link #getConfigLocations},
 * Config locations can either denote concrete files like "/myfiles/context.xml"
 * or Ant-style patterns like "/myfiles/*-context.xml" (see the
 * {@link org.springframework.util.AntPathMatcher} javadoc for pattern details).
 *
 * <p>Note: In case of multiple config locations, later bean definitions will
 * override ones defined in earlier loaded files. This can be leveraged to
 * deliberately override certain bean definitions via an extra XML file.
 *
 * <p><b>This is a simple, one-stop shop convenience ApplicationContext.
 * Consider using the {@link GenericApplicationContext} class in combination
 * with an {@link org.springframework.beans.factory.xml.XmlBeanDefinitionReader}
 * for more flexible context setup.</b>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see #getResource
 * @see #getResourceByPath
 * @see GenericApplicationContext
 */
/*
独立XML应用程序上下文，从文件系统或URL中获取上下文定义文件，将纯路径解释为相对文件系统位置（例如“mydir / myfile.txt”）。
对测试环境以及独立环境非常有用。

注：平滑路径将始终被解释为相对于当前VM工作目录，即使它们以斜杠开始。 （这与Servlet容器中的语义一致。）
使用明确的“file：”前缀来强制执行绝对文件路径。

配置位置的默认值可以通过AbstractRefreshableConfigApplicationContext.getConfigLocations（）来覆盖，
配置位置可以表示具体的文件，如“/myfiles/context.xml”，
或者是“/myfiles/*-context.xml”的Ant样式模式（参见AntPathMatcher javadoc模式细节）。

注意：如果有多个配置位置，稍后的bean定义将覆盖在先前加载的文件中定义的定义。这可以用来通过一个额外的XML文件故意重写某些bean定义。

这是一个简单的，一站式便捷的ApplicationContext。
考虑将GenericApplicationContext类与XmlBeanDefinitionReader结合使用，以实现更灵活的上下文设置。
 */

// 从文件系统下的一个或多个XML配置文件中加载上下文定义;
public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {

	/**
	 * Create a new FileSystemXmlApplicationContext for bean-style configuration.
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	/*
	为bean风格的配置创建一个新的FileSystemXmlApplicationContext。
	 */
	// TODO luohd 01/02/2018: 为什么这个构造器无参数，那默认都使用哪些参数？
	public FileSystemXmlApplicationContext() {
	}

	/**
	 * Create a new FileSystemXmlApplicationContext for bean-style configuration.
	 * @param parent the parent context
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	/*
	为bean风格的配置创建一个新的FileSystemXmlApplicationContext。
	 */
	public FileSystemXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	/**
	 * Create a new FileSystemXmlApplicationContext, loading the definitions
	 * from the given XML file and automatically refreshing the context.
	 * @param configLocation file path
	 * @throws BeansException if context creation failed
	 */
	/*
	创建一个新的FileSystemXmlApplicationContext，从给定的XML文件加载定义并自动刷新上下文。
	 */
	public FileSystemXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, true, null);
	}

	/**
	 * Create a new FileSystemXmlApplicationContext, loading the definitions
	 * from the given XML files and automatically refreshing the context.
	 * @param configLocations array of file paths
	 * @throws BeansException if context creation failed
	 */
	/*
	创建一个新的FileSystemXmlApplicationContext，从给定的XML文件中加载定义并自动刷新上下文。
	 */
	public FileSystemXmlApplicationContext(String... configLocations) throws BeansException {
		this(configLocations, true, null);
	}

	/**
	 * Create a new FileSystemXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files and automatically
	 * refreshing the context.
	 * @param configLocations array of file paths
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 */
	/*
	用给定的父级上下文创建一个新的FileSystemXmlApplicationContext，从给定的XML文件加载定义并自动刷新上下文。
	 */
	public FileSystemXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
		this(configLocations, true, parent);
	}

	/**
	 * Create a new FileSystemXmlApplicationContext, loading the definitions
	 * from the given XML files.
	 * @param configLocations array of file paths
	 * @param refresh whether to automatically refresh the context,
	 * loading all bean definitions and creating all singletons.
	 * Alternatively, call refresh manually after further configuring the context.
	 * @throws BeansException if context creation failed
	 * @see #refresh()
	 */
	/*
	创建一个新的FileSystemXmlApplicationContext，从给定的XML文件中加载定义。
	 */
	public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
		this(configLocations, refresh, null);
	}

	/**
	 * Create a new FileSystemXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files.
	 * @param configLocations array of file paths
	 * @param refresh whether to automatically refresh the context,
	 * loading all bean definitions and creating all singletons.
	 * Alternatively, call refresh manually after further configuring the context.
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 * @see #refresh()
	 */
	/*
	用给定的父项创建一个新的FileSystemXmlApplicationContext，从给定的XML文件中加载定义。
	 */
	public FileSystemXmlApplicationContext(
			String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
			throws BeansException {

		// 首先,调用父类容器的构造方法为容器设置好资源加载器;
		super(parent);

		// 然后，再调用父类AbstractRefreshableConfigApplicationContext的setConfigLocations(configLocations)方法
		// 设置Bean定义资源文件的定位路径。
		setConfigLocations(configLocations);


		if (refresh) {
			// 这个refresh方法启动了对BeanDefinition的载入过程。
			refresh();
		}
	}


	/**
	 * Resolve resource paths as file system paths.
	 * <p>Note: Even if a given path starts with a slash, it will get
	 * interpreted as relative to the current VM working directory.
	 * This is consistent with the semantics in a Servlet container.
	 * @param path path to the resource
	 * @return Resource handle
	 * @see org.springframework.web.context.support.XmlWebApplicationContext#getResourceByPath
	 */
	/*
	将资源路径解析为文件系统路径。
	注意：即使给定的路径以斜杠开始，也会被解释为相对于当前的VM工作目录。 这与Servlet容器中的语义是一致的。

	这是一个模板方法，是为读取Resource服务的。
	 */
	@Override
	protected Resource getResourceByPath(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		//这里使用文件系统资源对象来定义bean 文件
		return new FileSystemResource(path);
	}

}
