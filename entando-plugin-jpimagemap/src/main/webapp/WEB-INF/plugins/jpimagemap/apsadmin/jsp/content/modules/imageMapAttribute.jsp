<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="currentResource" value="#attribute.resource"></s:set>
<s:set name="defaultResource" value="#attribute.resource"></s:set>
<s:if test="#lang.default">
	<%-- Lingua di DEFAULT --%>
		<p class="imagebox">
			<span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>
			<s:if test="#currentResource != null && #currentResource.id != 0">
				
					<%-- Lingua di default - Risorsa VALORIZZATA --%>
					<%-- IMMAGINE E LINK + TESTO + PULSANTE RIMUOVI --%>
					
					<span class="imageAttribute">
						<%-- PULSANTE RIMUOVI --%>
						<wpsa:set name="resourceTypeCode"><%= request.getParameter("resourceTypeCode")%></wpsa:set>
						<wpsa:actionParam action="removeImageMapResource" var="removeResourceActionName" >
							<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
							<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
						</wpsa:actionParam>
						<wpsa:set name="iconImagePath" ><wp:resourceURL/>administration/common/img/icons/22x22/delete.png</wpsa:set>
						<wpsf:submit type="image"useTabindexAutoIncrement="true" action="%{#removeResourceActionName}" 
							value="%{getText('label.remove')}" title="%{getText('label.remove')}" src="%{#iconImagePath}" />
						
						<%-- IMMAGINE E LINK --%>
						<span class="imageAttribute-img">
							<a href="<s:property value="#defaultResource.getImagePath('0')" />"><img src="<s:property value="#defaultResource.getImagePath('1')"/>" 
								alt="<s:property value="#defaultResource.descr"/>" /></a>
						</span>

						<%-- TESTO --%>
						<span class="imageAttribute-text">
						<wpsf:textfield useTabindexAutoIncrement="true" id="%{#attributeTracer.getFormFieldName(#attribute.image)}" 
							name="%{#attributeTracer.getFormFieldName(#attribute.image)}" value="%{#attribute.getImage().getTextForLang(#lang.code)}"
							maxlength="254" cssClass="text" cssClass="text" />
						</span>
						
					</span>
					
			</s:if>
			<s:else>
				<%-- Lingua di default - Risorsa NON VALORIZZATA --%>
				<%-- PULSANTE DI RICERCA RISORSA --%>
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/include/chooseResourceSubmit.jsp">
					<s:param name="resourceTypeCode">Image</s:param>
					<s:param name="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/image.png</s:param>
				</s:include>
			</s:else>
		</p>
			
			<%-- AREE --%>
			<div class="jpimage-areas-list">
				<s:if test="%{#attribute.areas.size() >0}">
						<p class="noscreen">
							Areas of the imagemap can be set by button Add
						</p>
						<ul> 
							<s:iterator value="#attribute.areas" id="area" status="elementStatus">
								<wpsa:actionParam action="removeImageMapArea" var="removeAreaActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
								<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/delete.png</wpsa:set>
								<s:set var="htmlPrefixId" value="%{'jpimagemap_'+#attribute.name+'_'+#lang.code+'_'}"  />
								<li>
									<span class="important">Area&#32;<s:property value="#elementStatus.count"/></span>&#32;
									<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeAreaActionName}" type="image" value="%{getText('label.remove')}" title="%{getText('label.remove')}" src="%{#iconImagePath}" />
									<div class="jpimagemap-compositeAttribute">
										<div class="jpimagemap-compositeAttribute-element">
											<label class="basic-mint-label" for="<s:property value="%{#htmlPrefixId+'shape'+#elementStatus.count}"/>">Shape:</label>
											<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#htmlPrefixId+'shape'+#elementStatus.count}" name="%{#attribute.name}_shape_%{#elementStatus.index}" value="%{#area.shape}" disabled="disabled"/>
										</div>
										<div class="jpimagemap-compositeAttribute-element">
											<label class="basic-mint-label" for="<s:property value="%{#htmlPrefixId+'coords'+#elementStatus.count}"/>">Coords:</label>
											<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="%{#htmlPrefixId+'coords'+#elementStatus.count}" name="%{#attribute.name}_coords_%{#elementStatus.index}" value="%{#area.coords}" />
											&#32;
											<%-- define area button --%>
											<wpsa:actionParam action="defineImageMapArea" var="defineAreaActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
											<wpsa:set name="iconImagePath" ><wp:resourceURL/>plugins/jpimagemap/administration/img/tablet.png</wpsa:set>
											<wpsf:submit type="image" useTabindexAutoIncrement="true" action="%{#defineAreaActionName}" value="%{getText('label.defineArea')}" title="%{getText('label.defineArea')}" src="%{#iconImagePath}" />
										</div>
										<div class="jpimagemap-compositeAttribute-element">
											<label class="basic-mint-label" for="<s:property value="%{#htmlPrefixId+'link'+#elementStatus.count}"/>">Link:</label>
											<s:set name="currentAreaLink" value="#area.link"/>
											<s:if test="%{#currentAreaLink.symbolicLink != null}">
												<s:if test="#currentAreaLink.symbolicLink.destType == 2 || #currentAreaLink.symbolicLink.destType == 4">
													<s:set name="linkedPage" value="%{getPage(#currentAreaLink.symbolicLink.pageDest)}"></s:set>
												</s:if>
												<s:if test="#currentAreaLink.symbolicLink.destType == 3 || #currentAreaLink.symbolicLink.destType == 4">
													<s:set name="linkedContent" value="%{getContentVo(#currentAreaLink.symbolicLink.contentDest)}"></s:set>
												</s:if>
												
												<s:set name="validLink" value="true" />
												
												<s:if test="(#currentAreaLink.symbolicLink.destType == 2 || #currentAreaLink.symbolicLink.destType == 4) && #linkedPage == null">
													<%-- LINK SU PAGINA BUCATO  --%>
													<s:set name="validLink" value="false"></s:set>
												</s:if>
												<s:if test="(#currentAreaLink.symbolicLink.destType == 3 || #currentAreaLink.symbolicLink.destType == 4) && (#linkedContent == null || !#linkedContent.onLine)">
													<%-- LINK SU CONTENUTO BUCATO  --%>
													<s:set name="validLink" value="false"></s:set>
												</s:if>
												
												<s:if test="#validLink">
												<%-- LINK VALORIZZATO CORRETTAMENTE --%>
												
													<s:if test="#currentAreaLink.symbolicLink.destType == 1">
														<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-url.png</wpsa:set>
														<s:set name="linkDestination" value="%{getText('note.URLLinkTo') + ': ' + #currentAreaLink.symbolicLink.urlDest}" />
													</s:if>
													
													<s:if test="#currentAreaLink.symbolicLink.destType == 2">
														<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-page.png</wpsa:set>
														<s:set name="linkDestination" value="%{getText('note.pageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
													</s:if>
													
													<s:if test="#currentAreaLink.symbolicLink.destType == 3">
														<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-content.png</wpsa:set>
														<s:set name="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #currentAreaLink.symbolicLink.contentDest + ' - ' + #linkedContent.descr}" />
													</s:if>
													
													<s:if test="#currentAreaLink.symbolicLink.destType == 4">
														<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link-contentOnPage.png</wpsa:set>
														<s:set name="linkDestination" value="%{getText('note.contentLinkTo') + ': ' + #currentAreaLink.symbolicLink.contentDest + ' - ' + #linkedContent.descr + ', ' + getText('note.contentOnPageLinkTo') + ': ' + #linkedPage.titles[currentLang.code]}" />
													</s:if>
													
													<img src="<s:property value="iconImagePath" />" alt="<s:property value="linkDestination" />" title="<s:property value="linkDestination" />" />
													<%-- CAMPO DI TESTO --%>
													<wpsf:textfield useTabindexAutoIncrement="true" id="%{#htmlPrefixId+'link'+#elementStatus.count}" 
														name="%{#attributeTracer.getFormFieldName(#currentAreaLink)}_%{#elementStatus.index}" value="%{#currentAreaLink.getTextForLang(#lang.code)}"
														maxlength="254" cssClass="text" />
												 
												</s:if>
											</s:if>
										&nbsp;
										<wpsa:actionParam action="chooseLink" var="chooseLinkActionName" ><wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" /><wpsa:actionSubParam name="elementIndex" value="%{#elementStatus.index}" /><wpsa:actionSubParam name="langCode" value="%{#lang.code}" /></wpsa:actionParam>
										<wpsa:set name="iconImagePath" var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/22x22/link.png</wpsa:set>
										<wpsf:submit useTabindexAutoIncrement="true" action="%{#chooseLinkActionName}" type="image"
											value="%{getText('label.configure')}" title="%{getText('label.configure')}" src="%{#iconImagePath}" />
										</div>
									</div>
								</li>
							</s:iterator>
						</ul>
				</s:if>
				<%-- AGGIUNGI AREA --%>
				<p class="button-align">
					<wpsa:actionParam action="addImageMapArea" var="actionName" >
						<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
						<wpsa:actionSubParam name="langCode" value="%{#lang.code}" />
					</wpsa:actionParam> 
					<wpsf:submit action="%{#actionName}" useTabindexAutoIncrement="true"  cssClass="button" src="%{#iconImagePath}" value="%{getText('label.add')}" title="%{getText('label.add')}" />
				</p>
					
			</div>
			
</s:if>
<s:else>
<%-- Lingua NON di DEFAULT --%>
	<p>
		<span class="important"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>
	
		<s:if test="#defaultResource == null || #defaultResource.id == 0">
			<%-- Risorsa lingua di DEFAULT NON VALORIZZATA --%>
			<s:text name="note.editContent.doThisInTheDefaultLanguage" />.
		</s:if>
		<s:else>
			
			<%-- IMMAGINE DI DEFAULT --%> 
			<a href="<s:property value="#defaultResource.getImagePath('0')" />"><img src="<s:property value="#defaultResource.getImagePath('1')"/>" 
				alt="<s:property value="#defaultResource.descr"/>" /></a>
			
			<%-- TESTO --%>
			<wpsf:textfield useTabindexAutoIncrement="true"  id="%{#attributeTracer.getFormFieldName(#attribute.image)}" 
				name="%{#attributeTracer.getFormFieldName(#attribute.image)}" value="%{#attribute.getImage().getTextForLang(#lang.code)}"
				maxlength="254" cssClass="text" cssClass="text" />
		</s:else>
	</p>
</s:else>