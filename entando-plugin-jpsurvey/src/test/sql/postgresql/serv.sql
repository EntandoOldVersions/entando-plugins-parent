CREATE TABLE jpsurvey
(
  id integer NOT NULL,
  description character varying NOT NULL,
  maingroup character varying(20) NOT NULL,
  startdate date NOT NULL,
  enddate date,
  active smallint NOT NULL,
  publicpartialresult smallint NOT NULL,
  publicresult smallint NOT NULL,
  questionnaire smallint NOT NULL,
  gatheruserinfo smallint NOT NULL,
  title character varying NOT NULL,
  restrictedaccess smallint NOT NULL,
  checkcookie smallint NOT NULL,
  checkipaddress smallint NOT NULL,
  imageid character varying(16),
  imagedescr character varying,
  CONSTRAINT jpsurvey_pkey PRIMARY KEY (id)
);

CREATE TABLE jpsurvey_questions
(
  id integer NOT NULL,
  surveyid integer NOT NULL,
  question character varying NOT NULL,
  pos smallint NOT NULL,
  singlechoice smallint NOT NULL,
  minresponsenumber smallint,
  maxresponsenumber smallint,
  CONSTRAINT jpsurvey_questions_pkey PRIMARY KEY (id),
  CONSTRAINT jpsurvey_questions_surveyid_fkey FOREIGN KEY (surveyid)
      REFERENCES jpsurvey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE jpsurvey_choices
(
  id integer NOT NULL,
  questionid integer NOT NULL,
  choice character varying NOT NULL,
  pos smallint NOT NULL,
  freetext smallint NOT NULL,
  CONSTRAINT jpsurvey_answeres_pkey PRIMARY KEY (id),
  CONSTRAINT jpsurvey_answeres_questionid_fkey FOREIGN KEY (questionid)
      REFERENCES jpsurvey_questions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
); 

CREATE TABLE jpsurvey_voters
(
  id integer NOT NULL,
  age smallint,
  country character varying(2),
  sex char,
  votedate date NOT NULL,
  surveyid integer NOT NULL,
  username character varying(30) NOT NULL,
  ipaddress character varying(15) NOT NULL,
  CONSTRAINT jpsurvey_voters_pkey PRIMARY KEY (id),
  CONSTRAINT surveyid FOREIGN KEY (surveyid)
      REFERENCES jpsurvey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE jpsurvey_responses
(
  voterid integer NOT NULL,
  questionid integer NOT NULL,
  choiceid integer NOT NULL,
  freetext character varying(30),
  CONSTRAINT choiceid FOREIGN KEY (choiceid)
      REFERENCES jpsurvey_choices (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT jpsurvey_responses_voterid_fkey FOREIGN KEY (voterid)
      REFERENCES jpsurvey_voters (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT questionid FOREIGN KEY (questionid)
      REFERENCES jpsurvey_questions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (OIDS=TRUE);

INSERT INTO jpsurvey ( id, description, maingroup, startdate, enddate, active, publicpartialresult, 
	publicresult, questionnaire, gatheruserinfo, title, restrictedaccess, checkcookie, 
	checkipaddress, imageid, imagedescr ) VALUES 
	( 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Description-1</property>
<property key="it">Descrizione-1</property>
</properties>', 'ignored', '2009-03-16', NULL, 1, 0, 1, 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Title-1</property>
<property key="it">Titolo-1</property>
</properties>', 0, 0, 0, 'IMG001', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Barrali by night</property>
<property key="it">Barrali di notte</property>
</properties>' ), 
	( 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Descrizione-2</property>
	<property key="en">Description-2</property>
</properties>', 'ignoredToo', '2008-02-06', NULL, 0, 0, 0, 0, 0, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Titolo-2</property>
	<property key="en">Title-2</property>
</properties>', 1, 0, 0, NULL, NULL );


INSERT INTO jpsurvey_questions ( id, surveyid, question, pos, singlechoice, minresponsenumber, maxresponsenumber ) VALUES 
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-1</property>
	<property key="en">Question 1-1</property>
</properties>', 2, 1, 1, 1 ), 
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Domanda 1-2</property>
	<property key="en">Question 1-2</property>
</properties>', 1, 0, 1, 2 );


INSERT INTO jpsurvey_choices ( id, questionid, choice, pos, freetext ) VALUES 
	( 1, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-1</property>
	<property key="en">Option 1-1-1</property>
</properties>', 1, 0 ), 
	( 2, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-2</property>
	<property key="en">Option 1-1-2</property>
</properties>', 3, 0 ), 
	( 3, 1, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-1-3</property>
	<property key="en">Option 1-1-3</property>
</properties>', 2, 0 ), 
	( 4, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-2</property>
	<property key="en">Option 1-2-2</property>
</properties>', 2, 0 ), 
	( 5, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-1</property>
	<property key="en">Option 1-2-1</property>
</properties>', 1, 0 ), 
	( 6, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione TESTO LIBERO</property>
	<property key="en">Option FREE TEXT</property>
</properties>', 4, 1 ), 
	( 7, 2, '<?xml version="1.0" encoding="UTF-8"?>
<properties>
	<property key="it">Opzione 1-2-3</property>
	<property key="en">Option 1-2-3</property>
</properties>', 3, 0 );


INSERT INTO jpsurvey_voters ( id, age, country, sex, votedate, surveyid, username, ipaddress ) VALUES 
	( 1, 99, 'ir', 'M', '2008-04-07', 2, 'guest', '192.168.10.1' );


INSERT INTO jpsurvey_responses ( voterid, questionid, choiceid, freetext ) VALUES
	( 1, 2, 6, 'lorem ipsum dolor' );


