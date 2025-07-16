package br.com.batistaneto.gestao_vagas.exceptions;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException() {
        super("Job não encontrado");
    }
}
