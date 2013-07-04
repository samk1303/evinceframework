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
package com.evinceframework.data.warehouse.query.impl;

import com.evinceframework.data.warehouse.Dimension;
import com.evinceframework.data.warehouse.query.DimensionCriterion;

public class DimensionCriterionImpl extends CriterionSupport<Integer> implements DimensionCriterion {

	private Dimension dimension;
	
	public DimensionCriterionImpl(Dimension dimension, Integer identifier) {
		super(Integer.class, new Integer[]{ identifier });
		this.dimension = dimension;
	}

	@Override
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public boolean requiresJoinOnDimensionTable() {
		return false;
	}
	
	@Override
	public String createWhereFragment(String factTableAlias, String dimensionTableAlias) {
		return String.format(createWhereFormat(), 
				String.format("%s.%s", factTableAlias, dimension.getForeignKeyColumn()));
	}

}
