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
package com.evinceframework.web.dojo.navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.evinceframework.web.dojo.json.JsonSerializationContext;

public class NavigationCategory extends NavigationItem {

	private List<NavigationItem> items = new ArrayList<NavigationItem>();
	
	public List<NavigationItem> getItems() {
		return items;
	}

	public void setItems(List<NavigationItem> items) {
		this.items = items;
	}

	public static class JsonConverter extends NavigationItem.JsonConverter<NavigationCategory> {

		public JsonConverter() {
			super(NavigationCategory.class);
		}

		@Override
		protected void onWriteObjectProperties(JsonSerializationContext context, NavigationCategory obj) throws IOException {
			super.onWriteObjectProperties(context, obj);
			
			context.writeProperty(Navigator.JsonConverter.ITEMS_PROPERTY, obj.getItems());	
		}
	}
	
}
