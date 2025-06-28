package br.com.batistaneto.gestao_vagas.exceptions;

public class CandidateFoundException extends RuntimeException {
    public CandidateFoundException(){
        super("Usuário ja existe");
    }
}
