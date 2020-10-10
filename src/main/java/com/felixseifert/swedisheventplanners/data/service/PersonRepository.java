package com.felixseifert.swedisheventplanners.data.service;

import com.felixseifert.swedisheventplanners.data.entity.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}