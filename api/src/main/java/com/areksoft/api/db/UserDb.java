package com.areksoft.api.db;

import com.areksoft.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDb extends JpaRepository<User, Integer> {
}
