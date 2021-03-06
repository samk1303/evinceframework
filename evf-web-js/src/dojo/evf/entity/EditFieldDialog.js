/*
 * Copyright 2013 Craig Swing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0<div>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 define([
    'dojo/_base/declare',
	'evf/dialog/SimpleFormDialog',
	'./FieldForm',
	'./serviceFactory',
	'dojo/i18n!./nls/entity',
], function(declare, SimpleFormDialog, FieldForm, serviceFactory, i18n) {

	return declare('evf.entity.EditFieldDialog', [SimpleFormDialog], {
		
		entity: null,

		data: null,

		_setEntityAttr: function(entity) {
			this.entity = entity;
			if(!this._started) return;

			this.set('title', this.dojoLang.replace(i18n.editFieldDialog_title, [this.entity.name]));	
		},

		_setDataAttr: function(data) {
			this.data = data;
			if(!this._started) return;

			this.content.set('field', data);
		},

		startup: function(){
			if(this._started) return;
			this.inherited(arguments);

			this.set('entity', this.entity);
			this.set('data', this.data);
		},

		createContent: function(node) {
			return this.constructWidget(FieldForm, {
				field: this.data
			}, node);
		},

		onSubmit: function(data) {
			var fn = serviceFactory.findService(serviceFactory.updateField);
			return fn && fn(this.entity, 
				this.dojoLang.mixin({'_id': this.data['_id']}, data));
		}
	});
});