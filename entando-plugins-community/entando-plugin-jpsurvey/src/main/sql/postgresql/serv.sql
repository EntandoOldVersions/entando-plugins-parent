
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
) ;

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
) ;


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
  sex "char",
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
