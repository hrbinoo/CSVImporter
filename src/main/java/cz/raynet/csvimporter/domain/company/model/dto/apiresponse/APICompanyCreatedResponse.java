package cz.raynet.csvimporter.domain.company.model.dto.apiresponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APICompanyCreatedResponse {
    private boolean success;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private int id;
    }
}
