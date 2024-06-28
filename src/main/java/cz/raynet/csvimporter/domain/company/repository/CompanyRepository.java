package cz.raynet.csvimporter.domain.company.repository;

import cz.raynet.csvimporter.domain.company.model.entity.Company;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CompanyRepository extends ReactiveCrudRepository<Company, Long> {
    Mono<Company> findByRegNumber(Mono<String> regNumber);
}
