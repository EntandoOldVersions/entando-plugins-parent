CREATE TABLE jprss_channel (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description character varying(100) NOT NULL,
    active character varying(5) NOT NULL,
    contenttype character varying(30) NOT NULL,
    filters character varying,
    feedtype character varying(10) NOT NULL,
    category character varying(30),
    maxcontentsize integer,
	 CONSTRAINT rsschannel_pkey PRIMARY KEY (id)
);

INSERT INTO authpermissions (permissionname, descr) VALUES ('jprss_channels_edit', 'Operation on RSS Channels');