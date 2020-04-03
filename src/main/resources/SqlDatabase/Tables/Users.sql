create table users
(
    UserId       int                              not null
        primary key,
    UserNickName varchar(20)                      not null,
    UserMail     varchar(254)                     not null,
    Role         varchar(30) default 'Authorized' null,
    Password     varchar(30)                      not null,
    constraint UniqueUser
        unique (UserNickName, UserMail)
);

