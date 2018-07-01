package org.springframework.tests.sample.beans;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * desc
 *
 * @author archerda
 * @date 2018/6/29
 */
@Service
public class ComponentTestBean {

	@Resource
	// @Autowired
	private Object beanWithObjectPropertyA;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AutowiredTestBean{" +
				"name='" + name + '\'' +
				'}';
	}
}
