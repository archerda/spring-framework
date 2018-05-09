/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.transaction.annotation;

import org.springframework.transaction.TransactionDefinition;

/**
 * Enumeration that represents transaction propagation behaviors for use
 * with the {@link Transactional} annotation, corresponding to the
 * {@link TransactionDefinition} interface.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @since 1.2
 */
public enum Propagation {

	/**
	 * Support a current transaction, create a new one if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p>This is the default setting of a transaction annotation.
	 */
	/*
	支持一个当前的事务,如果事务不存在的话就创建一个新的事务.
	类似同名的EJB事务属性.
	这是事务注解的默认值.
	 */
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),

	/**
	 * Support a current transaction, execute non-transactionally if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p>Note: For transaction managers with transaction synchronization,
	 * PROPAGATION_SUPPORTS is slightly different from no transaction at all,
	 * as it defines a transaction scope that synchronization will apply for.
	 * As a consequence, the same resources (JDBC Connection, Hibernate Session, etc)
	 * will be shared for the entire specified scope. Note that this depends on
	 * the actual synchronization configuration of the transaction manager.
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setTransactionSynchronization
	 */
	/*
	支持当前的事务,如果当前不存在事务,则以非事务的方式执行.
	类似同名的EJB事务属性.
	注意:对于有事务同步的事务管理器来说,SUPPORTS跟完全没有事务是略有不同的,因为它定义了同步使用的事务范围.
	因此，相同的资源（JDBC连接，Hibernate会话等）将为整个指定范围共享.请注意,这取决于事务管理器的实际同步配置。
	 */
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),

	/**
	 * Support a current transaction, throw an exception if none exists.
	 * Analogous to EJB transaction attribute of the same name.
	 */
	/*
	支持当前事务,如果当前没有事务,则抛出一个异常;
	类似同名的EJB事务属性.
	 */
	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),

	/**
	 * Create a new transaction, and suspend the current transaction if one exists.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	/*
	创建一个新的事务,如果已经存在一个事务了则把它挂起.
	类似同名的EJB事务属性.
	请注意:实际的事务挂起并不是在所有的事务管理器中都会生效.
	这是特别应用于 org.springframework.transaction.jta.JtaTransactionManager 的,
	它要求传递给它的 javax.transaction.TransactionManager 一起启用了这个特性(这是标准的JavaEE服务器中特定的).
	// TODO by archerda on 2018/4/17. 这里不一定生效没理解.
	 */
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),

	/**
	 * Execute non-transactionally, suspend the current transaction if one exists.
	 * Analogous to EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the {@code javax.transaction.TransactionManager} to be
	 * made available it to it (which is server-specific in standard Java EE).
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	/*
	以非事务的方式执行,如果当前有事务了则挂起这个事务.
	类似同名的EJB事务属性.
	请注意:实际的事务挂起并不是在所有的事务管理器中都会生效.
	这是特别应用于 org.springframework.transaction.jta.JtaTransactionManager 的,
	它要求传递给它的 javax.transaction.TransactionManager 一起启用了这个特性(这是标准的JavaEE服务器中特定的).
	// TODO by archerda on 2018/4/17. 这里不一定生效没理解.
	 */
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),

	/**
	 * Execute non-transactionally, throw an exception if a transaction exists.
	 * Analogous to EJB transaction attribute of the same name.
	 */
	/*
	以非事务方式执行,如果当前已经存在一个事务了则抛出异常.
	类似同名的EJB事务属性.
	 */
	NEVER(TransactionDefinition.PROPAGATION_NEVER),

	/**
	 * Execute within a nested transaction if a current transaction exists,
	 * behave like PROPAGATION_REQUIRED else. There is no analogous feature in EJB.
	 * <p>Note: Actual creation of a nested transaction will only work on specific
	 * transaction managers. Out of the box, this only applies to the JDBC
	 * DataSourceTransactionManager when working on a JDBC 3.0 driver.
	 * Some JTA providers might support nested transactions as well.
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	/*
	如果当前存在事务了则在嵌套的事务中执行,如果没有事务则相当于 PROPAGATION_REQUIRED.
	在EJB中没有类似的特性.
	注意: 实际上嵌套事务的创建只在特定的事务管理器中有效. 这仅仅适用于 JDBC 的 DataSourceTransactionManager,
	而且要在JDBC3.0以上的驱动.
	一些JTA 也提供了对这个特性的支持.
	 */
	NESTED(TransactionDefinition.PROPAGATION_NESTED);


	private final int value;


	Propagation(int value) { this.value = value; }

	public int value() { return this.value; }

}
