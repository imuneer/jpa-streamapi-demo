package com.example.demo;

import org.hibernate.jpa.QueryHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.stream.Stream;

@Repository
public interface TicketRepository extends CrudRepository<ParkingTickets, Integer> {

    @org.springframework.data.jpa.repository.QueryHints(value = {
            @QueryHint(name = QueryHints.HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = QueryHints.HINT_CACHEABLE, value = "false"),
            //@QueryHint(name = QueryHints.HINT_READONLY, value = "true")
    })
    @Query("SELECT p FROM ParkingTickets p")
    Stream<ParkingTickets> collectTickets();
}
