-- DB Port Script

CREATE TABLE jpstats_statistics
(
  ip character varying(19),
  referer character varying,
  session_id character varying(254),
  "role" character varying(254),
  "timestamp" character varying(254),
  year_value character(4),
  month_value character(2),
  day_value character(2),
  hour_value character(8),
  pagecode character varying(254),
  langcode character(2),
  useragent character varying,
  browserlang character varying(254),
  "content" character varying(16)
);