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

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * 一个BeanDefinition描述了一个bean实例，它有属性值、构造器值，还有更多具体实现提供的信息。
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *
 * 这是最小的一个接口：主要的意图是允许一个 BeanFactoryPostProcessor(比如 PropertyPlaceholderConfigurer)
 * 去检视和修改属性值和其他的一些bean元数据。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
/*
BeanDefinition 是一个接口，在Spring中存在3种实现：RootBeanDefinition、ChildBeanDefinition 和 GenericBeanDefinition。
3个实现都继承了 AbstractBeanDefinition， 其中 BeanDefinition 是配置文件<bean>元素标签在容器中的内部表现形式。
<bean>标签拥有class、scope、lazy-init等配置属性，BeanDefinition则提供了相应的 beanClass、scope、lazyInit的属性，
BeanDefinition和<bean>中的属性是一一对应的。其中 RootBeanDefinition 是最常用的实现类，它对应一般性的<bean>标签，
GenericBeanDefinition 是自2.5版本后新加入的，是一站式服务类。

BeanDefinition就是对依赖反转模式中管理的对象依赖关系的数据抽象，也是容器实现依赖反转功能的核心数据结构，依赖反转功能都是围绕对这个BeanDefinition
的处理来完成的。
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	/*
	单例作用域的作用域标识符。
	请注意，扩展的bean工厂可能支持更多的作用域。
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	/*
	原型作用域的标识符。
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 */
	/*
	表明一个 BeanDefinition 是应用程序的主要部分。通常对应于用户自定义的bean。
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 */
	/*
	表明一个 BeanDefinition 是一些大型配置的支持部分，通常对应于外部的 ComponentDefinition。
    SUPPORT bean被认为是非常重要的，特别是需要查看特定ComponentDefinition的时候，而在查看应用程序的整体配置时则不需要。
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 */
	/*
	infrastructure: 基础设施；
	表明一个BeanDefinition是提供一个完全后台的角色，对终端用户来说是透明。
	这个提示用于一个注册的bean，它完全是内部工作的一部分。
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 */
	/*
	设置父级bean的名字，如果有的话。
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 */
	/*
	返回父级bean的名字，如果有的话。
	 */
	@Nullable
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	/*
	指定这个bean定义的bean类名称。
	在bean工厂的 post-processing过程中，可以修改这个类名，通常用它的解析变体来取代它的类名。
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	/*
	获取这个bean定义当前的bean类名。
	请注意，这不一定是在运行时使用的实际类名，如果发生子定义覆盖/继承父类的类名的话。
	另外，这可能只是调用工厂方法的类，或者在调用方法的工厂bean引用的情况下，它甚至可能是空的。
	因此，不要认为这是在运行时定义的bean类型，而只是将它用于单个bean定义级别的解析目的。
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	/*
	设置bean定义的作用域。
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 */
	/*
	返回这个bean当前的作用域，如果不确定的话则返回null。
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	/*
	设置这个bean是否是懒加载的。
	如果设置为false，那么bean将在工厂启动的时候初始化这个单例的bean。
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 */
	/*
	返回这个bean是否应该被懒加载，比如，不应该在启动的时候进行饿汉式加载。
	只有单例实例才可用。
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 */
	/*
	设置这个bean初始化的时候需要依赖的bean的名字。
	工厂将会保证那些bean会先被初始化。
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 */
	/*
	返回这个bean依赖的bean的名称列表。
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 */
	/*
	设置一个bean是否是其他bean自动注入时的候选者。
	请注意，这个标志只对 byType 类型的自动注入有效。
	它对 byName 类型的自动注入是无效的，因为它仍然会去解析，即使那个bean没有被声明为候选者。
	总之，byName 类型的自动注入无论如何都会注入这个bean，只要名字匹配的话。
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 */
	/*
	返回这个bean是否是候选者，当自动注入到其他bean的时候。
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 */
	/*
	设置这个bean是否是最主要的候选者。
	如果设置的值是true的话，如果有多个匹配的候选者的话，这个bean将会被选中。
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 */
	/*
	返回这个bean是否是最重要的候选者。
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 */
	/*
	显式指定使用的工厂bean，如果有的话。
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * Return the factory bean name, if any.
	 */
	/*
	返回工厂bean的名称，如果有的话。
	 */
	@Nullable
	String getFactoryBeanName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 */
	/*
	设置工厂方法的名称，如果有的话。
	这个方法将会带着构造参数或者无参数被调用。
	这个方法将会被指定的工厂bean调用，如果有的话，或者作为本地bean类的静态方法。
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/**
	 * Return a factory method, if any.
	 */
	/*
	返回一个工厂方法，如果有的话。
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	/*
	返回这个bean的构造参数的值。
	这个返回的实例可以在bean工厂的 post-processing 阶段中被修改。
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return if there are constructor argument values defined for this bean.
	 * @since 5.0.2
	 */
	/*
	返回这个bean的构造方法上是不是有值。
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 */
	/*
	返回可以应用到这个bean新实例的属性值。
	这个返回的实例可以在bean工厂的 post-processing 阶段中被修改。
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * Return if there are property values values defined for this bean.
	 * @since 5.0.2
	 */
	/*
	返回这个bean是否有属性值。
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}


	// Read-only attributes

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 */
	/*
	返回这个bean是否是单例的。
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 */
	/*
	返回这个bean是否是原型的。
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 */
	/*
	返回这个bean是否是抽象的，如果是，则意味着它不会被初始化。
	 */
	boolean isAbstract();

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	/*
	获取这个bean的角色提示。
	 */
	int getRole();

	/**
	 * Return a human-readable description of this bean definition.
	 */
	/*
	返回这个bean的可读性高的描述。
	 */
	@Nullable
	String getDescription();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 */
	/*
	返回这个bean被定义的那个资源文件的描述。（主要是为了在错误日志中展现）
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 */
	/*
	获取来源的bean，如果没有的话则返回null。
	允许检索被装饰的bean，如果有的话。
	请注意，这个方法返回最近的起源者。通过这个起源者进行迭代，可以获取用户定义的最早起源的bean。
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
