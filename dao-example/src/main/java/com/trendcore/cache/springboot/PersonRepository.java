package com.trendcore.cache.springboot;

import com.trendcore.core.domain.Person;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends GemfireRepository<Person,Long> {

    List<Person> findByLastName(String lastName);

}
