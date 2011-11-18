/*
 * Edit:
 * 
 *  - 2010/05/13 : getClone(), added the right width using getSize().x from the parent element (line 77) 
 * 
 */

var varIShowlet = {

	Implements: [Events, Options],

	options: {
		box: ".showlet",
		snap: 4,
		opacity: 1,
		clone: false,
		revert: false,
		handle: ".box_cappello_up_aux",
		handleToolTipText: "Drag Here",
		handlerCssClass1: "moveHandler",
		handlerCssClass2: "moveHandler2",
		handlerCssClass3: "moveHandler_relative",
		constrain: false, 
		ajaxSwapUrl: "",
		ajaxDeleteUrl: "",
		ajaxParamCurrentFrame: "frameSource",
		ajaxParamframeToSwapWith: "frameDest",
		ajaxParamCurrentPage: "currentPage",
		ajaxParamCurrentPageValue: "currentPage",
		swappingElCss: "swappingEl",
		backcloneElCss: "jpmyportalclone_back",
		cloneElCss: "jpmyportalclone"
		
	},

	initialize: function(lists, options){
		this.setOptions(options);
		this.elements = [];
		this.idle = true;
		this.addItems();
		if (!this.options.clone) this.options.revert = false;
		if (this.options.revert) this.effect = new Fx.Morph(null, $merge({duration: 250, link: 'cancel'}, this.options.revert));
	},

	addItems: function(){
		
		this.elements = $$(this.options.box);
		
		for (var i = 0; i < this.elements.length; i++){
			var element = this.elements[i];
			this.setupItem(element);
		}
	},
	setupItem: function(element) {
		//element.store("currentPos",element.getFirst(".this_editable").getFirst("span").get("class"));
		element.store("currentPos",element.getElements(".this_editable")[0].getFirst("span").get("class"));
		var start = element.retrieve('sortables:start', this.start.bindWithEvent(this, element));
		
		var appliedClass = this.options.handlerCssClass1;
		if (element.getElements(".inputOptions").length > 0) { appliedClass=this.options.handlerCssClass2;};
		
		var handle = new Element("button",{
			"class" : appliedClass,
			"title" : this.options.handleToolTipText
		});
		
		handle.inject(element.getElement(this.options.handle));
		if (element.getElements(".inputBack").length > 0) {
			handle.swapClass(appliedClass,this.options.handlerCssClass3);
			handle.inject(element.getElements(".inputBack")[0], "bottom");
		}
		
		(handle ? handle || element.getElement(this.options.handle) || element : element).addEvent('mousedown', start);
	},

	getClone: function(event, element){
		if (!this.options.clone) return new Element('div').inject(document.body);
		if ($type(this.options.clone) == 'function') return this.options.clone.call(this, event, element, this.list);
		return element.clone(true).setStyles({
			margin: '0px',
			position: 'absolute',
			visibility: 'hidden',
			'width': element.getStyle('width')
		}).inject(this.list).setPosition(element.getPosition(element.getOffsetParent())).setStyle("width",element.getSize().x);
	},

	getDroppables: function(){
		var droppables = this.list.getChildren();
		if (!this.options.constrain) droppables = this.lists.concat(droppables).erase(this.list);
		return droppables.erase(this.clone).erase(this.element);
	},

	insert: function(dragging, element){
		this.swapElement = element;
		element.addClass(this.options.swappingElCss);
	},
	leave: function(dragging, element){
		this.swapElement = null;
		element.removeClass(this.options.swappingElCss);
	},

	start: function(event, element){
		if (!this.idle) return;
		this.idle = false;
		this.element = element;
		this.opacity = element.get('opacity'); 
		this.list = element.getParent();
		this.clone = this.getClone(event, element);
		
		
		element.addClass(this.options.backcloneElCss);
		this.clone.addClass(this.options.cloneElCss);

		var drooooop = $$(this.options.box);
		drooooop.erase(this.drag);
		drooooop.erase(this.clone);
		drooooop.erase(this.element);
		drooooop.erase(element);

		//element.addClass("movingEl");

		this.drag = new Drag.Move(this.clone, {
			snap: this.options.snap,
			container: this.options.constrain && this.element.getParent(),
			droppables: drooooop,
			onSnap: function(){
				event.stop();
				this.clone.setStyle('visibility', 'visible');
				this.fireEvent('start', [this.element, this.clone]);
			}.bind(this),
			onEnter: this.insert.bind(this),
			onLeave: this.leave.bind(this),
			onCancel: this.reset.bind(this),
			onComplete: this.end.bind(this)
		});

		this.clone.inject(this.element, 'before');
		this.drag.start(event);
	},

	end: function(dragging, element){
		
		if($defined(this.swapElement)) {
			
			//console.log("c'è...") 
			this.element.inject(this.swapElement,"before");
			this.swapElement.inject(this.clone,"before");
			this.swapElement.removeClass(this.options.swappingElCss);
			this.element.removeClass(this.options.backcloneElCss);
			
			this.element.setStyle("border","2px solid yellow");
			this.swapElement.setStyle("border","2px solid yellow");
			
			var elnum,swpnum,swpnumInput,elnumInput;
			var elnumInput = this.element.retrieve("currentPos");
			var swpnumInput = this.swapElement.retrieve("currentPos");

			var ajaxReturn; 

			elnum = this.element.getElement("h2 abbr").get("text");
			swpnum = this.swapElement.getElement("h2 abbr").get("text");
			this.element.getElement("h2 abbr").set("text", swpnum);
			this.swapElement.getElement("h2 abbr").set("text", elnum);

			ajaxReturn = this.ajaxSwapRequest(elnumInput,swpnumInput,this.element,this.swapElement);
		
			if($defined(swpnumInput)) {
				
				var elCF = this.element.getElements("input[name=frameSource]")[0];
				var elFTE = this.element.getElements("input[name=frameToEdit]")[0];
				var elCPF = this.element.getElements("input[name=currentPageFrame]")[0];
				
				
				if($defined(elCF)) { 
					//var old = elCF.get("value");
					elCF.set("value",swpnumInput); 
					//this.checkValue(old,elCF.get("value"),elCF);
				}
				if($defined(elFTE)) { 
					//var old = elFTE.get("value");
					elFTE.set("value",swpnumInput);
					//this.checkValue(old,elFTE.get("value"),elFTE);
				}
				if($defined(elCPF)) { 
					//var old = elCPF.get("value");
					elCPF.set("value",swpnumInput);
					//this.checkValue(old,elCPF.get("value"),elCPF);
				}
			}
			if ($defined(elnumInput)) {
				var swpCF = this.swapElement.getElements("input[name=frameSource]")[0];
				var swpFTE = this.swapElement.getElements("input[name=frameToEdit]")[0];
				var swpCPF = this.swapElement.getElements("input[name=currentPageFrame]")[0];
				
				if($defined(swpCF)) { 
					//var old = swpCF.get("value");
					swpCF.set("value",elnumInput);
					//this.checkValue(old,swpCF.get("value"),swpCF);
				}

				if($defined(swpFTE)) { 
					//var old = swpFTE.get("value");
					swpFTE.set("value",elnumInput);
					//this.checkValue(old,swpFTE.get("value"),swpFTE);							
				}

				if($defined(swpCPF)) { 
					//var old = swpCPF.get("value");
					swpCPF.set("value",elnumInput);
					//this.checkValue(old,swpCPF.get("value"),swpCPF);	
				}
				
			}
			if(ajaxReturn) {
				//console.log("ajaxReturn",ajaxReturn);
				this.element.store("currentPos",swpnumInput);
				this.swapElement.store("currentPos",elnumInput);
			}
				
		}else {
			//console.log("non c'è...")
		}
		
		
		this.drag.detach();
		//this.element.set('opacity', this.opacity);
		if (this.effect){
			var dim = this.element.getStyles('width', 'height');
			var pos = this.clone.computePosition(this.element.getPosition(this.clone.offsetParent));
			this.effect.element = this.clone;
			this.effect.start({
				top: pos.top,
				left: pos.left,
				width: dim.width,
				height: dim.height,
				opacity: 0.25
			}).chain(this.reset.bind(this));
		
		
		} else {
			this.reset();
		}
	
	this.swapElement=null;
				
	},
	checkValue: function(old,neww,el) {
		var ret = true;
		if (old == neww) {
			//console.log("Attenzione invariato",old,neww,el);
		}
		//console.log("tutto ok",old,neww,el);
		return ret;
	},
	
	ajaxSwapRequest: function(fromPos,toPos,elfrom,swapel) {
		var stringData = this.options.ajaxParamCurrentPage+'='+this.options.ajaxParamCurrentPageValue+'&'+this.options.ajaxParamCurrentFrame+'='+fromPos+'&'+this.options.ajaxParamframeToSwapWith+'='+toPos;
		var myRequest = new Request({
			url: this.options.ajaxSwapUrl,
			onSuccess: function(responseText,responseXML) {
				ret = true;
				elfrom.erase("style");
				swapel.erase("style");
			}		
		});
		var ret = true;
		myRequest.send({ 
		    method: 'post',
		    data: stringData
		});
		return ret; 
		//console.log(stringData);
	},

	ajaxDeleteRequest: function(deletepos,elementToDelete) {
		var stringData = this.options.ajaxParamCurrentPage+'='+this.options.ajaxParamCurrentPageValue+'&'+this.options.ajaxParamCurrentFrame+'='+fromPos+'&'+this.options.ajaxParamframeToSwapWith+'='+toPos;
		var myRequest = new Request({
			url: this.options.ajaxDeleteUrl,
			onSuccess: function(responseText,responseXML) {
				ret = true;
				elfrom.erase("style");
				swapel.erase("style");
			}		
		});
		var ret = true;
		myRequest.send({ 
		    method: 'post',
		    data: stringData
		});
		return ret; 
		//console.log(stringData);
	},

	reset: function(){
		this.idle = true;
		this.clone.destroy();
		this.swapElement=null;
		this.fireEvent('complete', this.element);
	},

	serialize: function(){
		var params = Array.link(arguments, {modifier: Function.type, index: $defined});
		var serial = this.lists.map(function(list){
			return list.getChildren().map(params.modifier || function(element){
				return element.get('id');
			}, this);
		}, this);

		var index = params.index;
		if (this.lists.length == 1) index = 0;
		return $chk(index) && index >= 0 && index < this.lists.length ? serial[index] : serial;
	}

};

var IShowlet = new Class(varIShowlet);