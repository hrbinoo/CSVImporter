package cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class APICompanyResponse implements Serializable {
    private String success;
    private int totalCount;
    @JsonProperty("data")
    private List<APICompanyResponseDTO> data;

    @Override
    public String toString() {
        return "APICompanyResponse{" +
                "success='" + success + '\'' +
                ", totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
