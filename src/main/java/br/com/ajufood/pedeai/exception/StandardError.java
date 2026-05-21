package br.com.ajufood.pedeai.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe utilizada para padronizar o retorno de erros da API.
 *
 * Reúne as principais informações do erro, como data/hora, status HTTP,
 * descrição, mensagem e caminho da requisição.
 *
 * Exemplo:
 * {
 *     "timestamp": 1715000000000,
 *     "status": 404,
 *     "error": "Not Found",
 *     "message": "Cliente não encontrado.",
 *     "path": "/clientes/10"
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Momento em que o erro ocorreu.
     */
    private Long timestamp;

    /**
     * Código de status HTTP retornado pela API.
     */
    private Integer status;

    /**
     * Descrição resumida do erro.
     */
    private String error;

    /**
     * Mensagem detalhada sobre o erro ocorrido.
     */
    private String message;

    /**
     * Caminho da requisição que gerou o erro.
     */
    private String path;
}