package br.com.ajufood.pedeai.exception;

/**
 * Exceção utilizada para representar erros de integridade dos dados.
 *
 * Deve ser lançada quando uma operação violar a consistência dos dados,
 * como tentar excluir um registro relacionado a outros registros.
 *
 * Exemplos:
 * throw new DataIntegrityException("Não é possível excluir um cliente com pedidos vinculados.");
 * throw new DataIntegrityException("Erro ao salvar os dados do pedido.", e);
 */
public class DataIntegrityException extends RuntimeException {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção de integridade de dados com uma mensagem explicativa.
     *
     * @param msg mensagem que descreve o erro de integridade dos dados
     */
    public DataIntegrityException(String msg) {
        super(msg);
    }

    /**
     * Cria uma nova exceção de integridade de dados com uma mensagem explicativa
     * e a causa original do erro.
     *
     * @param msg mensagem que descreve o erro de integridade dos dados
     * @param cause exceção original que causou este erro
     */
    public DataIntegrityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}