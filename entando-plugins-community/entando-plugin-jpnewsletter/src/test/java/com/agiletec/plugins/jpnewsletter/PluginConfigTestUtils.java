/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
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
package com.agiletec.plugins.jpnewsletter;

import com.agiletec.ConfigTestUtils;

/**
 * @author E.Santoboni
 */
public class PluginConfigTestUtils extends ConfigTestUtils {
	
	@Override
	protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[5];
		filePaths[0] = "classpath:spring/systemConfig.xml";
		filePaths[1] = "classpath:spring/aps/managers/**/**.xml";
		filePaths[2] = "classpath:spring/apsadmin/**/**.xml";
		filePaths[3] = "classpath*:spring/plugins/**/aps/managers/**/**.xml";
		filePaths[4] = "classpath*:spring/plugins/**/apsadmin/**/**.xml";  	
		return filePaths;
    }
	
}
