CREATE TABLE tickets(
                        ticketsId int not null primary key,
                        userId int default null,
                        eventId int not null,
                        ticketType varchar(30) not null,
                        constraint User_tickets_fk
                            foreign key (UserId) references users (UserId),
                        constraint Event_tickets_fk
                            foreign key (eventId) references event(EventId)
);