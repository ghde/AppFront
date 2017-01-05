--
-- CREATE TABLES if not yet existing.
--

CREATE TABLE IF NOT EXISTS af_activity (
  id   INT(11)                 NOT NULL AUTO_INCREMENT,
  name VARCHAR(50)
       COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name_idx (name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS af_authentication (
  id                INT(11)                 NOT NULL AUTO_INCREMENT,
  client_id         VARCHAR(36)
                    COLLATE utf8_unicode_ci NOT NULL,
  client_public_key VARCHAR(512)
                    COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY client_id_idx (client_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS af_interest (
  id                  INT(11)                 NOT NULL AUTO_INCREMENT,
  interest_id         VARCHAR(36)
                      COLLATE utf8_unicode_ci NOT NULL,
  verify_hash         VARCHAR(64)
                      COLLATE utf8_unicode_ci NOT NULL,
  client_push_token   VARCHAR(512)
                      COLLATE utf8_unicode_ci NOT NULL,
  visibility_type     TINYINT(1)              NOT NULL DEFAULT 0,
  visibility_end_date TIMESTAMP               NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY interest_id_idx (interest_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS af_interest_activity (
  interest_id INT(11) NOT NULL,
  activity_id INT(11) NOT NULL,
  PRIMARY KEY (interest_id, activity_id),
  KEY interest_id_fk (interest_id),
  KEY activity_id_fk (activity_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS af_match_request (
  id                INT(11) NOT NULL AUTO_INCREMENT,
  interest_id       INT(11) NOT NULL,
  other_interest_id INT(11) NOT NULL,
  PRIMARY KEY (id),
  KEY interest_id_fk (interest_id),
  KEY other_interest_id_fk (other_interest_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- ADD CONSTRAINT
--

ALTER TABLE af_interest_activity
ADD CONSTRAINT interest_activity_interest_id_fk FOREIGN KEY (interest_id) REFERENCES af_interest (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE,
ADD CONSTRAINT interest_activity_activity_id_fk FOREIGN KEY (activity_id) REFERENCES af_activity (id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

ALTER TABLE af_match_request
ADD CONSTRAINT match_request_interest_id_fk FOREIGN KEY (interest_id) REFERENCES af_interest (id)
  ON UPDATE CASCADE,
ADD CONSTRAINT match_request_other_interest_id_fk FOREIGN KEY (other_interest_id) REFERENCES af_interest (id)
  ON UPDATE CASCADE;

--
-- ADD ACTIVITIES
--

INSERT INTO af_activity (name) VALUES
('Diving'),
('Downhill mountain bike'),
('River rafting'),
('Rock climbing'),
('Sailing');