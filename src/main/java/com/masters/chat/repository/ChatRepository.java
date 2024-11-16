package com.masters.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.masters.chat.domain.Chat;
import com.masters.chat.domain.User;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("""
            SELECT c FROM Chat c
            WHERE c.isGroup = false
            AND :user MEMBER OF c.users
            AND :reqUser MEMBER OF c.users
            """)
    Chat findSingleChatByUserIds(@Param("user") User user,
                                 @Param("reqUser") User reqUser);

    @Query("""
            SELECT c FROM Chat c
            JOIN c.users u
            WHERE u.id = :userId
            """)
    List<Chat> findChatByUserId(@Param("userId") Integer userId);
}
