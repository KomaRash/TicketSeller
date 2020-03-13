create table place
(
    PlaceID int auto_increment
        primary key,
    Name    varchar(30) null,
    Address varchar(40) not null
);