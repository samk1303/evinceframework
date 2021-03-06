/*
 * Copyright 2012 the original author or authors.
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
package com.evinceframework.data.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.evinceframework.data.Query;
import com.evinceframework.data.QueryParameters;
import com.evinceframework.data.QueryResult;

public class JpaCriteriaQuery<T> implements Query<T> {
	
	private Class<T> dataClass;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public JpaCriteriaQuery(Class<T> dataClass) {
		this.dataClass = dataClass;
	}

	@Override
	public QueryResult<T> execute(QueryParameters params) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		QueryHelper helper = new QueryHelper(builder, params);
		
		// TODO add criteria to the queries
		
		return helper.execute();
	}
	
	private class QueryHelper {
		
		private QueryParameters params;
		
		private CriteriaQuery<Long> countQuery;
		
		private Root<T> countRoot;
		
		private CriteriaQuery<T> query;
		
		Root<T> root;

		public QueryHelper(CriteriaBuilder builder, QueryParameters params) {
			this.params = params;
			countQuery = builder.createQuery(Long.class);
			countRoot = countQuery.from(dataClass);
			countQuery.select(builder.count(countRoot));
			
			query = builder.createQuery(dataClass);
			root = query.from(dataClass);
		
			List<Order> jpaOrders = new ArrayList<Order>();
			for (com.evinceframework.data.Order order : params.getOrder()) {
				if (order.isAscending()) {
					jpaOrders.add(
							builder.asc(getPath(order.getSortField())));
				} else {
					jpaOrders.add(
							builder.desc(getPath(order.getSortField())));
				}
			}
			query.orderBy(jpaOrders);
		}
		
		protected Path<?> getPath(String path) {
			// TODO allow sorting in child objects
			return root.get(path);
		}
		
		public QueryResult<T> execute() {
			
			DefaultQueryResultImpl<T> result = new DefaultQueryResultImpl<T>(params);
			
			TypedQuery<Long> typedCountQuery = entityManager.createQuery(countQuery);
			
			TypedQuery<T> typedQuery = entityManager.createQuery(query);
			
			if (params.getPageSize() != null) {
				
				// this calculates the first item we are looking for
				result.setTotalItems(result.getPageSize() * 2);
				
				typedQuery.setFirstResult(result.getFirstItemIndex());
				typedQuery.setMaxResults(result.getPageSize());
			}

			// TODO reevaluate integer vs long in the query parameter interface
			Long count = typedCountQuery.getSingleResult();
			if (count < new Long(Integer.MAX_VALUE))
				result.setTotalItems(count.intValue());
			
			
			
			result.setItems(typedQuery.getResultList());
			
			return result;
		}
	}
	
	
}
