create procedure addTickets(IN numberOfTickets int, IN EventIdInput int, IN freeTicketsFieldId int)
BEGIN

    l:while numberOfTickets >0 do
    insert  into tickets (EventId,FreeTicketsId) values(EventIdInput,freeTicketsFieldId);
    set numberOfTickets=numberOfTickets - 1;
    end while l;
end;

create procedure createEvent(IN name varchar(30), IN num int, IN type varchar(30))
begin

    insert ignore into event(EventName)
    values(name);
    insert into freeTickets(numberoftickets,EventId,TicketType)
    values(num,(Select EventID from Event where name=EventName),type);
end;
