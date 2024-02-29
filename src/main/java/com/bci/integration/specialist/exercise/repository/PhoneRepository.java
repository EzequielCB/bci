package com.bci.integration.specialist.exercise.repository;

import com.bci.integration.specialist.exercise.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
}
