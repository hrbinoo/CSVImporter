package cz.raynet.csvimporter.domain.company.model.dto.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSVCompanyDTO {
    @CsvBindByName(column = "regNumber")
    private String regNumber;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "phone")
    private String phone;
}
