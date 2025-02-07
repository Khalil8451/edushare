
INSERT INTO academic_group (id, class_group, name) VALUES (nextval('academic_group_seq'), 'ENG-A1', 'ENGINEER');
INSERT INTO academic_group (id, class_group, name) VALUES (nextval('academic_group_seq'), 'MAS-A2', 'MASTER-COMPUTER-SCIENCE');


INSERT INTO public.user_account (id, first_name, last_name, email, group_id)
VALUES
    (nextval('user_account_seq'), 'khalil', 'guesmi', 'khalilguesmi@gmail.com', (SELECT id FROM academic_group WHERE name = 'ENGINEER')),
    (nextval('user_account_seq'), 'rami', 'chargui', 'ramichargui@gmail.com', (SELECT id FROM academic_group WHERE name = 'MASTER-COMPUTER-SCIENCE')),
    (nextval('user_account_seq'), 'amir', 'abdellaoui', 'amirabdellaoui@gmail.com', (SELECT id FROM academic_group WHERE name = 'ENGINEER')),
    (nextval('user_account_seq'), 'ahmed', 'missaoui', 'ahmedmissaoui@gmail.com', (SELECT id FROM academic_group WHERE name = 'ENGINEER')),
    (nextval('user_account_seq'), 'nidhal', 'chemkhi', 'nidhalchemkhi@gmail.com', (SELECT id FROM academic_group WHERE name = 'ENGINEER'));

