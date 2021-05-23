insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (1, 'A52TC78', 'TURner34', 'Caesar', 'Turner', 'Homme', STR_TO_DATE('05-12-1972', '%d-%m-%Y'), 'Montreal',
        '106 rue Papineau', 'H76 JGH', 'Caesarturner12@gmail.com', '5145879595', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (2, 'SR6G5SR', 'Ing56$ld', 'Ingerfield', 'Marie-noël', 'Femme', STR_TO_DATE('28-03-1987', '%d-%m-%Y'), 'Québec',
        '84 boulevard pépinière', '3T7 9GL', 'Marie-noelIng@gmail.com', '5147895248', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (3, 'AZUK5QE', 'Eli258', 'Leindecker', 'Eliès', 'Homme', STR_TO_DATE('09-11-2012', '%d-%m-%Y'), 'Saint-Jean',
        '114 rue Joviale', 'J67 6U5', 'Elies2598@gmail.com', '5148963575', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (4, '641AZ3H', 'JellaKo', 'Al-Simen', 'Jellal', 'Homme', STR_TO_DATE('15-07-1954', '%d-%m-%Y'), 'Montreal',
        '25 boulevard Amor', 'RTJ 57H', 'Jellalibou@gmail.com', '4506969768', FALSE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (5, '64QES45', 'zeDvhj98', 'Cutridge', 'Gaétane', 'Femme', STR_TO_DATE('06-06-1986', '%d-%m-%Y'), 'Saint-Jean',
        '114 rue Joviale', 'J67 6U5', 'Gaga78@gmail.com', '4503485271', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (6, '3541ASE', 'Asli9874', 'Normand', 'Åslï', 'Femme', STR_TO_DATE('24-01-1999', '%d-%m-%Y'), 'Montreal',
        '87 avenue Jalue', 'JHG 58F', 'aslily@gmail.com', '5148989758', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (7, 'QSGER42', 'FredeJop', 'Normand', 'Frédérique', 'Homme', STR_TO_DATE('07-09-68', '%d-%m-%Y'), 'Longeuil',
        '25 rue Desfond', 'GJI 5JI', 'SpainFeder@gmail.com', '5146738282', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (8, 'AZG85EZ', 'Julie759', 'Vetmann', 'Thomas', 'Homme', STR_TO_DATE('17-02-1985', '%d-%m-%Y'), 'Trois-rivières',
        '02 rue perdu', 'AZ8 9RE', 'ThomasVietnam@gmail.com', '4506892574', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (9, '68JZ4ZT', 'ImLiedTo', 'Caesar', 'Liè', 'Autre', STR_TO_DATE('11-11-1994', '%d-%m-%Y'), 'Montreal',
        '106 rue Papineau', 'H76 JGH', 'lily45@gmail.com', '5147826985', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (10, 'VAZET1Y', 'qerthqeh', 'Caesar', 'Valérie', 'Femme', STR_TO_DATE('06-08-1948', '%d-%m-%Y'), 'Montreal',
        '108 rue Papineau', 'H79 JGH', 'Valebou@gmail.com', '5149863257', TRUE);
insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (11, 'JODNZ52', '$hfg$254', 'Leindecker', 'Christophe', 'Homme', STR_TO_DATE('15-04-82', '%d-%m-%Y'),
        'Saint-Jean', '114 rue Joviale', 'J67 6U5', 'cleinden@gmail.com', '5143587539', TRUE);


insert into child_parent (id_child, id_parent)
values (3, 11);
insert into child_parent (id_child, id_parent)
values (3, 10);
insert into child_parent (id_child, id_parent)
values (3, 5);
insert into child_parent (id_child, id_parent)
values (1, 10);
insert into child_parent (id_child, id_parent)
values (9, 1);

insert into citizen_infos_covid (id_citizen_infos_covid, type, nbr_dose, citizen_id, date_test, is_current)
VALUES (1, 'VACCIN', 1, 1, NOW() - INTERVAL 1 DAY, TRUE);
insert into citizen_infos_covid (id_citizen_infos_covid, type, results, citizen_id, date_test, is_current)
VALUES (2, 'TEST', 'NEGATIVE', 3, NOW() - INTERVAL 1 DAY, TRUE);
insert into citizen_infos_covid (id_citizen_infos_covid, type, results, citizen_id, date_test, is_current)
VALUES (3, 'TEST', 'POSITIVE', 5, NOW() - INTERVAL 2 DAY, TRUE);
insert into citizen_infos_covid (id_citizen_infos_covid, type, results, citizen_id, date_test, is_current)
VALUES (4, 'TEST', 'NEGATIVE', 11, NOW() - INTERVAL 2 DAY, TRUE);
insert into citizen_infos_covid (id_citizen_infos_covid, type, nbr_dose, citizen_id, date_test, is_current)
VALUES (5, 'VACCIN', 2, 10, NOW() - INTERVAL 8 DAY, TRUE);

insert into citizen (citizen_id, social_ins_nbr, password, last_name, first_name, sex, birth_date, city, address,
                     postal_code, email, phone_number, is_active)
values (100, '125HFR8', 'TOTO1234', 'Renaud', 'Vincent', 'Homme', STR_TO_DATE('15-04-82', '%d-%m-%Y'), 'Saint-Jean',
        '114 rue Joviale', 'J67 6U5', 'v.pisano35@gmail.com', '5143587539', TRUE);
insert into citizen_infos_covid (id_citizen_infos_covid, type, results, citizen_id, date_test, is_current)
VALUES (100, 'TEST', 'NEGATIVE', 100, NOW()  - INTERVAL 2 DAY, false);
insert into citizen_infos_covid (id_citizen_infos_covid, type, nbr_dose, citizen_id, date_test, is_current)
VALUES (101, 'VACCIN', 2, 100, NOW() - INTERVAL 2 DAY, TRUE);