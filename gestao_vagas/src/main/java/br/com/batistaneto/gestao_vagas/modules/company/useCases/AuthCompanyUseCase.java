package br.com.batistaneto.gestao_vagas.modules.company.useCases;

import br.com.batistaneto.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.batistaneto.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.batistaneto.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;


@Service
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Invalid username or password");
                }
        );

        var isPasswordValid = passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if(!isPasswordValid) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiredIn = Instant.now().plus(Duration.ofHours((2)));
        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiredIn)
                .withClaim("roles", Arrays.asList("COMPANY"))
                .withSubject(company.getId().toString())
                .sign(algorithm);

        return AuthCompanyResponseDTO.builder()
                .access_token(token)
                .expiredAt(expiredIn.toEpochMilli())
                .build();
    }
}
