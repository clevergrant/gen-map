INSERT INTO Events (eventID, descendant, personID, latitude, longitude, country, city, eventType, year)
VALUES ('1','userName','1',1,1,'A Country','A City','Birth',1992);

INSERT INTO AuthTokens (token, userName)
VALUES ('1','userName');

INSERT INTO Persons (personID, descendant, firstName, lastName, gender, father, mother, spouse)
VALUES ('1','userName','Grant','Perdue','m','2','3','4');

INSERT INTO Users (userName, password, email, firstName, lastName, gender, personID)
VALUES ('userName','password','clever@grant.me','Grant','Perdue','m','1');