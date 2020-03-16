create table users
(
    UserId       int         not null
        primary key,
    UserNickName varchar(20) not null,
    UserMail varchar(254) not null
);
alter table users add column Operations.Role varchar(30) default 'Operations.User';
