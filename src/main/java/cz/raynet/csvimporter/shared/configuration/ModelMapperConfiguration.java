package cz.raynet.csvimporter.shared.configuration;

import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyAddressDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyAddressesDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyContactInfoDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APINewCompanyDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyAddressDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyDTO;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.enums.Rating;
import cz.raynet.csvimporter.domain.company.model.enums.Role;
import cz.raynet.csvimporter.domain.company.model.enums.State;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        final var modelMapper = new ModelMapper();
        configureCompanyToAPINewCompanyDTO(modelMapper);
        configureCompanyToAPICompanyAddressesDTO(modelMapper);
        configureCompanyToUpdateCompanyAddressDTO(modelMapper);
        configureCompanyToUpdateCompanyDTO(modelMapper);
        return modelMapper;
    }

    private void configureCompanyToAPINewCompanyDTO(final ModelMapper modelMapper) {
        modelMapper.createTypeMap(Company.class, APINewCompanyDTO.class).addMappings(new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setRegNumber(source.getRegNumber());
                map().setName(source.getTitle());

                // ONLY FOR SHOWCASE PURPOSES
                map().setRating(Rating.A);
                map().setState(State.A_POTENTIAL);
                map().setRole(Role.D_RIVAL);

            }
        });
    }

    private void configureCompanyToAPICompanyAddressesDTO(final ModelMapper modelMapper) {
        modelMapper.typeMap(Company.class, APICompanyAddressesDTO.class).setConverter(context -> {
            final var company = context.getSource();
            final var apiCompanyAddressesDTO = new APICompanyAddressesDTO();
            final var apiCompanyContactInfoDTO = new APICompanyContactInfoDTO();

            // ONLY FOR SHOWCASE PURPOSES
            var apiCompanyAddressDTO = new APICompanyAddressDTO();
            apiCompanyAddressDTO.setName("Ostrava");

            apiCompanyContactInfoDTO.setEmail(company.getEmail());
            apiCompanyContactInfoDTO.setTel1(company.getPhone());
            apiCompanyAddressesDTO.setAddress(apiCompanyAddressDTO);
            apiCompanyAddressesDTO.setContactInfo(apiCompanyContactInfoDTO);

            return apiCompanyAddressesDTO;
        });
    }

    private void configureCompanyToUpdateCompanyDTO(final ModelMapper modelMapper) {
        modelMapper.typeMap(Company.class, UpdateCompanyDTO.class).setConverter(context -> {
            final var company = context.getSource();
            final var updateCompanyDTO = new UpdateCompanyDTO();
            updateCompanyDTO.setName(company.getTitle());
            return updateCompanyDTO;
        });
    }

    private void configureCompanyToUpdateCompanyAddressDTO(final ModelMapper modelMapper) {
        modelMapper.typeMap(Company.class, UpdateCompanyAddressDTO.class).setConverter(context -> {
            final var company = context.getSource();
            final var apiCompanyAddressDTO = new APICompanyAddressDTO();
            final var apiCompanyContactInfoDTO = new APICompanyContactInfoDTO();

            apiCompanyAddressDTO.setName("Ostrava");

            apiCompanyContactInfoDTO.setEmail(company.getEmail());
            apiCompanyContactInfoDTO.setTel1(company.getPhone());

            final var updateCompanyAddressDTO = new UpdateCompanyAddressDTO();
            updateCompanyAddressDTO.setAddress(apiCompanyAddressDTO);
            updateCompanyAddressDTO.setContactInfo(apiCompanyContactInfoDTO);

            return updateCompanyAddressDTO;
        });
    }

}
