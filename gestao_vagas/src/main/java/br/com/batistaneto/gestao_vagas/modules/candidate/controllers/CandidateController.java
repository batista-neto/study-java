package br.com.batistaneto.gestao_vagas.modules.candidate.controllers;

import br.com.batistaneto.gestao_vagas.exceptions.CandidateFoundException;
import br.com.batistaneto.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.batistaneto.gestao_vagas.modules.candidate.dto.ProfileCandidateDTO;
import br.com.batistaneto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import br.com.batistaneto.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
import br.com.batistaneto.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.batistaneto.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import br.com.batistaneto.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import br.com.batistaneto.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato")
public class CandidateController {

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Autowired
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadastro de canditato", description = "Esta função é responsável por cadastrar um cadidato")
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try{
            var result = createCandidateUseCase.execute(candidateEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do candidato", description = "Esta funcao desvolve informacao do candidato")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            schema = @Schema(implementation = ProfileCandidateDTO.class)
                    )
            }),
            @ApiResponse(responseCode = "400")
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> get(HttpServletRequest request) {
        var idCandidate = request.getAttribute("candidate_id");
        try{
            var profile = profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listagem de vagas disponíveis para o candidato", description = "Esta função é responsável por listar as vagas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                    )
            })
    })
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> findJobbyFilter(@RequestParam String filter) {
        return listAllJobsByFilterUseCase.execute(filter);
    }

    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Inscrição do candidato para uma vaga", description = "Esta função é responsável por realizar a inscrição do candidato em uma vaga")
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID idJob) {
        var idCandidate = request.getAttribute("candidate_id");
        try {
            var result = this.applyJobCandidateUseCase.execute(UUID.fromString(idCandidate.toString()), idJob);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
