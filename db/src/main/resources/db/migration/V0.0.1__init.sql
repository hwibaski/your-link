create table public.link
(
    id         bigint primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    title      varchar(255) not null,
    link_url   varchar(255) not null,
    hit_count  integer      not null,
    member_id  bigint       not null
);

create table public.member
(
    id         bigint primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    email      varchar(255) not null
        constraint member_email_unique_constraint
            unique
);

create index idx_member_email on public.member (email);

create table public.tag
(
    id         bigint primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    name       varchar(255) not null
        constraint tag_name_unique_constraint
            unique
);

create index idx_tag_name on public.tag (name);

create table public.tag_link_map
(
    id         bigint primary key,
    created_at timestamp not null,
    updated_at timestamp,
    tag_id     bigint    not null,
    link_id    bigint    not null
);

create index idx_tag_link_map_tag_id_link_id on public.tag_link_map (tag_id, link_id);
