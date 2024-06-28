package cz.raynet.csvimporter.application;

import cz.raynet.csvimporter.domain.company.model.dto.company.CompanyDTO;
import cz.raynet.csvimporter.domain.company.service.CompanyService;
import cz.raynet.csvimporter.shared.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/upload")
    public Mono<List<CompanyDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return companyService.companyFileHandler(file);
        } catch (Exception e) {
            log.error("Error processing file: {}", file.getOriginalFilename(), e);
            throw new FileProcessingException("Failed to process file: " + file.getOriginalFilename());
        }
    }
}
