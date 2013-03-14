/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpfacetnav.aps.system.services.content;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.PublicContentSearcherDAO;

/**
 * Data Access Object for Faceted Navigation module.
 * @author E.Santoboni
 */
public class ContentFacetSearcherDAO extends PublicContentSearcherDAO implements IContentFacetSearcherDAO {
	
	@Override
	public Map<String, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> userGroupCodes) {
		Set<String> groupCodes = new HashSet<String>();
		if (null != userGroupCodes) groupCodes.addAll(userGroupCodes);
		groupCodes.add(Group.FREE_GROUP_NAME);
		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(contentTypeCodes, facetNodeCodes, groupCodes, true, conn);
			result = stat.executeQuery();
			while (result.next()) {
				String facetNode = result.getString(1);
				Integer number = new Integer(result.getInt(2));
				occurrences.put(facetNode, number);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error in loading occurrences", "getOccurrences");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return occurrences;
	}
	
	@Override
	public List<String> loadContentsId(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> userGroupCodes) {
		Set<String> groupCodes = new HashSet<String>();
		if (null != userGroupCodes) groupCodes.addAll(userGroupCodes);
		groupCodes.add(Group.FREE_GROUP_NAME);
		List<String> searchResult = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(contentTypeCodes, facetNodeCodes, groupCodes, false, conn);
			result = stat.executeQuery();
			while (result.next()) {
				String id = result.getString(1);
				if (!searchResult.contains(id)) {
					searchResult.add(id);
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error in loading list id contents", "searchForFacetNav");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return searchResult;
	}
	
	protected PreparedStatement buildStatement(List<String> contentTypeCodes, List<String> facetNodeCodes, Set<String> userGroupCodes, boolean forSelectOccurrence, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		String query = null;
		if (!forSelectOccurrence) {
			query = this.createQueryStringForSelectContents(contentTypeCodes, facetNodeCodes, groupsForSelect);
		} else {
			query = this.createQueryStringForSelectOccurrences(contentTypeCodes, facetNodeCodes, groupsForSelect);
		}
		//System.out.println("XX QUERY : " + query);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			if (null != contentTypeCodes && !contentTypeCodes.isEmpty()) {
				for (int i=0; i<contentTypeCodes.size(); i++) {
					stat.setString(++index, contentTypeCodes.get(i));
				}
			}
			if (groupsForSelect != null) {
				index = this.addGroupStatementBlock(groupsForSelect, index, stat);
			}
			if (facetNodeCodes != null && !facetNodeCodes.isEmpty()) {
				for (int i=0; i<facetNodeCodes.size(); i++) {
					stat.setString(++index, facetNodeCodes.get(i));
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error in creation statement.", "buildStatement");
		}
		return stat;
	}
	
	private String createQueryStringForSelectOccurrences(List<String> contentTypeCodes, List<String> facetNodes, 
			Collection<String> groupsForSelect) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT contentrelations.refcategory, count(contentrelations.refcategory) FROM contentrelations ")
			.append("LEFT JOIN contents on contentrelations.contentid = contents.contentid ")
			.append("WHERE contentrelations.").append(FACET_REF_FIELDS_MARKER).append(" IS NOT NULL ");
		this.addCommonBlock(contentTypeCodes, facetNodes, groupsForSelect, query);
		query.append(" GROUP BY contentrelations.").append(FACET_REF_FIELDS_MARKER);
		String queryString = query.toString();
		queryString = queryString.replaceAll(FACET_REF_FIELDS_MARKER, this.getFacetRefFieldName());
		return queryString;
	}
	
	private String createQueryStringForSelectContents(List<String> contentTypeCodes, List<String> facetNodes, 
			Collection<String> groupsForSelect) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT contents.contentid, contentrelations.").append(FACET_REF_FIELDS_MARKER)
			.append(" FROM contents LEFT JOIN contentrelations ")
			.append("ON contentrelations.contentid = contents.contentid ")
			.append("LEFT OUTER JOIN contentsearch contentsearch0 ON contents.contentid = contentsearch0.contentid ")
			.append("WHERE contentrelations.").append(FACET_REF_FIELDS_MARKER).append(" IS NOT NULL ");
		this.addCommonBlock(contentTypeCodes, facetNodes, groupsForSelect, query);
		query.append(" ORDER BY contents.lastmodified DESC");
		String queryString = query.toString();
		queryString = queryString.replaceAll(FACET_REF_FIELDS_MARKER, this.getFacetRefFieldName());
		return queryString;
	}

	private void addCommonBlock(List<String> contentTypeCodes,
			List<String> facetNodes, Collection<String> groupsForSelect,
			StringBuffer query) {
		boolean hasAppendWhereClause = true;
		if (contentTypeCodes != null && !contentTypeCodes.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append(" ( ");
			for (int i=0; i<contentTypeCodes.size(); i++) {
				if (i>0) query.append(" OR ");
				query.append(" contents.contenttype = ? ");
			}
			query.append(" ) ");
		}
		if (groupsForSelect != null && !groupsForSelect.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groupsForSelect);
		}
		if (facetNodes != null && !facetNodes.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			for (int i=0; i<facetNodes.size(); i++) {
				if (i>0) query.append(" AND ");
				query.append(" contents.contentid IN (SELECT contentid FROM ")
				.append(this.getContentRelationsTableName())
				.append(" WHERE ").append(this.getContentRelationsTableName()).append(".").append(FACET_REF_FIELDS_MARKER).append(" = ? ) ");
			}
		}
	}
	
	protected String getFacetRefFieldName() {
		return _facetRefFieldName;
	}
	public void setFacetRefFieldName(String facetRefFieldName) {
		this._facetRefFieldName = facetRefFieldName;
	}
	
	private String _facetRefFieldName;
	
	private final String FACET_REF_FIELDS_MARKER = "###FACET_REF_MARKER###";
	
}
