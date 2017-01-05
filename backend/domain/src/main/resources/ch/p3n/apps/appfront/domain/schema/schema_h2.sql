--
-- CREATE TABLES if not yet existing.
--

CREATE TABLE IF NOT EXISTS af_activity (
  id   INT(11)     NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS af_authentication (
  id                INT(11)     NOT NULL AUTO_INCREMENT,
  client_id         VARCHAR(36) NOT NULL,
  client_public_key TEXT        NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS af_interest (
  id                  INT(11)      NOT NULL AUTO_INCREMENT,
  interest_id         VARCHAR(36)  NOT NULL,
  verify_hash         VARCHAR(64)  NOT NULL,
  client_push_token   VARCHAR(512) NOT NULL,
  visibility_type     TINYINT      NOT NULL DEFAULT 0,
  visibility_end_date TIMESTAMP    NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS af_interest_activity (
  interest_id INT(11) NOT NULL,
  activity_id INT(11) NOT NULL,
  PRIMARY KEY (interest_id, activity_id)
);

CREATE TABLE IF NOT EXISTS af_match_request (
  id                INT(11) NOT NULL AUTO_INCREMENT,
  interest_id       INT(11) NOT NULL,
  other_interest_id INT(11) NOT NULL,
  PRIMARY KEY (id)
);

--
-- ADD CONSTRAINT
--

ALTER TABLE af_activity ADD CONSTRAINT name_idx UNIQUE(name);

ALTER TABLE af_authentication ADD CONSTRAINT client_id_idx UNIQUE(client_id);

ALTER TABLE af_interest ADD CONSTRAINT interest_id_idx UNIQUE(interest_id);

ALTER TABLE af_interest_activity
ADD CONSTRAINT interest_activity_interest_id_fk FOREIGN KEY (interest_id) REFERENCES af_interest (id)
ON UPDATE CASCADE
ON DELETE CASCADE;

ALTER TABLE af_interest_activity
ADD CONSTRAINT interest_activity_activity_id_fk FOREIGN KEY (activity_id) REFERENCES af_activity (id)
ON UPDATE CASCADE
ON DELETE CASCADE;

ALTER TABLE af_match_request
ADD CONSTRAINT match_request_interest_id_fk FOREIGN KEY (interest_id) REFERENCES af_interest (id)
ON UPDATE CASCADE;

ALTER TABLE af_match_request
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