package com.masters.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.masters.chat.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("""
            SELECT m
            FROM Message m
            JOIN m.chat c
            WHERE c.id = :chatId
            """)
    List<Message> findMessagesByChatId(@Param("chatId") Integer chatId);

}
