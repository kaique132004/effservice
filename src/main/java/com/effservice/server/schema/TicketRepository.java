package com.effservice.server.schema;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TicketRepository extends MongoRepository<TicketsEntity, String> {
    List<TicketsEntity> findByStatus(String status);
    List<TicketsEntity> findByPriority(String priority);
    List<TicketsEntity> findByAssignee(String assignee);
    List<TicketsEntity> findByNodeName(String nodeName);
    List<TicketsEntity> findByStatusAndPriority(String status, String priority);
    List<TicketsEntity> findByStatusAndAssignee(String status, String assignee);
    List<TicketsEntity> findByStatusAndNodeName(String status, String nodeName);
    List<TicketsEntity> findByPriorityAndAssignee(String priority, String assignee);

    // Busca o último ticket gerado pelo ticketID em ordem decrescente
    @Query(sort = "{ ticketID: -1 }") 
    Optional<TicketsEntity> findTopByOrderByTicketIDDesc();
}
