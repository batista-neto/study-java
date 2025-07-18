package br.com.batistaneto.gestao_vagas.modules.candidate.useCases;

import br.com.batistaneto.gestao_vagas.exceptions.CandidateFoundException;
import br.com.batistaneto.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.batistaneto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
                .ifPresent((user) -> {
                    throw new CandidateFoundException();
                });

        var password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);
        return candidateRepository.save(candidateEntity);
    }
}
