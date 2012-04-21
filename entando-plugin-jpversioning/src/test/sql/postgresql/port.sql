CREATE TABLE jpversioning_versionedcontents
(
  id integer NOT NULL,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  status character varying(12) NOT NULL,
  contentxml character varying NOT NULL,
  versiondate timestamp without time zone NOT NULL,
  versioncode character varying(7) NOT NULL,
  onlineversion integer NOT NULL,
  approved smallint NOT NULL,
  username character varying(40),
  CONSTRAINT jpversioning_versionedcontents_pkey PRIMARY KEY (id),
  CONSTRAINT jpversioning_versionedcontents_contentid_key UNIQUE (contentid, versioncode)
);


CREATE TABLE jpversioning_trashedresources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resxml character varying NOT NULL,
  CONSTRAINT jpversioning_trashedresources_pkey PRIMARY KEY (resid)
);

INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('66', 'Attach', 'configurazione', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Attach" id="66">
<descr>configurazione</descr>
<groups mainGroup="free" />
<categories />
<instance><size>0</size><filename>configurazione.txt</filename><mimetype>application/msword</mimetype><weight>55 Kb</weight></instance>
</resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('67', 'Image', '219', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="67"><descr>219</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0219_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>2</size><filename>DSCN0219_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>1</size><filename>DSCN0219_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0219_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('68', 'Image', '220', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="68"><descr>220</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0220_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>3 Kb</weight></instance><instance><size>2</size><filename>DSCN0220_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>3 Kb</weight></instance><instance><size>1</size><filename>DSCN0220_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0220_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('69', 'Image', 'qqq', 'customers', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="69"><descr>qqq</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>DSCN0219_d3.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>2</size><filename>DSCN0219_d2.JPG</filename><mimetype>image/jpeg</mimetype><weight>4 Kb</weight></instance><instance><size>1</size><filename>DSCN0219_d1.JPG</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>0</size><filename>DSCN0219_d0.JPG</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('70', 'Image', 'tux', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="70"><descr>tux</descr><groups mainGroup="free" /><categories /><instance><size>3</size><filename>tux_d3.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>2</size><filename>tux_d2.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>1</size><filename>tux_d1.png</filename><mimetype>image/png</mimetype><weight>5 Kb</weight></instance><instance><size>0</size><filename>tux_d0.png</filename><mimetype>image/png</mimetype><weight>0 Kb</weight></instance></resource>');
INSERT INTO jpversioning_trashedresources (resid, restype, descr, maingroup, resxml) VALUES ('71', 'Image', 'Logo jAPS', 'free', '<?xml version="1.0" encoding="UTF-8"?>
<resource typecode="Image" id="398"><descr>Logo jAPS</descr><groups mainGroup="free" /><categories><category id="resCat1" /></categories><instance><size>3</size><filename>jAPS_logo_d3.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>2</size><filename>jAPS_logo_d2.jpg</filename><mimetype>image/jpeg</mimetype><weight>2 Kb</weight></instance><instance><size>1</size><filename>jAPS_logo_d1.jpg</filename><mimetype>image/jpeg</mimetype><weight>1 Kb</weight></instance><instance><size>0</size><filename>jAPS_logo_d0.jpg</filename><mimetype>image/jpeg</mimetype><weight>0 Kb</weight></instance></resource>
');




