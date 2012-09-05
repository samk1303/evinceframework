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
 define("evf/data/Pager", [
	"dojo", "dijit", "dojo/dom-construct",
	"dijit/_Widget", "dijit/_TemplatedMixin",
	"dojo/i18n", "dojo/i18n!./nls/pager"
], function(dojo, dijit, domConstruct, Widget, Template) {

return dojo.declare("evf.data.Pager", [Widget, Template], {
  
  metadata:             null,
  
  previousNode:         null,
  pageNodeFirst:        null,
  dotsNodeFirst:        null,
  dynamicContainerNode: null,
  dotsNodeLast:         null,
  pageNodeLast:         null,
  nextNode:             null,
  
  templateString:   dojo.cache('evf.data', 'templates/Pager.html'),
  
  postMixInProperties: function() {
	  this.inherited(arguments);
	  this.i18n = dojo.i18n.getLocalization("evf.data", "pager");
  },
  
  postCreate: function() {
	  this.inherited(arguments);
  },
  
  startup: function() {
	  this.inherited(arguments);
	  this._buildUI();
  },
  
  _setMetadataAttr: function(meta) {
	  this.metadata = meta;
	  this._buildUI();
  },
  
  _buildUI: function() {
	  if(!this._started)
		  return;
	  
	  if (!this.metadata)
		  throw 'metadata is required';
	  
	  dojo.forEach(this._connects, this.disconnect, this);
	  
	  if (this.metadata.totalPages <= 1) {
		  dojo.style(this.domNode, 'display', 'none');
		  return;
	  }
    
	  // calculate the inner numbers needed to display
	  var startLimit = 2;
	  var endLimit = this.metadata.totalPages - 1;
	  var increment = 2;
	
	  var start = Math.max(startLimit, this.metadata.page - increment);
	  var end = Math.min(endLimit, this.metadata.page + increment);
	  if(end < start) end = start;
	
	  if (end - start < 4) {
		  var needed = 4 - (end - start);
		  if (start == startLimit && end < endLimit) {
			  end = Math.min(end + needed, endLimit);
		  }
	  
		  if (end == endLimit && start > startLimit) {
			  start = Math.max(startLimit, end - needed);
		  }
	  }
	    
	  var _s = this;
	  var fnWireLink = function(a, page) {
		  _s.connect(a, 'onclick', function() { _s.onNavigate(page); });
	  };
   
	  if (this.metadata.page == 1) {
		  dojo.addClass(this.pageNodeFirst, 'current');
	  } else {
		  dojo.removeClass(this.pageNodeFirst, 'current');
		  fnWireLink(this.pageNodeFirst, 1);
		  fnWireLink(this.previousNode, this.metadata.page - 1);
	  }
	  dojo.style(this.previousNode, 'display', this.metadata.page == 1 ? 'none' : '');
    
	  if (this.metadata.page == this.metadata.totalPages) {
		  dojo.addClass(this.pageNodeLast, 'current');
	  } else {
		  dojo.removeClass(this.pageNodeLast, 'current');
		  fnWireLink(this.pageNodeLast, this.metadata.totalPages);
		  fnWireLink(this.nextNode, this.metadata.page + 1);
	  }
	  
	  this.pageNodeLast.innerHTML = this.metadata.totalPages;
	  dojo.style(this.nextNode, 'display', this.metadata.page == this.metadata.totalPages ? 'none' : '');
    
	  domConstruct.empty(this.dynamicContainerNode);
	  for (var i=start; i<=end; i++) {
		  var a = domConstruct.create('a', {}, this.dynamicContainerNode);
		  a.innerHTML = i;
		  dojo.addClass(a, 'page-numbers');
      
		  if (this.metadata.page == i) {
			  dojo.addClass(a, 'current');
		  } else {
			  fnWireLink(a, i);
		  }
	  }
  },
  
  onNavigate: function(page) { }
  
});

});