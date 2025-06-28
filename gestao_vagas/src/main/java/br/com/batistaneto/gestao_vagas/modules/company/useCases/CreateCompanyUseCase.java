package br.com.batistaneto.gestao_vagas.modules.company.useCases;

import br.com.batistaneto.gestao_vagas.exceptions.CandidateFoundException;
import br.com.batistaneto.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.batistaneto.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent((user) -> {
                    throw new CandidateFoundException();
                });

        var passwordEconded = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(passwordEconded);

        return this.companyRepository.save(companyEntity);
    }
}
