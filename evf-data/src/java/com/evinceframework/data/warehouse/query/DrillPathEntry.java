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
package com.evinceframework.data.warehouse.query;

import com.evinceframework.data.warehouse.Dimension;
import com.evinceframework.data.warehouse.DimensionalAttribute;

public class DrillPathEntry<T> {

	private T value;
	
	private Dimension dimension;
	
	private DimensionalAttribute<T> attribute;
	
	private DrillPathEntry<?> root;
	
	private DrillPathEntry<?> previous;
	
	private DrillPathEntry<?> next;
	
	public DrillPathEntry(Dimension dimension, DimensionalAttribute<T> attribute) {
		this(dimension, attribute, null);
	}
	
	public DrillPathEntry(Dimension dimension, DimensionalAttribute<T> attribute, DrillPathEntry<?> previous) {
		this.dimension = dimension;
		this.attribute = attribute;
		this.previous = previous;
		
		if(this.previous != null) {
			this.previous.setNext(this);
			this.root = this.previous.getRootEntry();
		} else {
			this.root = this;
		}
	}

	public Dimension getDimension() {
		return dimension;
	}
	
	public DimensionalAttribute<T> getDimensionalAttribute() {
		return attribute;
	}
	
	public DrillPathEntry<?> getNextEntry() {
		return next;
	}
	
	public DrillPathEntry<?> getPreviousEntry() {
		return previous;
	}
	
	private void setNext(DrillPathEntry<?> next) {
		this.next = next;
	}
	
	public DrillPathEntry<?> getRootEntry() {
		return root;
	}
	
	public T getFilterValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void setFilterValue(Object value) {
		this.value= (T) value;
	}
	
}
