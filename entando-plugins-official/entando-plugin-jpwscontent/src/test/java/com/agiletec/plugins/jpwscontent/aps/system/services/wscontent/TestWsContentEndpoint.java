/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.agiletec.plugins.jpwscontent.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointProxy;
import com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEnvelope;

public class TestWsContentEndpoint extends ApsPluginBaseTestCase {

	public void testGetContent() throws Throwable {
		WsContentEndpointProxy proxy = new WsContentEndpointProxy();
		proxy.setEndpoint("http://localhost:8080/PortalExample/services/WsContentEndpoint");
		WsContentEnvelope envelope = proxy.getContent("ART180");
		assertNotNull(envelope);
		assertEquals(CONTENT_ART180.trim(), envelope.getContent().trim());
		WsResource[] resources = envelope.getResources();
		assertEquals(1, resources.length);
		WsResource resource = resources[0];
		assertEquals("logo", resource.getDescription());
		assertEquals("44", resource.getId());
		assertEquals("image/jpeg", resource.getMimeType());
		assertEquals("lvback_d0.jpg", resource.getNameFile());
		assertEquals("Image", resource.getTypeCode());
	}

	// WARNING! After running this test you have to clean the database used by the target application 
	public void testAddContent() throws Throwable {
		WsContentEndpointProxy proxy = new WsContentEndpointProxy();
		proxy.setEndpoint("http://localhost:8080/PortalExample/services/WsContentEndpoint");
		WsContentEnvelope envelope = this.createEnvelope();
		int status = proxy.addContent(envelope);
		assertEquals(IWsContentManager.RECIVING_OK, status);
	}

	private WsContentEnvelope createEnvelope() throws Throwable {
		WsContentEnvelope envelope = new WsContentEnvelope();
		envelope.setContent(SIMPLE_CONTENT);
		WsResource[] resources = new WsResource[1];
		resources[0]= this.getNewResources("T1");
		envelope.setResources(resources);
		return envelope;
	}

	private WsResource getNewResources(String id) throws Throwable {
		WsResource risorsa = new WsResource();
		risorsa.setDescription("TestX1"+id);
		risorsa.setId(id);
		File file = new File("target/test/jAPS_logo.jpg");
		risorsa.setFileBinary(this.getBytesFromFile(file));
		risorsa.setNameFile("remoteImage"+id+".jpg");
		risorsa.setMimeType("jpg");
		risorsa.setTypeCode("Image");
		return risorsa;
	}

	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+ file.getName());
		}
		is.close();
		return bytes;
	}
	

	private final String SIMPLE_CONTENT = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<content id=\"\" typecode=\"ART\" typedescr=\"Articolo rassegna stampa\">" +
		"<descr>remote content</descr><groups mainGroup=\"free\">" +
		"</groups>" +
		"<categories />" +
		"<attributes>" +
		"<attribute name=\"Titolo\" attributetype=\"Text\"><text lang=\"it\">Titolo remote content</text><text lang=\"en\">Title remote content</text></attribute>" +
		"<list attributetype=\"Monolist\" name=\"Autori\" nestedtype=\"Monotext\" />" +
		"<attribute name=\"VediAnche\" attributetype=\"Link\" />" +
		"<attribute name=\"CorpoTesto\" attributetype=\"Hypertext\" />" +
		"<attribute name=\"Foto\" attributetype=\"Image\"><resource resourcetype=\"Image\" id=\"T1\" /><text lang=\"it\">remote image</text></attribute>" +
		"<attribute name=\"Data\" attributetype=\"Date\" />" +
		"</attributes>" +
		"<status>Bozza</status>" +
		"</content>";

	private final String CONTENT_ART180 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
	"<content id=\"ART180\" typecode=\"ART\" typedescr=\"Articolo rassegna stampa\"><descr>una descrizione</descr><groups mainGroup=\"free\" /><categories><category id=\"cat1\" /></categories><attributes><attribute name=\"Titolo\" attributetype=\"Text\" /><list attributetype=\"Monolist\" name=\"Autori\" nestedtype=\"Monotext\" /><attribute name=\"VediAnche\" attributetype=\"Link\" /><attribute name=\"CorpoTesto\" attributetype=\"Hypertext\" /><attribute name=\"Foto\" attributetype=\"Image\"><resource resourcetype=\"Image\" id=\"44\" lang=\"it\" /><text lang=\"it\">Descrizione foto</text></attribute><attribute name=\"Data\" attributetype=\"Date\" /><attribute name=\"Numero\" attributetype=\"Number\" /></attributes><status>READY</status><version>1.0</version></content>";

}
