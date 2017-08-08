package com.acmeair.morphia.repositories;

import com.acmeair.morphia.entities.TicketImpl;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<TicketImpl, String> {
  List<TicketImpl> findByUser(String user);
}