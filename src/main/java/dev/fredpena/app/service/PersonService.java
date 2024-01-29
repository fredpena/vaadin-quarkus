package dev.fredpena.app.service;

import dev.fredpena.app.data.Person;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 24/01/2024  - 11:53
 */

@ApplicationScoped
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> listAll(Page page, String searchTerm) {

        String searchInput = "%" + searchTerm.toLowerCase() + "%";

        return personRepository
                .findByCriteria(searchInput)
//                .findAll(Sort.descending("code"))
                .page(page)
                .list();
    }


    @Transactional
    public void createOrUpdate(Person person) {
        if (person.getCode() != null) {
            personRepository.update(person).orElseThrow(() -> new InvalidParameterException("Person not found"));
        } else {
            personRepository.create(person);
        }
    }


    @Transactional
    public void delete(long userId) {
        personRepository.deleteById(userId);
    }
}
