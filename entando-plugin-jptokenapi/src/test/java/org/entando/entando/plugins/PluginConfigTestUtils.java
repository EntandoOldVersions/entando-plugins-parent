/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins;

import com.agiletec.ConfigTestUtils;

/**
 * @author E.Santoboni
 */
public class PluginConfigTestUtils extends ConfigTestUtils {
	
	@Override
	protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[9];
		filePaths[0] = "classpath:spring/propertyPlaceholder.xml";
		filePaths[1] = "classpath:spring/baseSystemConfig.xml";
		filePaths[2] = "classpath*:spring/aps/**/**.xml";
		filePaths[3] = "classpath*:spring/apsadmin/**/**.xml";
		filePaths[4] = "classpath*:spring/plugins/jacms/aps/**/**.xml";
		filePaths[5] = "classpath*:spring/plugins/jacms/apsadmin/**/**.xml";
		filePaths[6] = "classpath*:spring/plugins/jptokenapi/aps/ormComponentConfig.xml";
		filePaths[7] = "classpath*:spring/plugins/jptokenapi/aps/managers/**/**.xml";
		filePaths[8] = "classpath*:spring/plugins/jptokenapi/apsadmin/**/**.xml";
		return filePaths;
    }
	
}