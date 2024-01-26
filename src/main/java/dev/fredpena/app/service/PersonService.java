package dev.fredpena.app.service;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.security.InvalidParameterException;
import java.util.List;

@ApplicationScoped
@AllArgsConstructor
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
