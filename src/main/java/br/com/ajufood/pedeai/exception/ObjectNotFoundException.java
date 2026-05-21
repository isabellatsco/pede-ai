package br.com.ajufood.pedeai.exception;

/**
 * Exceção utilizada para representar objetos não encontrados na aplicação.
 *
 * Deve ser lançada quando uma busca não localizar o registro solicitado,
 * como um cliente, produto, pedido ou categoria.
 *
 * Exemplos:
 * throw new ObjectNotFoundException("Cliente não encontrado.");
 * throw new ObjectNotFoundException("Produto não encontrado para o ID informado.", e);
 */
public class ObjectNotFoundException extends RuntimeException {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção de objeto não encontrado com uma mensagem explicativa.
     *
     * @param msg mensagem que descreve o objeto não encontrado
     */
    public ObjectNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Cria uma nova exceção de objeto não encontrado com uma mensagem explicativa
     * e a causa original do erro.
     *
     * @param msg mensagem que descreve o objeto não encontrado
     * @param cause exceção original que causou este erro
     */
    public ObjectNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}