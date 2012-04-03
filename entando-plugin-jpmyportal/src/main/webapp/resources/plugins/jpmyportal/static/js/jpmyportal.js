var ijaps;
window.addEvent("domready",function(){
	
	var list = $$(".this_editable");
	
	for (var i = 0; i < list.length;i++) {
		list[i].getParent(".showlet").addClass("editable_showlet");
	} 
	
	ijaps = new IShowlet('', {
	    constrain: false,
	    clone: true, 
	    revert: true,
		box: ".editable_showlet",
	    opacity: 0.5,
		ajaxSwapUrl: varajaxSwapUrl,
		handle: ".box_cappello_up_aux",
		handleToolTipText: varhandleToolTipText,
		handlerCssClass1: "moveHandler",
		handlerCssClass2: "moveHandler2",
		ajaxDeleteUrl: varajaxDeleteUrl,
		ajaxParamCurrentPageValue: varajaxParamCurrentPage,
		ajaxPparamCurrentFrame: "frameSource",
		ajaxPparamframeToSwapWith: "frameDest", 
		ajaxParamCurrentPage: "currentPage"
	});
});
