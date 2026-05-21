package br.com.ajufood.pedeai.exception;

/**
 * Exceção utilizada para representar erros relacionados ao acesso ao banco de dados.
 *
 * Deve ser lançada quando ocorrer algum problema ao executar operações SQL,
 * como falha em consultas, inserções, atualizações ou exclusões.
 *
 * Exemplos:
 * throw new DatabaseException("Erro ao consultar os pedidos do cliente.");
 * throw new DatabaseException("Erro ao salvar o produto no banco de dados.", e);
 */
public class DatabaseException extends RuntimeException {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção de SQL com uma mensagem explicativa.
     *
     * @param msg mensagem que descreve o erro ocorrido no banco de dados
     */
    public DatabaseException(String msg) {
        super(msg);
    }

    /**
     * Cria uma nova exceção de SQL com uma mensagem explicativa
     * e a causa original do erro.
     *
     * @param msg mensagem que descreve o erro ocorrido no banco de dados
     * @param cause exceção original que causou este erro
     */
    public DatabaseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}