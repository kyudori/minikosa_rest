package com.kosa.mini.api.repository;


import com.kosa.mini.api.entity.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<ContactUs,Integer> {

}
