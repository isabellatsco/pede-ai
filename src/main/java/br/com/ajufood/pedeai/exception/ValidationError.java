package br.com.ajufood.pedeai.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilizada para representar erros de validação da API.
 *
 * Estende {@link StandardError} e adiciona uma lista com os erros
 * específicos de cada campo.
 *
 * Exemplo:
 * validationError.addError("nome", "O nome é obrigatório.");
 * validationError.addError("email", "O e-mail informado é inválido.");
 */
@Getter
public class ValidationError extends StandardError {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lista de erros de validação encontrados nos campos da requisição.
     */
    private final List<FieldMessage> errors = new ArrayList<>();

    /**
     * Adiciona um erro de validação relacionado a um campo específico.
     *
     * @param fieldName nome do campo que apresentou erro
     * @param message mensagem de erro associada ao campo
     */
    public void addError(String fieldName, String message) {

        errors.add(new FieldMessage(fieldName, message));
    }
}