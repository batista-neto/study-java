package br.com.batistaneto.gestao_vagas.modules.candidate.useCases;

import br.com.batistaneto.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.batistaneto.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.batistaneto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCandidateUseCase {

    @Value("${security.token.secret.candidate}")
    private String secretKey;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTODTO) throws AuthenticationException {
        var candidate = candidateRepository.findByUsername(authCandidateRequestDTODTO.username())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Username or password incorrect");
                });

        var passwordIsValid = passwordEncoder.matches(authCandidateRequestDTODTO.password(), candidate.getPassword());

        if(!passwordIsValid) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiredAt = Instant.now().plus(Duration.ofHours(2));
        var token = JWT.create().withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withClaim("roles", Arrays.asList("CANDIDATE"))
                .withExpiresAt(expiredAt)
                .sign(algorithm);

        var authCandidateResponse = AuthCandidateResponseDTO.builder()
                .access_token(token)
                .expiredAt(expiredAt.toEpochMilli())
                .build();

        return authCandidateResponse;
    }

}
