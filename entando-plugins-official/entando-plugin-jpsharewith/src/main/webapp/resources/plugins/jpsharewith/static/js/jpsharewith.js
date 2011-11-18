var currentUrl;
var currentTitle;
window.addEvent('domready', function(){
	currentUrl = encodeURIComponent(document.location.href);
	currentTitle = encodeURIComponent(document.title);
	var target = $('jpsharewith');
	jpsharewith_buttons.each(function(item,index) {
		item.link = item.link.replace(/§URL§/g,currentUrl);
		item.link = item.link.replace(/§TITLE§/g,currentTitle);
		var a = new Element('a',{href: item.link});
		var img = null;
		if ($pick(item.image) != null) {
			img = new Element('img',{src : jpsharewith_imgURL+item.image});
		}
		a.set('text',item.title);
		if (img != null){
			img.inject(a,'top');
		}
		a.inject(target);
		target.set('html',target.get('html')+' ');
	});
});