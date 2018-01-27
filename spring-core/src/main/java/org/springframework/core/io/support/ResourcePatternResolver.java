/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.core.io.support;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Strategy interface for resolving a location pattern (for example,
 * an Ant-style path pattern) into Resource objects.
 *
 * 用于将位置模式 (例如, Ant 样式的路径模式) 解析为资源对象的策略接口。
 *
 * <p>This is an extension to the {@link org.springframework.core.io.ResourceLoader}
 * interface. A passed-in ResourceLoader (for example, an
 * {@link org.springframework.context.ApplicationContext} passed in via
 * {@link org.springframework.context.ResourceLoaderAware} when running in a context)
 * can be checked whether it implements this extended interface too.
 *
 * 这是 ResourceLoader 接口的扩展。
 * 通过 ResourceLoader (例如, 在上下文中运行时通过 ResourceLoaderAware 传递的 ApplicationContext)
 * 可以检查它是否也实现了这个扩展接口。
 *
 * <p>{@link PathMatchingResourcePatternResolver} is a standalone implementation
 * that is usable outside an ApplicationContext, also used by
 * {@link ResourceArrayPropertyEditor} for populating Resource array bean properties.
 *
 * PathMatchingResourcePatternResolver 是一个独立的实现,
 * 可在 ApplicationContext 之外使用, ResourceArrayPropertyEditor 也用于填充资源数组 bean 属性。
 *
 * <p>Can be used with any sort of location pattern (e.g. "/WEB-INF/*-context.xml"):
 * Input patterns have to match the strategy implementation. This interface just
 * specifies the conversion method rather than a specific pattern format.
 *
 * 可用于任何类型的位置模式 (例如 "/WEB-INF/*-context.xml"): 输入模式必须与策略实现相匹配。
 * 此接口只指定转换方法, 而不是特定的模式格式。
 *
 * <p>This interface also suggests a new resource prefix "classpath*:" for all
 * matching resources from the class path. Note that the resource location is
 * expected to be a path without placeholders in this case (e.g. "/beans.xml");
 * JAR files or classes directories can contain multiple files of the same name.
 *
 * 此接口还为类路径中的所有匹配资源建议了一个新的资源前缀 "classpath*:"。
 * 请注意, 在这种情况下, 资源位置将是没有占位符的路径 (例如 "/beans.xml");JAR 文件或类目录可以包含多个同名文件。
 *
 * @author Juergen Hoeller
 * @since 1.0.2
 * @see org.springframework.core.io.Resource
 * @see org.springframework.core.io.ResourceLoader
 * @see org.springframework.context.ApplicationContext
 * @see org.springframework.context.ResourceLoaderAware
 */
public interface ResourcePatternResolver extends ResourceLoader {

	/**
	 * Pseudo URL prefix for all matching resources from the class path: "classpath*:"
	 * This differs from ResourceLoader's classpath URL prefix in that it
	 * retrieves all matching resources for a given name (e.g. "/beans.xml"),
	 * for example in the root of all deployed JAR files.
	 * @see org.springframework.core.io.ResourceLoader#CLASSPATH_URL_PREFIX
	 */
	/*
	从类路径中的所有匹配资源的伪 URL 前缀: "classpath*:" 这与 ResourceLoader 的类路径 URL 前缀不同, ResourceLoader只检索单个资源
	而 ResourcePatternResolver 检索给定名称的所有匹配资源 (例如 "/beans.xml"), 例如在所有已部署 JAR 的根中文件.
	 */
	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

	/**
	 * Resolve the given location pattern into Resource objects.
	 * <p>Overlapping resource entries that point to the same physical
	 * resource should be avoided, as far as possible. The result should
	 * have set semantics.
	 * @param locationPattern the location pattern to resolve
	 * @return the corresponding Resource objects
	 * @throws IOException in case of I/O errors
	 */
	/*
	将给定的位置模式解析为资源对象。
	应尽可能避免指向同一物理资源的重叠资源条目。结果应设置语义。
	 */
	Resource[] getResources(String locationPattern) throws IOException;

}
