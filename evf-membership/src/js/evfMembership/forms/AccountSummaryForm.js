/*
* Copyright 2013 the original author or authors.
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
define([
	'require',
	'dojo/_base/declare', 
	'dijit/_TemplatedMixin',
	'dijit/form/Button',
	'evf/ComplexWidget',
	'../roles',
	'../topics',
	'dojo/text!./templates/AccountSummaryForm.html',
	'dojo/i18n!../nls/membership'
], function(require, declare, Templated, Button, ComplexWidget, roles, topics, template, i18n){

	return declare('evfMembership.forms.AccountSummaryForm', [ComplexWidget, Templated], {

		templateString: template,

		postMixInProperties: function() {
			this.inherited(arguments);
			this.name = this.dojoConfig.user.name;
			this.login = this.dojoConfig.user.login;
			this.avatarSrc = this.dojoConfig.user.avatar 
				|| this.dojoConfig.noImage 
				|| require.toUrl('evf/resources/noImage.png');
		},

		postCreate: function(){
			this.inherited(arguments);

			if(this.hasSecurityRole(roles.viewProfile)) {
				var profileBtn = this.constructWidget(Button, {
					label: i18n.viewProfileAction
				}, this.viewProfileNode);
					this.listen(profileBtn, 'click', function() {
					this.publish(topics.viewProfile, {});
				});
			}

			if(this.hasSecurityRole(roles.logout)) {
				var logoutBtn = this.constructWidget(Button, {
					label: i18n.logoutAction
				}, this.logoffNode);
				this.listen(logoutBtn, 'click', function() {
					this.publish(topics.logout, {});
				});
			}
		}

	});
});