package cz.raynet.csvimporter.domain.company.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Company {
    @Id
    private long id;
    private String regNumber;
    private String title;
    private String email;
    private String phone;

    public Company(String title, String email, String phone) {
        this.title = title;
        this.email = email;
        this.phone = phone;
    }

}
