package com.example.pasir_kochanski_cezary.repository;

import com.example.pasir_kochanski_cezary.model.Group;
import com.example.pasir_kochanski_cezary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMemberships_User(User user);

}
