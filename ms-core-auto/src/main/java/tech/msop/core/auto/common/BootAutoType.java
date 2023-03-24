/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package tech.msop.core.auto.common;


import tech.msop.core.auto.annotation.*;;

/**
 * 注解类型
 *
 * @author ruozhuliufeng
 */
public enum BootAutoType {
	/**
	 * ApplicationContextInitializer 添加到 spring.factories
	 */
	CONTEXT_INITIALIZER(AutoContextInitializer.class.getName(), "org.springframework.context.ApplicationContextInitializer"),
	/**
	 * ApplicationListener 添加到 spring.factories
	 */
	LISTENER(AutoListener.class.getName(), "org.springframework.context.ApplicationListener"),

	/**
	 * ApplicationRunListener 添加到spring.factories
	 */
	RUN_LISTENER(AutoRunListener.class.getName(),"org.springframework.boot.SpringApplicationRunListener"),
	/**
	 * FailureAnalyzer 添加到 spring.factories
	 */
	FAILURE_ANALYZER(AutoFailureAnalyzer.class.getName(), "org.springframework.boot.diagnostics.FailureAnalyzer"),
	/**
	 * Component 组合注解，添加到 spring.factories
	 */
	COMPONENT("org.springframework.stereotype.Component", "org.springframework.boot.autoconfigure.EnableAutoConfiguration"),
	/**
	 * EnvironmentPostProcessor 添加到spring.factories
	 */
	ENV_POST_PROCESSOR(AutoEnvPostProcessor.class.getName(), "org.springframework.boot.env.EnvironmentPostProcessor"),

	;

	private final String annotationName;
	private final String configureKey;

	BootAutoType(String annotationName, String configureKey) {
		this.annotationName = annotationName;
		this.configureKey = configureKey;
	}

	public final String getAnnotationName() {
		return annotationName;
	}

	public final String getConfigureKey() {
		return configureKey;
	}

}
