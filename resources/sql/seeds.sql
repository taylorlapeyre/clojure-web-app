
-- TAYLOR
INSERT INTO users (email, password, first_name, last_name)
VALUES('taylor@email.com', 'password123', 'Taylor', 'Lapeyre'),
      ('jeff@email.com', 'password123', 'Jeff', 'McPerson');

INSERT INTO groups (name, user_id)
VALUES('Christmas 2014', 1);

INSERT INTO users_groups (user_id, group_id)
VALUES (1, 1),
       (2, 1);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Taylor\'s Christmas List', 1, 1),
       ('Jeff\'s Christmas List', 1, 2);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Rubix Cube', 15, 1, '/public/images/system/sdh324kj.png'),
       ('Volcano', 600, 1, '/public/images/system/sdfl346.png'),
       ('Ear Rings', 86, 1, '/public/images/system/lmhjf46.png'),

       ('Stir Crazy', 23, 2, '/public/images/system/rthfhg4.png'),
       ('iPhone 6', 299, 2, '/public/images/system/iuh434.png');

-- #####################################################

-- ANDREW
INSERT INTO users (email, password, first_name, last_name)
VALUES('TheBerg@email.com', 'three333', 'Andrew', 'Bergeron'),
  ('Mikeyyy@email.com', 'mypass333', 'Michael', 'Bergeron');

INSERT INTO groups (name, user_id)
VALUES('Bergeron Christmas', 3);

INSERT INTO users_groups (user_id, group_id)
VALUES (3, 2),
  (4, 2);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Andrew\’s Christmas List', 2, 3),
  ('Bergeron\’s Christmas List', 2, 4);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Playstation 4', 299, 3, '/public/images/system/ps4.png'),
  ('Purple Erkle', 300, 3, '/public/images/system/purp_nugs.png'),
  ('Rolex', 5000, 3, '/public/images/system/rolex.png'),


  ('Xbox One', 349, 3, '/public/images/system/xbox1.png'),
  ('iPad', 299, 3, '/public/images/system/ipad3.png'),
  ('HTC One M8', 599, 3, '/public/images/system/htcMe.png');

-- #####################################################

-- KELSEY
INSERT INTO users (email, password, first_name, last_name)
VALUES('kelsey@email.com', 'password123', 'Kelsey', 'Cameron'),
  ('looloo@email.com', 'password123', 'Laura', 'DeLoach');

INSERT INTO groups (name, user_id)
VALUES('Girls\' Christmas 2014', 5);

INSERT INTO users_groups (user_id, group_id)
VALUES (5, 3),
  (6, 3);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Kelsey\'s Christmas List', 3, 5),
  ('Laura\'s Christmas List', 3, 6);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('David Yurman Bracelet', 780, 5, '/public/images/system/sdfsdfpph3244kj.png'),
  ('Pandora Charm', 40, 5, '/public/images/system/ssdfdfl348396.png'),
  ('Fossil Watch', 87, 5, '/public/images/system/lmhsdfjf46r.png'),

  ('Hammock', 67, 6, '/public/images/system/ksdk34.png'),
  ('Chromecast', 35, 6, '/public/images/system/kjfiw984.png'),
  ('White Beanie', 12, 6, '/public/images/system/4iw4.png');

-- #####################################################

-- ALEX
INSERT INTO users (email, password, first_name, last_name)
VALUES('bob@email.com', 'password123', 'bill', 'bob'),
      ('jo@email.com', 'password123', 'bobby', 'jo');

INSERT INTO groups (name, user_id)
VALUES('super christmas', 7);

INSERT INTO users_groups (user_id, group_id)
VALUES (7, 4),
       (8, 4);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Taylor\'s Christmas List', 4, 7),
  ('Jeff\'s Christmas List', 4, 8);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Rubix Cube', 16, 7, '/public/images/system/sdh324kj.png'),
  ('Volcano', 600, 7, '/public/images/system/sdfl346.png'),
  ('Ear Rings', 86, 7, '/public/images/system/lmhjf46.png'),

  ('Stir Crazy', 23, 8, '/public/images/system/rthfhg4.png'),
  ('iPhone 6', 299, 8, '/public/images/system/iuh434.png');

-- #####################################################

-- KELSEY 2
INSERT INTO users (email, password, first_name, last_name)
VALUES('erin@email.com', 'lolhaha3', 'Erin', 'Norton'),
  ('cravycarol@email.com', 'password123', 'Carol', 'Furr');

INSERT INTO groups (name, user_id)
VALUES('Roommate Christmas 2k14', 9);

INSERT INTO users_groups (user_id, group_id)
VALUES (9, 4),
  (10, 4);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Erin\'s Christmas List', 4, 9),
  ('Carol\'s Christmas List', 4, 10);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Notebook', 8, 9, '/public/images/system/zxc45j.png'),
  ('Mechanical Keyboard', 100, 9, '/public/images/system/s96.png'),

  ('Wine Glass', 12, 10, '/public/images/system/uiodk67.png'),
  ('Sharpie', 6, 10, '/public/images/system/isw854.png');

-- #####################################################

-- KELSEY 3
INSERT INTO users (email, password, first_name, last_name)
VALUES('kelli@email.com', 'lolhaha3', 'Kelli', 'Albert'),
  ('savanna@email.com', 'password123', 'Savannah', 'Ronco');

INSERT INTO groups (name, user_id)
VALUES('12/25/14', 11);

INSERT INTO users_groups (user_id, group_id)
VALUES (11, 5),
  (12, 5);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Kelli\'s Christmas List', 5, 11),
  ('Savannah\'s Christmas List', 5, 12);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Butterfinger Bites', 4, 11, '/public/images/system/zxc4yyy5j.png'),
  ('Creative Cloud', 999, 11, '/public/images/system/s96yy8.png'),

  ('Kindle', 52, 12, '/public/images/system/uioyyydk7.png'),
  ('Andriod Charger', 3, 12, '/public/images/system/isw8554q4.png');

-- #####################################################

-- KELSEY 4
INSERT INTO users (email, password, first_name, last_name)
VALUES('alex@email.com', 'lolhaha3', 'Alex', 'Clavelle'),
  ('adam@email.com', 'santalover', 'Adam', 'Clavelle');

INSERT INTO groups (name, user_id)
VALUES('Brother bonding', 13);

INSERT INTO users_groups (user_id, group_id)
VALUES (13, 6),
  (14, 6);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('Alex\'s Christmas List', 6, 13),
  ('Adam\'s Christmas List', 6, 14);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('50in TV', 500, 13, '/public/images/system/zrt8j.png'),
  ('Xbox One', 450, 13, '/public/images/system/kdu2.png'),

  ('Golf club', 93, 14, '/public/images/system/upsdj2.png'),
  ('Raybans', 88, 14, '/public/images/system/kdns11.png');


-- #####################################################

-- John
INSERT INTO users (email, password, first_name, last_name)
VALUES('johnanny@email.com', 'helloworld1', 'John', 'Anny'),
  ('jackanny@email.com', 'jackjill1', 'Jack', 'Anny'),
  ('jessicamarie@email.com', 'jasmine17', 'Jessica', 'Anny');

INSERT INTO groups (name, user_id)
VALUES('Anny Squad', 15);

INSERT INTO users_groups (user_id, group_id)
VALUES (15, 7),(16, 7),(17, 7);

INSERT INTO wheels (name, group_id, user_id)
VALUES ('John\'s Christmas List', 7, 15),
  ('Jack\'s Christmas List', 7, 16),
  ('Jessica\'s Christmas List', 7, 17);

INSERT INTO items (name, price, wheel_id, image)
VALUES ('Samsung 40in TV', 450, 15 , '/public/images/system/tvsam.png'),
  ('Xbox One', 325, 15, '/public/images/system/kdu2.png'),
  ('Nexus 6', 678, 15, '/public/images/system/nex6.png'),

  ('LSU BowTie', 65, 16 , '/public/images/system/bowtie7.png'),
  ('Northface Vest', 120, 16, '/public/images/system/vest1.png'),
  ('Ralph Lauren Blazer', 310, 16, '/public/images/system/upsdbj69.png'),

  ('Steve Madden Boots', 93, 17, '/public/images/system/sexy37.png'),
  ('Fossil Purse', 93, 17, '/public/images/system/stripepurse.png'),
  ('Padma Sleeveless Dress', 298, 17, '/public/images/system/bcbgdress.png'),
  ('Itunes Gift Card', 88, 17, '/public/images/system/cardgift.png');