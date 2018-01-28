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

package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link org.springframework.beans.factory.BeanFactory}
 * interface.
 *
 * <p>This bean factory interface is not meant to be used in normal application
 * code: Stick to {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * needs. This extended interface is just meant to allow for framework-internal
 * plug'n'play and for special access to bean factory configuration methods.
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */
/*
配置接口由大多数bean工厂来实现。 除BeanFactory接口中的Bean工厂客户端方法外，还提供了配置Bean工厂的工具。

这个bean的工厂接口并不意味着在正常的应用程序代码中使用：对于典型需求，坚持BeanFactory或ListableBeanFactory。
这个扩展的接口只是为了允许框架内部的插件显示以及对bean工厂配置方法的特殊访问。
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 */
	/*
	标准单例作用域的作用域标识符：“singleton”。 自定义范围可以通过registerScope添加。
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 */
	/*
	标准原型范围的范围标识符：“prototype”。 自定义范围可以通过registerScope添加。
	 */
	String SCOPE_PROTOTYPE = "prototype";


	/**
	 * Set the parent of this bean factory.
	 * <p>Note that the parent cannot be changed: It should only be set outside
	 * a constructor if it isn't available at the time of factory instantiation.
	 * @param parentBeanFactory the parent BeanFactory
	 * @throws IllegalStateException if this factory is already associated with
	 * a parent BeanFactory
	 * @see #getParentBeanFactory()
	 */
	/*
	设置这个bean工厂的父项。
	请注意，父级不能更改：如果在工厂实例化时它不可用，则只能在构造函数之外进行设置。
	 */
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	/**
	 * Set the class loader to use for loading bean classes.
	 * Default is the thread context class loader.
	 * <p>Note that this class loader will only apply to bean definitions
	 * that do not carry a resolved bean class yet. This is the case as of
	 * Spring 2.0 by default: Bean definitions only carry bean class names,
	 * to be resolved once the factory processes the bean definition.
	 * @param beanClassLoader the class loader to use,
	 * or {@code null} to suggest the default class loader
	 */
	/*
	设置类加载器用于加载Bean类。 默认是线程上下文类加载器。
	请注意，这个类加载器将仅适用于尚未包含已解析的bean类的bean定义。
	默认情况下，Spring 2.0就是这种情况：Bean定义只承载bean类的名字，一旦工厂处理了bean的定义，就会被解析。
	 */
	void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

	/**
	 * Return this factory's class loader for loading bean classes.
	 */
	/*
	返回这个工厂的类加载器来加载bean类。
	 */
	@Nullable
	ClassLoader getBeanClassLoader();

	/**
	 * Specify a temporary ClassLoader to use for type matching purposes.
	 * Default is none, simply using the standard bean ClassLoader.
	 * <p>A temporary ClassLoader is usually just specified if
	 * <i>load-time weaving</i> is involved, to make sure that actual bean
	 * classes are loaded as lazily as possible. The temporary loader is
	 * then removed once the BeanFactory completes its bootstrap phase.
	 * @since 2.5
	 */
	/*
	指定一个临时ClassLoader用于类型匹配。 默认是none，只需使用标准的Bean ClassLoader即可。
	一个临时的ClassLoader通常只在涉及到加载时织入的时候被指定，以确保实际的bean类被尽可能的延迟加载。
	一旦BeanFactory完成引导阶段，临时加载器就会被删除。
	 */
	void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

	/**
	 * Return the temporary ClassLoader to use for type matching purposes,
	 * if any.
	 * @since 2.5
	 */
	/*
	如果有的话，返回临时的ClassLoader用于类型匹配的目的。
	 */
	@Nullable
	ClassLoader getTempClassLoader();

	/**
	 * Set whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes. Default is on.
	 * <p>Turn this flag off to enable hot-refreshing of bean definition objects
	 * and in particular bean classes. If this flag is off, any creation of a bean
	 * instance will re-query the bean class loader for newly resolved classes.
	 */
	/*
	设置是否缓存bean元数据，如给定的bean定义（以合并的方式）和已解析的bean类。 默认打开。
	关闭此标志以启用对bean定义对象的热刷新，特别是bean类。 如果此标志已关闭，则任何创建的bean实例都将重新查询用于新解析的类的Bean类加载器。
	 */
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	/**
	 * Return whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes.
	 */
	/*
	返回是否缓存bean元数据，如给定的bean定义（以合并方式）和已解析的bean类。
	 */
	boolean isCacheBeanMetadata();

	/**
	 * Specify the resolution strategy for expressions in bean definition values.
	 * <p>There is no expression support active in a BeanFactory by default.
	 * An ApplicationContext will typically set a standard expression strategy
	 * here, supporting "#{...}" expressions in a Unified EL compatible style.
	 * @since 3.0
	 */
	/*
	为bean定义值中的表达式指定解析策略。
	在默认情况下，BeanFactory中没有活动的表达式支持。
	ApplicationContext通常会在这里设置一个标准表达式策略，在Unified EL兼容风格中支持“＃{...}”表达式。
	 */
	void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 * @since 3.0
	 */
	/*
	返回bean定义值中表达式的解析策略。
	 */
	@Nullable
	BeanExpressionResolver getBeanExpressionResolver();

	/**
	 * Specify a Spring 3.0 ConversionService to use for converting
	 * property values, as an alternative to JavaBeans PropertyEditors.
	 * @since 3.0
	 */
	/*
	指定一个Spring 3.0 ConversionService用于转换属性值，作为JavaBean PropertyEditor的替代方法。
	 */
	void setConversionService(@Nullable ConversionService conversionService);

	/**
	 * Return the associated ConversionService, if any.
	 * @since 3.0
	 */
	/*
	返回关联的ConversionService（如果有的话）。
	 */
	@Nullable
	ConversionService getConversionService();

	/**
	 * Add a PropertyEditorRegistrar to be applied to all bean creation processes.
	 * <p>Such a registrar creates new PropertyEditor instances and registers them
	 * on the given registry, fresh for each bean creation attempt. This avoids
	 * the need for synchronization on custom editors; hence, it is generally
	 * preferable to use this method instead of {@link #registerCustomEditor}.
	 * @param registrar the PropertyEditorRegistrar to register
	 */
	/*
	添加一个PropertyEditorRegistrar以应用于所有的bean创建过程。
	这样的注册商创建新的PropertyEditor实例，并将它们注册到给定的注册表中，对于每个bean创建尝试都是新的。
	这避免了在定制编辑器上进行同步的需要;
	因此，通常最好使用此方法而不是registerCustomEditor（java.lang.Class <？>，java.lang.Class <？extends java.beans.PropertyEditor>）。
	 */
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	/**
	 * Register the given custom property editor for all properties of the
	 * given type. To be invoked during factory configuration.
	 * <p>Note that this method will register a shared custom editor instance;
	 * access to that instance will be synchronized for thread-safety. It is
	 * generally preferable to use {@link #addPropertyEditorRegistrar} instead
	 * of this method, to avoid for the need for synchronization on custom editors.
	 * @param requiredType type of the property
	 * @param propertyEditorClass the {@link PropertyEditor} class to register
	 */
	/*
	为给定类型的所有属性注册给定的定制属性编辑器。 在工厂配置期间被调用。
	请注意，此方法将注册一个共享的自定义编辑器实例; 对该实例的访问将被同步用于线程安全。
	通常最好使用addPropertyEditorRegistrar（org.springframework.beans.PropertyEditorRegistrar）来代替这个方法，以避免在自定义编辑器上需要同步。
	 */
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	/**
	 * Initialize the given PropertyEditorRegistry with the custom editors
	 * that have been registered with this BeanFactory.
	 * @param registry the PropertyEditorRegistry to initialize
	 */
	/*
	用已经注册到这个BeanFactory的自定义编辑器初始化给定的PropertyEditorRegistry。
	 */
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	/**
	 * Set a custom type converter that this BeanFactory should use for converting
	 * bean property values, constructor argument values, etc.
	 * <p>This will override the default PropertyEditor mechanism and hence make
	 * any custom editors or custom editor registrars irrelevant.
	 * @see #addPropertyEditorRegistrar
	 * @see #registerCustomEditor
	 * @since 2.5
	 */
	/*
	设置一个自定义的类型转换器，这个BeanFactory应该用来转换bean属性值，构造函数参数值等。
	这将覆盖默认的PropertyEditor机制，从而使任何自定义编辑器或自定义编辑器注册表不相关。
	 */
	void setTypeConverter(TypeConverter typeConverter);

	/**
	 * Obtain a type converter as used by this BeanFactory. This may be a fresh
	 * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
	 * <p>If the default PropertyEditor mechanism is active, the returned
	 * TypeConverter will be aware of all custom editors that have been registered.
	 * @since 2.5
	 */
	/*
	获取这个BeanFactory使用的类型转换器。 这可能是每个调用的新实例，因为TypeConverters通常不是线程安全的。
	如果默认的PropertyEditor机制处于活动状态，则返回的TypeConverter将知道所有已经注册的自定义编辑器。
	 */
	TypeConverter getTypeConverter();

	/**
	 * Add a String resolver for embedded values such as annotation attributes.
	 * @param valueResolver the String resolver to apply to embedded values
	 * @since 3.0
	 */
	/*
	添加一个字符串解析器，用于嵌入值，如注解属性。
	 */
	void addEmbeddedValueResolver(StringValueResolver valueResolver);

	/**
	 * Determine whether an embedded value resolver has been registered with this
	 * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
	 * @since 4.3
	 */
	/*
	确定嵌入式值解析器是否已经注册到此bean工厂，通过resolveEmbeddedValue（String）应用。
	 */
	boolean hasEmbeddedValueResolver();

	/**
	 * Resolve the given embedded value, e.g. an annotation attribute.
	 * @param value the value to resolve
	 * @return the resolved value (may be the original value as-is)
	 * @since 3.0
	 */
	/*
	解析给定的嵌入值，例如 一个注解属性。
	 */
	@Nullable
	String resolveEmbeddedValue(String value);

	/**
	 * Add a new BeanPostProcessor that will get applied to beans created
	 * by this factory. To be invoked during factory configuration.
	 * <p>Note: Post-processors submitted here will be applied in the order of
	 * registration; any ordering semantics expressed through implementing the
	 * {@link org.springframework.core.Ordered} interface will be ignored. Note
	 * that autodetected post-processors (e.g. as beans in an ApplicationContext)
	 * will always be applied after programmatically registered ones.
	 * @param beanPostProcessor the post-processor to register
	 */
	/*
	添加一个新的BeanPostProcessor，将被应用到这个工厂创建的bean。 在工厂配置期间被调用。
	注：此处提交的后处理程序将按照注册顺序进行执行; 任何通过实现Ordered接口表达的排序语义都将被忽略。
	请注意，自动检测后处理器（例如ApplicationContext中的Bean）将始终在以编程方式注册的后处理器上应用。
	 */
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	/**
	 * Return the current number of registered BeanPostProcessors, if any.
	 */
	/*
	返回已注册BeanPostProcessor的当前数量，如果有的话。
	 */
	int getBeanPostProcessorCount();

	/**
	 * Register the given scope, backed by the given Scope implementation.
	 * @param scopeName the scope identifier
	 * @param scope the backing Scope implementation
	 */
	/*
	注册给定的范围，由给定的范围实现支持。
	 */
	void registerScope(String scopeName, Scope scope);

	/**
	 * Return the names of all currently registered scopes.
	 * <p>This will only return the names of explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * @return the array of scope names, or an empty array if none
	 * @see #registerScope
	 */
	/*
	返回所有当前注册的范围的名称。
	这只会返回显式注册的作用域名称。 内置的范围，如“单身”和“原型”不会暴露。
	 */
	String[] getRegisteredScopeNames();

	/**
	 * Return the Scope implementation for the given scope name, if any.
	 * <p>This will only return explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * @param scopeName the name of the scope
	 * @return the registered Scope implementation, or {@code null} if none
	 * @see #registerScope
	 */
	/*
	返回给定范围名称的范围实现（如果有的话）。
	这只会返回显式注册的范围。 内置的范围，如“单身”和“原型”不会暴露。
	 */
	@Nullable
	Scope getRegisteredScope(String scopeName);

	/**
	 * Provides a security access control context relevant to this factory.
	 * @return the applicable AccessControlContext (never {@code null})
	 * @since 3.0
	 */
	/*
	提供与此工厂相关的安全访问控制上下文。
	 */
	AccessControlContext getAccessControlContext();

	/**
	 * Copy all relevant configuration from the given other factory.
	 * <p>Should include all standard configuration settings as well as
	 * BeanPostProcessors, Scopes, and factory-specific internal settings.
	 * Should not include any metadata of actual bean definitions,
	 * such as BeanDefinition objects and bean name aliases.
	 * @param otherFactory the other BeanFactory to copy from
	 */
	/*
	复制给定的其他工厂的所有相关配置。
	应包含所有标准配置设置以及BeanPostProcessors，Scopes和工厂特定的内部设置。
	不应包含实际bean定义的任何元数据，例如BeanDefinition对象和bean名称别名。
	 */
	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	/**
	 * Given a bean name, create an alias. We typically use this method to
	 * support names that are illegal within XML ids (used for bean names).
	 * <p>Typically invoked during factory configuration, but can also be
	 * used for runtime registration of aliases. Therefore, a factory
	 * implementation should synchronize alias access.
	 * @param beanName the canonical name of the target bean
	 * @param alias the alias to be registered for the bean
	 * @throws BeanDefinitionStoreException if the alias is already in use
	 */
	/*
	给定一个bean名称，创建一个别名。 我们通常使用这种方法来支持XML ID（用于bean名称）内的非法名称。
	通常在工厂配置期间调用，但也可以用于运行时注册别名。 因此，工厂实现应该同步别名访问。
	 */
	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	/**
	 * Resolve all alias target names and aliases registered in this
	 * factory, applying the given StringValueResolver to them.
	 * <p>The value resolver may for example resolve placeholders
	 * in target bean names and even in alias names.
	 * @param valueResolver the StringValueResolver to apply
	 * @since 2.5
	 */
	/*
	解析在该工厂中注册的所有别名目标名称和别名，并将给定的StringValueResolver应用于它们。
	值解析器可以例如解析目标bean名称中的占位符，甚至可以解析别名。
	 */
	void resolveAliases(StringValueResolver valueResolver);

	/**
	 * Return a merged BeanDefinition for the given bean name,
	 * merging a child bean definition with its parent if necessary.
	 * Considers bean definitions in ancestor factories as well.
	 * @param beanName the name of the bean to retrieve the merged definition for
	 * @return a (potentially merged) BeanDefinition for the given bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition with the given name
	 * @since 2.5
	 */
	/*
	为给定的bean名称返回一个合并的BeanDefinition，如果需要的话，合并一个子bean定义和它的父代。 也考虑祖先工厂中的bean定义。
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Determine whether the bean with the given name is a FactoryBean.
	 * @param name the name of the bean to check
	 * @return whether the bean is a FactoryBean
	 * ({@code false} means the bean exists but is not a FactoryBean)
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.5
	 */
	/*
	确定具有给定名称的bean是否是FactoryBean。
	 */
	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Explicitly control the current in-creation status of the specified bean.
	 * For container-internal use only.
	 * @param beanName the name of the bean
	 * @param inCreation whether the bean is currently in creation
	 * @since 3.1
	 */
	/*
	显式控制指定bean的当前创建状态。 仅供容器内部使用。
	 */
	void setCurrentlyInCreation(String beanName, boolean inCreation);

	/**
	 * Determine whether the specified bean is currently in creation.
	 * @param beanName the name of the bean
	 * @return whether the bean is currently in creation
	 * @since 2.5
	 */
	/*
	确定指定的bean是否正在创建。
	 */
	boolean isCurrentlyInCreation(String beanName);

	/**
	 * Register a dependent bean for the given bean,
	 * to be destroyed before the given bean is destroyed.
	 * @param beanName the name of the bean
	 * @param dependentBeanName the name of the dependent bean
	 * @since 2.5
	 */
	/*
	为给定的bean注册一个依赖的bean，在给定的bean销毁之前被销毁。
	 */
	void registerDependentBean(String beanName, String dependentBeanName);

	/**
	 * Return the names of all beans which depend on the specified bean, if any.
	 * @param beanName the name of the bean
	 * @return the array of dependent bean names, or an empty array if none
	 * @since 2.5
	 */
	/*
	返回依赖于指定bean的所有bean的名字，如果有的话。
	 */
	String[] getDependentBeans(String beanName);

	/**
	 * Return the names of all beans that the specified bean depends on, if any.
	 * @param beanName the name of the bean
	 * @return the array of names of beans which the bean depends on,
	 * or an empty array if none
	 * @since 2.5
	 */
	/*
	返回指定的bean所依赖的所有bean的名字，如果有的话。
	 */
	String[] getDependenciesForBean(String beanName);

	/**
	 * Destroy the given bean instance (usually a prototype instance
	 * obtained from this factory) according to its bean definition.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * @param beanName the name of the bean definition
	 * @param beanInstance the bean instance to destroy
	 */
	/*
	根据bean的定义销毁给定的bean实例（通常是从这个工厂获得的原型实例）。
	在销毁期间出现的任何异常应该被捕获并记录，而不是传播给这个方法的调用者。
	 */
	void destroyBean(String beanName, Object beanInstance);

	/**
	 * Destroy the specified scoped bean in the current target scope, if any.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * @param beanName the name of the scoped bean
	 */
	/*
	销毁当前目标作用域中指定的作用域bean（如果有的话）。
	在销毁期间出现的任何异常应该被捕获并记录，而不是传播给这个方法的调用者。
	 */
	void destroyScopedBean(String beanName);

	/**
	 * Destroy all singleton beans in this factory, including inner beans that have
	 * been registered as disposable. To be called on shutdown of a factory.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 */
	/*
	销毁在这个工厂的所有单例bean，包括注册为一次性的内部bean。 在工厂关闭时被调用。
	在销毁期间出现的任何异常应该被捕获并记录，而不是传播给这个方法的调用者。
	 */
	void destroySingletons();

}
