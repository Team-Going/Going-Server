create table users
(
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    user_id            bigint auto_increment,
    name               varchar(255) not null,
    intro              varchar(255) not null,
    result             integer,
    platform_id        varchar(255) not null,
    platform           varchar(255) check (platform in ('KAKAO', 'APPLE')),
    refresh_token      varchar(255),
    primary key (user_id)
);

create table trip
(
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    trip_id            bigint auto_increment,
    title              varchar(255) not null,
    start_date         date         not null,
    end_date           date         not null,
    code               varchar(255) not null,
    primary key (trip_id)
);

create table participant
(
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    participant_id     bigint auto_increment,
    user_id            bigint unique,
    trip_id            bigint unique,
    role               varchar(255) check (role in ('HOST', 'PARTICIPATION')),
    style_a            integer not null,
    style_b            integer not null,
    style_c            integer not null,
    style_d            integer not null,
    style_e            integer not null,
    primary key (participant_id),
    foreign key (user_id) references users (user_id),
    foreign key (trip_id) references trip (trip_id)
);

create table todo
(
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    todo_id            bigint auto_increment,
    trip_id            bigint unique,
    title              varchar(255) not null,
    end_date           date,
    memo               varchar(255),
    secret             varchar(255) check (secret in ('OUR', 'MY')),
    progress           varchar(255) check (progress in ('INCOMPLETE', 'COMPLETE')),
    primary key (todo_id),
    foreign key (trip_id) references trip (trip_id)
);

create table allocator
(
    created_date       timestamp(6),
    last_modified_date timestamp(6),
    allocator_id       bigint auto_increment,
    participant_id     bigint unique,
    todo_id            bigint unique,
    primary key (allocator_id),
    foreign key (participant_id) references participant (participant_id),
    foreign key (todo_id) references todo (todo_id)
);