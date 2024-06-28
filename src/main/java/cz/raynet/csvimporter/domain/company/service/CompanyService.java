package cz.raynet.csvimporter.domain.company.service;

import cz.raynet.csvimporter.domain.company.model.dto.company.CompanyDTO;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompanyService {
    Mono<List<CompanyDTO>> companyFileHandler(MultipartFile file);
}
