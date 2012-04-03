package com.agiletec.plugins.jpuserreg.aps.system.common.entity.model.attribute.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.util.EnumeratorAttributeItemsExtractor;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

public class LangItemsExtractor implements EnumeratorAttributeItemsExtractor {
	
	@Override
	public List<String> getItems() {
		List<Lang> langs = this.getLangManager().getLangs();
		List<String> langItems = new ArrayList<String>(langs.size());
		Iterator<Lang> langsIter = langs.iterator();
		while (langsIter.hasNext()) {
			String langCode = langsIter.next().getCode();
			langItems.add(langCode);
		}
		return langItems;
	}
	
	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	private ILangManager _langManager;
	
}