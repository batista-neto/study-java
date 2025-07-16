package br.com.batistaneto.gestao_vagas.modules.company.controllers;

import br.com.batistaneto.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.batistaneto.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.batistaneto.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.batistaneto.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static br.com.batistaneto.utils.TestUtils.generateToken;
import static br.com.batistaneto.utils.TestUtils.objectToJSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {

        var company = CompanyEntity.builder().description("COMPANY_DESCRIPTION")
                .email("email@company.com")
                .password("1234567890")
                .username("COMPPANY_USERNAME")
                .name("COMPANY_NAME")
                .build();

        companyRepository.saveAndFlush(company);

        var createJobDto = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST").level("LEVEL_TEST").build();

        var result = this.mvc.perform(MockMvcRequestBuilders.post("/company/job/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJSON(createJobDto))
                                .header("Authorization",  "Bearer " + generateToken(company.getId(), secretKey))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result);
    }

    @Test
    public void should_not_be_able_to_create_a_new_job_if_company_not_found() throws Exception {

        var createJobDto = CreateJobDTO.builder()
                .benefits("BENEFITS_TEST")
                .description("DESCRIPTION_TEST").level("LEVEL_TEST").build();


            this.mvc.perform(MockMvcRequestBuilders.post("/company/job/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectToJSON(createJobDto))
                    .header("Authorization", "Bearer " + generateToken(UUID.randomUUID(), secretKey))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
