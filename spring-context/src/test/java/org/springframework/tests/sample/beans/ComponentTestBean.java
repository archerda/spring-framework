package org.springframework.tests.sample.beans;

import org.springframework.stereotype.Service;

/**
 * desc
 *
 * @author archerda
 * @date 2018/6/29
 */
@Service
public class ComponentTestBean {

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
