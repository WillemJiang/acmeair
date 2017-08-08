package com.acmeair.morphia.services;

import com.acmeair.entities.SeckillEvent;
import com.acmeair.service.EventSourcingRegisterService;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class EventSourcingRegisterServiceImpl extends EventSourcingRegisterService<SeckillEvent> {

  private static Logger logger = Logger.getLogger(EventSourcingRegisterServiceImpl.class.getName());

  private Queue<SeckillEvent> enventqueue = new LinkedList<>();

  @Override
  public synchronized void addEvent(SeckillEvent event) {
    enventqueue.add(event);

    logger.info("add new event type = " + event.getEventType());

    //raise event to sync query
  }
}
