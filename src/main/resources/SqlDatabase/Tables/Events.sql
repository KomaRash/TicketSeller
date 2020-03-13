create table event
(
    EventId   int auto_increment not null primary key ,
    EventName varchar(30) null,
    Preview   text        not null,
    DATETIME  datetime    not null,
    PlaceID   int         not null
);
alter TABLE event Add constraint event_place_fk
    foreign key(PlaceID) references place(PlaceID);
