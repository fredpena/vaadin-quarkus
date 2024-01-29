package dev.fredpena.app.service;

import dev.fredpena.app.data.Person;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * @author me@fredpena.dev
 * @created 24/01/2024  - 11:55
 */
@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {


    public PanacheQuery<Person> findByCriteria(String searchTerm) {

        String query = """
                lower(firstName) LIKE :firstName OR lower(lastName) LIKE :lastName OR lower(email) LIKE :email\s
                OR lower(phone) LIKE :phone OR lower(role) LIKE :role OR lower(occupation) LIKE :occupation
                """;

        Parameters parameters = Parameters
                .with("firstName", searchTerm)
                .and("lastName", searchTerm)
                .and("email", searchTerm)
                .and("phone", searchTerm)
                .and("role", searchTerm)
                .and("occupation", searchTerm);

        return find(query, Sort.descending("code"), parameters);
    }

    public void create(Person person) {

        persist(person);

    }

    public Optional<Person> update(Person person) {
        final var id = person.getCode();
        var savedOpt = findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setFirstName(person.getFirstName());
        saved.setLastName(person.getLastName());
        saved.setEmail(person.getEmail());
        saved.setPhone(person.getPhone());
        saved.setDateOfBirth(person.getDateOfBirth());
        saved.setOccupation(person.getOccupation());
        saved.setRole(person.getRole());
        saved.setImportant(person.isImportant());

        return Optional.of(saved);
    }
}
