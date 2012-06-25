/*
---

script: moo-jAPS-Homepage.js.js

description: A class for handling sortables boxes

license: MIT-style license

authors:
- Andrea Dessì <a.dessi@agiletec.it>

...
*/
var MOO_JAPS_JPMYPORTALPLUS = new Class({
	Implements: [Events,Options],
	options: {
		//"standardBoxSize": "500px",
		"duration" : 400,
		"draggableLists" : [".cs", ".cc", ".cd", ".unica"],
		"selettoreBox" : ".box",
		"selettoreCappello" : ".title",
		"selettoreApriChiudi" : ".openClose",
		"selettoreEdit" : ".editContent",
		"selettoreEditForm" : ".editContentBox", //form
		"selettoreRemove" : ".removeBox",
		"selettoreAjaxLoader": ".jpmyportalplus-loading-tmp-el",
		"boxApriChiudi" : ".content",
		"selettoreEditText" : {
			"open" : "apri",
			"close" : "chiudi"
		},
		"pageEditform": "editshowletlistForm",
		"boxEdit" : ".editContentBox",
		"contenitoreColonne": "columns",
		"cssClass" : {
			"draggin" : "el",
			"cloned" : "boxClone",
			"hover": "hover",
			"editFormElMessage": "form_message"
		},
		"showletIdPrefix": "__s__"
	},
	initialize: function(options){
		this.setOptions(options);
		this._setupDraggableLists();

		this._loadingEl = new Element("span",{"class": this.options.selettoreAjaxLoader.substring(1),"text": ""}).inject(document.body);
		this._loadingEl.dispose();

		this._setupBoxes();
		this._setupListForm();
		this._setupSortable();
	},
	addList: function(list) {
		this.lists.push(list);
		return this.lists;
	},
	_setupListForm: function() {
		if ($defined(this.options.pageEditform)) {
			this.editshowletlistForm = document.id(this.options.pageEditform);
		}
	},
	applyLoading: function(item) {
		if (item!==null&&item.getElement(this.options.selettoreApriChiudi)!=null){
			this._loadingEl.clone().inject(item.getElement(this.options.selettoreApriChiudi),"after");
		}
	},
	removeLoading: function(item){
		var f = function(item) {
			if (item!==null&&item.getElement(this.options.selettoreAjaxLoader)!==null) {
				item.getElements(this.options.selettoreAjaxLoader).destroy();
			}
		}
		f.delay(250,this,item);
	},
	_setupBoxes: function() {
		var boxes = document.getElements(this.options.selettoreBox);
		for (var i = 0; i < boxes.length; i++) {
			var item = boxes[i];
			var code_pos_arr = item.get("id").substring(8).split(this.options.showletIdPrefix);
			item.store("code",code_pos_arr[0]);
			item.store("position",code_pos_arr[1]);

			if (item.getElement(this.options.boxApriChiudi).getStyle("display") == "none") {
				item.store("status","down");
			}
			else {
				item.store("status","up");
			}
			var openEl = null;
			var editEl = null;
			var editFormEl = null;
			var closeEl = null;
			var boxApriChiudi = null;
			var configEl = null;

			if ($defined(this.options.selettoreApriChiudi)) openEl = item.getElement(this.options.selettoreApriChiudi);
			if ($defined(this.options.selettoreEdit)) editEl = item.getElement(this.options.selettoreEdit);
			if ($defined(this.options.selettoreEditForm)) editFormEl = item.getElement(this.options.selettoreEditForm);
			if ($defined(this.options.selettoreRemove)) closeEl = item.getElement(this.options.selettoreRemove);
			if ($defined(this.options.boxApriChiudi)) boxApriChiudi = item.getElement(this.options.boxApriChiudi);
			if ($defined(this.options.configEl)) configEl = item.getElement(this.options.configEl);

			item.getElement(this.options.selettoreCappello).addEvents({
				"mouseover": function(){ this[0].addClass(this[1].cssClass.hover); }.bind([item,this.options]),
				"mouseleave": function(){ this[0].removeClass(this[1].cssClass.hover); }.bind([item,this.options])
			});
			if ($defined(openEl) && $defined(boxApriChiudi)) {
				var fxopenEl = new Fx.Reveal(boxApriChiudi,{
					"duration": this.options.duration,
					"transition": Fx.Transitions.Linear,
					"mode": "vertical"
				});
				var requestOpenEl = new Request.JSON({
					"secure": true,
					"link": "ignore",
					"noCache": true,
					onRequest: function request() {
						this[0].applyLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,openEl]),
					onFailure: function failure() {
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,openEl]),
					onException: function exeption() {
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,openEl]),
					onCancel: function cancel() {
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,openEl]),
					onSuccess: function(result){
						this[0].setupButtonsFormStatus(this[1],this[1].retrieve("position"));
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,openEl])
				});

				var fnShowHide = function(ev) {
						ev.preventDefault();
						var item = this[0];
						var openEl = this[1];
						var classThis = this[2];
						var boxEdit = this[2].options.boxEdit;
						var selettoreEditText = this[2].options.selettoreEditText;
						var ajaxConfig = this[2].options.ajaxConfig;
						var fx = this[3];
						var requestOpenEl = this[4];
						var boxApriChiudi = this[5];
						var url = null;
						var status = boxApriChiudi.getStyle("display") == "none" ? "down" : "up";
						fx.toggle();
						if (status == "down") {
							item.store("status","up");
							url = ajaxConfig.URL.openFrame;
							if ($defined(selettoreEditText.close)) {
								openEl.set("text",selettoreEditText.close);
							}
						}
						else {
							item.store("status","down");
							url = ajaxConfig.URL.closeFrame;
							if ($defined(boxEdit) && $defined(item.getElement(boxEdit))) {
								item.getElement(boxEdit).dissolve();
							}
							if($defined(selettoreEditText.open)) {
								openEl.set("text",selettoreEditText.open);
							}
						}
						var objReq = {};
						objReq[ajaxConfig.statusParams.openclose] = item.retrieve("position");;
						requestOpenEl.options.url = url;
						requestOpenEl.get(objReq);
						item.setStyle("width", "");
					}.bind([item,openEl,this,fxopenEl,requestOpenEl,boxApriChiudi]);
				openEl.addEvent("click",fnShowHide);
				item.getElement(this.options.selettoreCappello).addEvent("dblclick",fnShowHide);
			}

			var fxBoxAjaxFormReq = undefined;
			var fxBoxEditEl = undefined;
			if (($defined(editEl) || $defined(configEl)) && $defined(this.options.boxEdit)) {
				var boxEditEl = item.getElement(this.options.boxEdit);
				fxBoxAjaxFormReq = new Request.HTML({
					"url": this.options.ajaxConfig.URL.configWidget,
					"secure": true,
					"noCache": true,
					"link": "cancel",
					onException: function exeption() {
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,boxEditEl]),
					onCancel: function(){
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,boxEditEl]),
					onFailure: function(){
							this[1].set("html","<p>"+this[0].options.ajaxConfig.i18n.error+"</p>");
							this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,boxEditEl]),
					onRequest: function(){
						this[1].set("html","<p>"+this[0].options.ajaxConfig.i18n.loading+"</p>");
						this[0].applyLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
					}.bind([this,boxEditEl]),
					onSuccess: function(responseTree, responseElements, responseHTML, responseJavaScript){
						this[1].set("html",responseHTML);
						this[0].removeLoading.run(this[1].getParent(this[0].options.selettoreBox),this[0]);
						this[0].fireEvent("showEditBox",[this[2],this[1]]);
						if (responseJavaScript!==undefined && responseJavaScript!=null && responseJavaScript.length>0) {
							eval(responseJavaScript);
						}

					}.bind([this,boxEditEl,item])
				});
				fxBoxEditEl = new Fx.Reveal(boxEditEl,{
					"duration": this.options.duration,
					"transition": Fx.Transitions.Linear,
					"mode": "vertical",
					"onComplete": function() {
						//console.log("onComplete", this);
						//console.log("onComplete", this);
					},
					onChainComplete: function() {
						//console.log("onChainComplete", this);
					}
				});
			}

			if ($defined(editEl) && $defined(this.options.boxEdit)) {
				var boxEditEl = item.getElement(this.options.boxEdit);
				if ($defined(boxEditEl)) {
					var url = this.options.ajaxConfig.URL.formMove;
					editEl.addEvent("click",function(ev) {
						if ($defined(ev)) ev.preventDefault();
						var item = this[0];
						var editEl = this[1];
						var thiis = this[2];
						var boxEditEl = this[3];
						var status = boxEditEl.getStyle("display") == "none" ? "down" : "up";
						var fxBoxEditEl = this[4];
						var fxBoxAjaxFormReq = this[5];
						//fxBoxEditEl.toggle();
						if (status == "down") {
								boxEditEl.reveal();
								editEl.store("status","up");
								var objReq = {};
								objReq["id"] = item.retrieve("id");
								objReq[this[2].options.ajaxConfig.URL.paramNames.formMove] = item.retrieve("position");
								fxBoxAjaxFormReq.get(objReq);
								boxEditEl.setStyle("width", "");
							}
						else {
							thiis.fireEvent("hideEditBox",[item, editEl, boxEditEl]);
							boxEditEl.dissolve();
							editEl.store("status","down");
						}
					}.bind([item,editEl,this,boxEditEl,fxBoxEditEl,fxBoxAjaxFormReq]));
				}
			}
			if($defined(closeEl)) {
				closeEl.addEvents({
					"click": function(ev){
						ev.preventDefault();
						var item = this[0];
						var closeEl = this[1];
						var mythis = this[2];
						mythis.applyLoading(item);
						item.dissolve({
							duration: 500,
							onComplete: function(){
								this[1].ajaxRemoveFrame(this[0]);
								this[1].editshowletlistFormRemove(this[0].retrieve("code"));
								this[0].destroy();
								this[1].removeLoading.run(this[0],this[1]);
							}.bind([item,mythis])
						});
					}.bind([item,closeEl,this])
				});
			}

			//config section //start
				if ($defined(configEl) && $defined(this.options.boxEdit)) {
					var boxEditEl = item.getElement(this.options.boxEdit);
					configEl.addEvent("click",function(ev) {
						ev.preventDefault();
						var item = this[0];
						var configEl = this[1];
						var thiis = this[2];
						var boxEditEl = this[3];
						var status = boxEditEl.getStyle("display") == "none" ? "down" : "up";
						var fxBoxEditEl = this[4];
						var fxBoxAjaxFormReq = this[5];
						if (status == "down") {
								boxEditEl.reveal({
									transition: 'linear',
									onComplete: function() {
										//this.element.setStyle("height", "");
									}
								}); 
								configEl.store("status","up");
								var objReq = {};
								objReq["id"] = item.retrieve("id"); 
								objReq[thiis.options.ajaxConfig.URL.paramNames.configWidget] = item.retrieve("position");
								fxBoxAjaxFormReq.options.url = thiis.options.ajaxConfig.URL.configWidget;
								fxBoxAjaxFormReq.get(objReq);
						}
						else {
							thiis.fireEvent("hideEditBox",[item, editEl, boxEditEl]);
							boxEditEl.dissolve({
									transition: 'linear',
									onComplete: function() {
										//this.element.setStyle("height", "0px");
									}
							});
							configEl.store("status","down");
						}
					}.bind([item,configEl,this,boxEditEl,fxBoxEditEl,fxBoxAjaxFormReq]));
				}
			//config section //end

		}
	},
	editshowletlistFormRemove: function(code) {
		if ($defined(this.editshowletlistForm) && $defined(code)) {
			var inputstring = "input[value="+code+"]";
			var inputEl = this.editshowletlistForm.getElement(inputstring);
			inputEl.removeProperty("checked");
		}
	},
	_setupDraggableLists: function(){
		var status = false;
		if ($defined(this.options.draggableLists) && $defined(this.options.selettoreBox)) {
			if (this.options.draggableLists.length > 0) {
				this.lists=[];
				var lists = this.options.draggableLists;
				status = true;
				for (var i = 0; i<lists.length;i++){
					var listid=lists[i].split("#")[1];
					document.id(listid).store("framepos",this.options.ajaxConfig.frames[listid]);
					this.addList(lists[i]);
				}
				this.flattenlist = [];
					this.lists.each(function(item, index){
						this.push(document.getElements(item));
					}.bind(this.flattenlist));
				this.flattenlist = this.flattenlist.flatten();
			}
		}
		return status;
	},
	_setupSortable: function() {
		var selettoreContainerListe;
		var r = "";
		for (var i=0; i < this.lists.length;i++) {
			r = r+this.lists[i];
			if (i < this.lists.length - 1) {
				r = r + ", ";
			}
		}
		selettoreContainerListe = r;
		var mythis = this;
		this.listPositions=this.setupLists();
		this.mySortables = new MOO_JAPS_JPMYPORTALPLUS_SORTABLE(selettoreContainerListe, {
			revert: { "duration": this.options.duration, transition: 'linear' },
			clone: true,
			snap: 2,
			handle: this.options.selettoreCappello,
			onBeforeStart: function (element,clone) {
				element.setStyle("width",element.getDimensions().x);
				if ($defined(this.options.boxEdit)) {
					var boxEditEl = element.getElement(this.options.boxEdit);
					if ($defined(boxEditEl)) {
							boxEditEl.dissolve();
							clone.getElement(this.options.boxEdit).dissolve();
						}
				}
				var rangeColumns = {}; //prepara l'oggetto
				for (var i = 0;i < this.flattenlist.length;i++) { //per tutte le colonne
					var currentCol = this.flattenlist[i];
					rangeColumns[currentCol.get("id")] = [];
					rangeColumns[currentCol.get("id")].push(currentCol.getPosition().x); //setta la quota di inizio area
					rangeColumns[currentCol.get("id")].push(currentCol.getPosition().x+currentCol.getDimensions().x); //e la quota di fine area
					currentCol.store("startHeight",currentCol.getDimensions().y); //setta una variabile con l'altezza di partenza //utilizzato da this.setupOverColumn()
				}
				this.rangeColumns = new Hash(rangeColumns); //crea l'hash con tutte le quote delle colonne //utilizzato da this.checkColumnOver()

				this.contenitoreColonneStartHeight = { //recupera le dimenzioni iniziali del container
					"height": document.id(this.options.contenitoreColonne).getDimensions().y, //altezza
					"top": document.id(this.options.contenitoreColonne).getPosition().y //quota y
				};
				return true;
			}.bind(this),
			onDragg: function(element, event) {
				var overColumn = this.checkColumnOver(event.page.x); //in base alla quota orizzontale trova la colonna
				if ($defined(overColumn)) { //se esiste una colonna
					if (!$defined(this.oldDragColumn)) { //se non esiste una variabile oldDragColumn settala
						this.oldDragColumn = overColumn;
					}
					else if (this.oldDragColumn != overColumn) { //se esiste, ma è diversa dalla colonna corrente
						document.id(this.oldDragColumn).setStyle("min-height",""); //resetta la min-height alla vecchia colonna
						this.oldDragColumn = overColumn; //imposta la nuova colonna
						if (overColumn != element.getParent().get("id")) {//se la nuova colonna è diversa dalla colonna di partenza
							this.setupOverColumn(overColumn,element.getDimensions().y); //effettua il setup delle colonne aggiungendo la dimensione del box all'altezza della colonna
						}
						else {
							this.setupOverColumn(overColumn,0); //altrimenti, effettua il setup delle colonne senza aggiungere niente a causa del doppio clone
						}
					}
				}
				else {
					this.setupOverColumn(); //se non sto effettuando l'over su nessuna colonna, le resetto.
				}
			}.bind(this),
			onStart: function(element, clone) {
				if ($defined(this.options.standardBoxSize)) {
					element.setStyle("width",this.options.standardBoxSize);
					clone.setStyle("width",this.options.standardBoxSize);
				}
				clone.removeClass(this.options.cssClass.hover);
				clone.addClass(this.options.cssClass.cloned);
				element.addClass(this.options.cssClass.draggin);
				element.removeClass(this.options.cssClass.hover);
			}.bind(this),
			onEnd: function(element,clone) {
				element.removeClass(this.options.cssClass.draggin);
				element.removeClass(this.options.cssClass.cloned);
				clone.removeClass(this.options.cssClass.draggin);
				clone.removeClass(this.options.cssClass.cloned);
				clone.setStyle("width","100%");
				element.setStyle("width","100%");
				//debugger;
				this.moveFrame(element);
				this.rangeColumns = null;
				this.oldDragColumn = null;
				this.setupOverColumn();
			}.bind(this)
		});
		this.mySortables.attach();
	},
	setupOverColumn: function(overColumn, size) {
		if ($defined(overColumn) && $defined(size)) { //se esiste la colonna di riferimento e la dimensione del box
			document.id(overColumn).setStyle("min-height",document.id(overColumn).retrieve("startHeight")+size); //setta l'altezza della colonna a "altezza corrente + altezza box spostato"
		}
		else {
			for (var i = 0; i < this.flattenlist.length; i++) { //se non eiste una colonna di riferimento, resetta tutte le altezze
				this.flattenlist[i].setStyle("min-height","");
			}
		}
	},
	checkColumnOver: function(mouseposition) { //scopre su quale colonna si sta passando
		var whereTheMouseIsOver = null; //inizializzo la variabile
		if (this.rangeColumns!=null&&this.rangeColumns!=undefined) { //se definito l'oggetto con i range delle quote delle colonne
			this.rangeColumns.each(function(value, key){ //per chiascuno elemento della mappa
				if (value[0] < mouseposition && mouseposition < value[1]) { //verifico la posizione del mouse sia compresa fra le due quote
					whereTheMouseIsOver = key; //se si, setto ed esco
				}
			});
		}
		return whereTheMouseIsOver;
	},
	moveFrame: function(element) {
		var paramNames = this.options.ajaxConfig.URL.paramNames;
		var positions = this._findCurrentElementPosition(element);
		var url = this.options.ajaxConfig.URL.moveFrame;
		var objReq = {};
		objReq["id"] = element.retrieve("position");
		objReq[paramNames.startFramePos] = positions.currentpos;
		objReq[paramNames.targetPrevFramePos] = positions.prevpos;
		objReq[paramNames.targetNextFramePos] = positions.nextpos;
		var r = new Request.JSON({
			"url": url,
			"secure": true,
			"noCache": true,
			onRequest: function(){
				this[0].applyLoading.run(this[1],this[0]);
			}.bind([this,element]),
			onSuccess: function(result){
				var newPos = result.result;
				var shiftingElements = result.shiftingElements;
				var element = this[0];
				var mythis = this[1];
				if ($defined(shiftingElements)) {
					mythis._shiftElements(shiftingElements);
				}
				mythis.setupButtonsFormStatus(element,newPos);
				this[1].removeLoading.run(this[0],this[1]);
			}.bind([element,this])
		}).get(objReq);
	},
	_shiftElements: function(shiftingElements) {
		var elementsToShift = [];
		for (var i = 0;i<shiftingElements.length;i++) {
			var element = shiftingElements[i][0];
			var newPos = shiftingElements[i][1];
			element = document.getElement("#"+this.options.contenitoreColonne+ " div[id*="+this.options.showletIdPrefix+element+"]");
			if ($defined(element)) {
				elementsToShift.push([element,newPos]);
			}
		}
		for (var i = 0;i< elementsToShift.length;i++) {
			var currentEl = elementsToShift[i][0];
			var currentNewPos = elementsToShift[i][1];
			this.setupButtonsFormStatus(currentEl,currentNewPos);
		}
	},
	setupButtonsFormStatus: function(element,newPos) {
		if ($defined(element) && $chk(newPos)) {
			element.store("position",newPos);
			element.setProperty("id","showlet_"+element.retrieve("code")+this.options.showletIdPrefix+newPos);
			var inputEl= element.getElement("input[name="+ this.options.ajaxConfig.URL.paramNames.startFramePos + "]");
			if ($defined(inputEl)) {
				inputEl.set("value",newPos);
			}

			var elaprichiudi=element.getElement(this.options.selettoreApriChiudi);
			var elrimuovi=element.getElement(this.options.selettoreRemove);
			var elsposta=element.getElement(this.options.selettoreEdit);
			if ($defined(elaprichiudi)) {
				var uri = null;
				var status = element.retrieve("status");
				if (status == "down") {
					uri = new URI(this.options.ajaxConfig.URL.openFrameStandard);
				}
				else if (status == "up") {
					uri = new URI(this.options.ajaxConfig.URL.closeFrameStandard);
				}
				var objData = {};
				objData[this.options.ajaxConfig.statusParams.openclose] = newPos;
				uri.setData(objData, true);
				elaprichiudi.set("href",uri.toString());
			}
			if ($defined(elrimuovi)) {
				var uri = new URI(elrimuovi.get("href"));
				var objData = {};
				objData[this.options.ajaxConfig.URL.paramNames.frameToEmpty] = newPos;
				uri.setData(objData, true);
				elrimuovi.set("href",uri.toString());
			}
			if ($defined(elsposta)) {
				var uri = new URI(elsposta.get("href"));
				var objData = {};
				objData[this.options.ajaxConfig.statusParams.edit] = newPos;
				uri.setData(objData, true);
				elsposta.set("href",uri.toString());
			}
		}
	},
	ajaxRemoveFrame: function(element) {
		var positionToRemove = element.retrieve("position");
		var url = this.options.ajaxConfig.URL.removeFrame;
		var objReq = {};
		objReq[this.options.ajaxConfig.URL.paramNames.frameToEmpty] = positionToRemove;
		var r = new Request.JSON({
			"url": url,
			"secure": true,
			"noCache": true,
			onSuccess: function(result){
				//nothing to do
			}
		}).get(objReq);
	},
	_findCurrentElementPosition: function (element) {
		var finalpos = { "currentpos" : element.retrieve("position") };
		var prevEl = element.getPrevious(this.options.selettoreBox);
		var nextEl = element.getNext(this.options.selettoreBox);
		var previousPositionValue = null;
		var nextPositionValue = null;
		if ($defined(prevEl)) {
			if ($defined(prevEl.getPrevious(this.options.selettoreBox)) ) {
				var prevTemp = prevEl.getPrevious(this.options.selettoreBox);
				if (prevTemp.hasClass(this.options.cssClass.clone)) {
					prevEl = prevTemp;
				}
			}
			previousPositionValue = prevEl.retrieve("position");
		}
		if ($defined(nextEl)) {
			nextPositionValue = nextEl.retrieve('position');
		}
		if (previousPositionValue == null && nextPositionValue == null) {
			var parentId = element.getParent().get("id");
			previousPositionValue = this.options.ajaxConfig.frames[parentId];
		}
		finalpos["prevpos"] = previousPositionValue;
		finalpos["nextpos"] = nextPositionValue;
		//console.log(finalpos.prevpos,finalpos.currentpos,finalpos.nextpos)
		return finalpos;
	},
	setupLists: function() {
		var boxes = [];
		for (var i = 0;i<this.lists.length;i++) {
			var currentList = document.getElements(this.lists[i]+ " "+this.options.selettoreBox);
			boxes[i] = [];
			for (var y = 0; y < currentList.length;y++) {
				var b = currentList[y];
				boxes[i].push(b.get("id"));
			}
			boxes[i] = boxes[i].clean();
		}
		boxes = boxes.clean();
		return boxes;
	}
});
/* //how to use it, example
window.addEvent("domready",function(){
	new MOO_JAPS_JPMYPORTALPLUS();
});
*/