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
	'evf/security' 	
], function(security) {

	var exports = {
		
		// generic authenticated user
		authenticatedUser: 	{ key: 'USER', description: '' },

		// generic authenticated user
		anonymousUser: 	{ key: 'ANONYMOUS', description: '' },

		// roles for anonymous users
		authenticate: 	{ key: 'evf.membership.standardAuthentication', description: '' },
		rememberMe: 	{ key: 'evf.membership.rememberMe', description: '' },
		register: 		{ key: 'evf.membership.register', description: '' },
		resetPassword: 	{ key: 'evf.membership.resetPassword', description: '' },

		// roles for authenticated users
		viewProfile: 	{ key: 'evf.membership.viewProfile', description: '' },
		logout: 		{ key: 'evf.membership.logout', description: '' }
	};

	security.registerRoles(exports);

	return exports;
});