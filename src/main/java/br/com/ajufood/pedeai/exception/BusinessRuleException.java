package br.com.ajufood.pedeai.exception;

/**
 * Exceção utilizada para representar erros de regra de negócio da aplicação.
 *
 * Deve ser lançada quando uma operação não puder ser realizada por violar
 * uma regra definida pelo sistema.
 *
 * Exemplos:
 * throw new BusinessRuleException("O produto informado está inativo.");
 * throw new BusinessRuleException("Não é possível cancelar um pedido já entregue.", e);
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção de regra de negócio com uma mensagem explicativa.
     *
     * @param msg mensagem que descreve a regra de negócio violada
     */
    public BusinessRuleException(String msg) {
        super(msg);
    }

    /**
     * Cria uma nova exceção de regra de negócio com uma mensagem explicativa
     * e a causa original do erro.
     *
     * @param msg mensagem que descreve a regra de negócio violada
     * @param cause exceção original que causou este erro
     */
    public BusinessRuleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}