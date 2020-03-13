create table tickets
(
    TicketId      int auto_increment
        primary key,
    EventId       int not null,
    UserId        int null,
    FreeTicketsId int not null,
    constraint FreeTickets_fk
        foreign key (FreeTicketsId) references freetickets (FreeTicketsId),
    constraint UserFk
        foreign key (UserId) references users (UserId),
    constraint tickets_ibfk_1
        foreign key (EventId) references event (EventId)
);

create index EventId
    on tickets (EventId);

