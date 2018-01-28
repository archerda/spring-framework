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

package org.springframework.context;

import java.io.Closeable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.lang.Nullable;

/**
 * SPI interface to be implemented by most if not all application contexts.
 * Provides facilities to configure an application context in addition
 * to the application context client methods in the
 * {@link org.springframework.context.ApplicationContext} interface.
 *
 * <p>Configuration and lifecycle methods are encapsulated here to avoid
 * making them obvious to ApplicationContext client code. The present
 * methods should only be used by startup and shutdown code.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 03.11.2003
 */
/*
SPI接口由大多数（如果不是所有的）应用程序上下文来实现。
除ApplicationContext接口中的应用程序上下文客户端方法之外，还提供了配置应用程序上下文的工具。

配置和生命周期方法封装在这里以避免使它们对ApplicationContext客户端代码显而易见。
目前的方法只能用于启动和关闭代码。
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#setConfigLocation
	 * @see org.springframework.web.context.ContextLoader#CONFIG_LOCATION_PARAM
	 * @see org.springframework.web.servlet.FrameworkServlet#setContextConfigLocation
	 */
	/*
	任何数量的这些字符都被视为单个字符串值中的多个上下文配置路径之间的分隔符。
	 */
	String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	/**
	 * Name of the ConversionService bean in the factory.
	 * If none is supplied, default conversion rules apply.
	 * @see org.springframework.core.convert.ConversionService
	 * @since 3.0
	 */
	/*
	工厂中的ConversionService bean的名称。 如果没有提供，则应用默认转换规则。
	 */
	String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

	/**
	 * Name of the LoadTimeWeaver bean in the factory. If such a bean is supplied,
	 * the context will use a temporary ClassLoader for type matching, in order
	 * to allow the LoadTimeWeaver to process all actual bean classes.
	 * @since 2.5
	 * @see org.springframework.instrument.classloading.LoadTimeWeaver
	 */
	/*
	LoadTimeWeaver bean在工厂中的名称。 如果提供了这样的bean，上下文将使用临时的类加载器进行类型匹配，
	以便允许LoadTimeWeaver处理所有实际的bean类。
	 */
	String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";

	/**
	 * Name of the {@link Environment} bean in the factory.
	 * @since 3.1
	 */
	/*
	工厂中的Environment bean的名称。
	 */
	String ENVIRONMENT_BEAN_NAME = "environment";

	/**
	 * Name of the System properties bean in the factory.
	 * @see java.lang.System#getProperties()
	 */
	/*
	系统属性bean的名称在工厂中。
	 */
	String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

	/**
	 * Name of the System environment bean in the factory.
	 * @see java.lang.System#getenv()
	 */
	/*
	系统环境bean在工厂中的名称。
	 */
	String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";


	/**
	 * Set the unique id of this application context.
	 * @since 3.0
	 */
	/*
	设置此应用程序上下文的唯一标识。
	 */
	void setId(String id);

	/**
	 * Set the parent of this application context.
	 * <p>Note that the parent shouldn't be changed: It should only be set outside
	 * a constructor if it isn't available when an object of this class is created,
	 * for example in case of WebApplicationContext setup.
	 * @param parent the parent context
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext
	 */
	/*
	设置此应用程序上下文的父项。
	请注意，不应更改父级：只有在创建此类的对象时才能将其设置在构造函数之外（例如，在设置WebApplicationContext的情况下）。
	 */
	void setParent(@Nullable ApplicationContext parent);

	/**
	 * Set the {@code Environment} for this application context.
	 * @param environment the new environment
	 * @since 3.1
	 */
	/*
	为此应用程序上下文设置环境。
	 */
	void setEnvironment(ConfigurableEnvironment environment);

	/**
	 * Return the {@code Environment} for this application context in configurable
	 * form, allowing for further customization.
	 * @since 3.1
	 */
	/*
	以可配置的形式返回此应用程序环境的环境，允许进一步定制。
	 */
	@Override
	ConfigurableEnvironment getEnvironment();

	/**
	 * Add a new BeanFactoryPostProcessor that will get applied to the internal
	 * bean factory of this application context on refresh, before any of the
	 * bean definitions get evaluated. To be invoked during context configuration.
	 * @param postProcessor the factory processor to register
	 */
	/*
	添加一个新的BeanFactoryPostProcessor，在对任何bean定义进行求值之前，刷新时将应用于此应用程序上下文的内部bean工厂。 在上下文配置期间被调用。
	 */
	void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

	/**
	 * Add a new ApplicationListener that will be notified on context events
	 * such as context refresh and context shutdown.
	 * <p>Note that any ApplicationListener registered here will be applied
	 * on refresh if the context is not active yet, or on the fly with the
	 * current event multicaster in case of a context that is already active.
	 * @param listener the ApplicationListener to register
	 * @see org.springframework.context.event.ContextRefreshedEvent
	 * @see org.springframework.context.event.ContextClosedEvent
	 */
	/*
	添加一个新的ApplicationListener，它将在上下文事件（如上下文刷新和上下文关闭）上得到通知。
	请注意，如果上下文尚未激活，则在此处注册的任何ApplicationListener都将应用于刷新，或者在上下文已处于活动状态的情况下随时使用当前事件多播器。
	 */
	void addApplicationListener(ApplicationListener<?> listener);

	/**
	 * Register the given protocol resolver with this application context,
	 * allowing for additional resource protocols to be handled.
	 * <p>Any such resolver will be invoked ahead of this context's standard
	 * resolution rules. It may therefore also override any default rules.
	 * @since 4.3
	 */
	/*
	注册给定的协议解析器与这个应用程序上下文，允许额外的资源协议来处理。
	任何这样的解析器都将在本文的标准解析规则之前被调用。 因此它也可以覆盖任何默认规则。
	 */
	void addProtocolResolver(ProtocolResolver resolver);

	/**
	 * Load or refresh the persistent representation of the configuration,
	 * which might an XML file, properties file, or relational database schema.
	 * <p>As this is a startup method, it should destroy already created singletons
	 * if it fails, to avoid dangling resources. In other words, after invocation
	 * of that method, either all or no singletons at all should be instantiated.
	 * @throws BeansException if the bean factory could not be initialized
	 * @throws IllegalStateException if already initialized and multiple refresh
	 * attempts are not supported
	 */
	/*
	加载或刷新配置的持久表示，这可能是XML文件，属性文件或关系数据库模式。
	因为这是一个启动方法，所以它应该销毁已经创建的单例bean，如果它失败，以避免摇摆的资源。 换句话说，在调用该方法之后，全部的或者不是所有的单例都应该被实例化。
	 */
	void refresh() throws BeansException, IllegalStateException;

	/**
	 * Register a shutdown hook with the JVM runtime, closing this context
	 * on JVM shutdown unless it has already been closed at that time.
	 * <p>This method can be called multiple times. Only one shutdown hook
	 * (at max) will be registered for each context instance.
	 * @see java.lang.Runtime#addShutdownHook
	 * @see #close()
	 */
	/*
	使用JVM运行时注册一个关闭钩子，在JVM关闭时关闭此上下文，除非此时已关闭。
	这个方法可以被多次调用。 每个上下文实例只会注册一个关闭钩子（最大）。
	 */
	void registerShutdownHook();

	/**
	 * Close this application context, releasing all resources and locks that the
	 * implementation might hold. This includes destroying all cached singleton beans.
	 * <p>Note: Does <i>not</i> invoke {@code close} on a parent context;
	 * parent contexts have their own, independent lifecycle.
	 * <p>This method can be called multiple times without side effects: Subsequent
	 * {@code close} calls on an already closed context will be ignored.
	 */
	/*
	关闭这个应用程序上下文，释放实现可能持有的所有资源和锁。 这包括销毁所有缓存的单例。
	注意：不在父上下文上调用close; 父上下文有自己独立的生命周期。

	这个方法可以被多次调用，而不会产生副作用：在已经关闭的上下文中的后续关闭调用将被忽略。
	 */
	@Override
	void close();

	/**
	 * Determine whether this application context is active, that is,
	 * whether it has been refreshed at least once and has not been closed yet.
	 * @return whether the context is still active
	 * @see #refresh()
	 * @see #close()
	 * @see #getBeanFactory()
	 */
	/*
	确定此应用程序上下文是否处于活动状态，即是否至少刷新了一次，还没有关闭。
	 */
	boolean isActive();

	/**
	 * Return the internal bean factory of this application context.
	 * Can be used to access specific functionality of the underlying factory.
	 * <p>Note: Do not use this to post-process the bean factory; singletons
	 * will already have been instantiated before. Use a BeanFactoryPostProcessor
	 * to intercept the BeanFactory setup process before beans get touched.
	 * <p>Generally, this internal factory will only be accessible while the context
	 * is active, that is, inbetween {@link #refresh()} and {@link #close()}.
	 * The {@link #isActive()} flag can be used to check whether the context
	 * is in an appropriate state.
	 * @return the underlying bean factory
	 * @throws IllegalStateException if the context does not hold an internal
	 * bean factory (usually if {@link #refresh()} hasn't been called yet or
	 * if {@link #close()} has already been called)
	 * @see #isActive()
	 * @see #refresh()
	 * @see #close()
	 * @see #addBeanFactoryPostProcessor
	 */
	/*
	返回此应用程序上下文的内部bean工厂。 可以用来访问底层工厂的特定功能。
	注意：不要用这个来后处理bean工厂; 单例以前已经被实例化了。 在Bean被触及之前，使用BeanFactoryPostProcessor拦截BeanFactory安装过程。

	一般来说，这个内部工厂只有在上下文处于活动状态时才可以访问，即在refresh（）和close（）之间。 isActive（）标志可用于检查上下文是否处于适当的状态。
	 */
	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
