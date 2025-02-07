create table if not exists academic_group
(
    id          integer not null
        primary key,
    class_group varchar(255),
    name        varchar(255)
);

create table if not exists user_account
(
    id                 integer          not null
        primary key,
    first_name        varchar(255),
    last_name               varchar(255),
    email varchar(255),
    group_id        integer
        constraint fk1mtsbur82frn64de7balymq9s
            references academic_group
);

create sequence if not exists academic_group_seq increment by 50;
create sequence if not exists user_account_seq increment by 50;



