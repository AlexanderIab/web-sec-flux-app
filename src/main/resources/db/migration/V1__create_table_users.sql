create extension if not exists "uuid-ossp";
create table users
(
    id uuid default uuid_generate_v4() primary key,
    username   varchar(64)   not null unique,
    password   varchar(2048) not null,
    role       varchar(32)   not null,
    first_name varchar(64)   not null,
    last_name  varchar(64)   not null,
    activated  boolean       not null default false,
    created_at timestamp,
    updated_at timestamp
);