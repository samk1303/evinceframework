/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evinceframework.data.web.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.evinceframework.core.factory.AbstractCachingLookupFactory;
import com.evinceframework.data.warehouse.query.Query;

/**
 * Resolves an http request to different implementations of the {@link Query} interface.  This class will
 * delegate to implementations of the {@link WebQueryResolver}.
 * 
 * @author Craig Swing
 */
public class QueryMethodArgumentResolver
		extends AbstractCachingLookupFactory<String, WebQueryResolver<?>>
		implements HandlerMethodArgumentResolver, BeanFactoryAware, InitializingBean {

	private BeanFactory beanFactory;
	
	private ConversionService conversionService;
	
	private List<WebQueryResolver<?>> queryResolvers = new ArrayList<WebQueryResolver<?>>();
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		queryResolvers.add(new HierarchicalQueryJsonResolver(beanFactory, conversionService));
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return ClassUtils.isAssignable(parameter.getParameterType(), Query.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		String key = webRequest.getParameter("queryType");
		if(!StringUtils.hasLength(key))
			return null;
		
		WebQueryResolver<?> resolver = lookup(key);
		if(resolver == null)
			return null;
		
		return resolver.create(parameter, mavContainer, webRequest, binderFactory);
	}

	@Override
	protected WebQueryResolver<?> findImplementation(String lookup) {
		for(WebQueryResolver<?> resolver : queryResolvers) {
			if(StringUtils.hasLength(lookup) && lookup.equals(resolver.getResolverKey())) {
				return resolver;
			}
		}
		return null;
	}
	
}
