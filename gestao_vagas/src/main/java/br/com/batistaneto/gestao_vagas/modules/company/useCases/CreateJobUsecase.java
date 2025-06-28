package br.com.batistaneto.gestao_vagas.modules.company.useCases;

import br.com.batistaneto.gestao_vagas.modules.company.entities.JobEntity;
import br.com.batistaneto.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateJobUsecase {

    @Autowired
    private JobRepository jobRepository;

    public JobEntity execute(JobEntity jobEntity) {
        return jobRepository.save(jobEntity);
    }
}
