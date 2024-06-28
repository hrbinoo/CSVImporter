package cz.raynet.csvimporter.csv.processing;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import cz.raynet.csvimporter.shared.exception.CSVParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVFileProcessor {
    private static final String TYPE = "text/csv";

    private boolean hasCSVFormat(final MultipartFile file) {
        final String contentType = file.getContentType();
        return TYPE.equals(contentType) || "application/vnd.ms-excel".equalsIgnoreCase(contentType);
    }

    /**
     * Parses the CSV file and returns a list of objects of the specified class
     *
     * @param file Input file to be parsed
     * @param type Class of the objects to be created
     * */
    public <T> List<T> parseCSVFile(final MultipartFile file,final Class<T> type) {
        if (!hasCSVFormat(file)) throw new CSVParsingException("Invalid file format: expected CSV", CSVFileProcessor.class);


        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            final CsvToBean<?> csvParser = new CsvToBeanBuilder<T>(reader)
                    .withSeparator(';')
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            return (List<T>) csvParser.parse();
        } catch (IOException ex) {
            throw new CSVParsingException("Error reading or parsing the CSV file", CSVFileProcessor.class, ex);
        }
    }


}
