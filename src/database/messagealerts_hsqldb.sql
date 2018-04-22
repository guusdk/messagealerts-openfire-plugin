CREATE TABLE ofmsgalerts (
  id bigint IDENTITY PRIMARY KEY,
  toJID varchar(255) NOT NULL,
  body LONGVARCHAR NOT NULL
);

INSERT INTO ofVersion (name, version) VALUES ('messagealerts', 0);
