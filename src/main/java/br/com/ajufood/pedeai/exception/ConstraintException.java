package br.com.ajufood.pedeai.exception;

/**
 * Exceção utilizada para representar erros de restrição ou integridade dos dados.
 *
 * Deve ser lançada quando uma operação violar alguma restrição da aplicação
 * ou do banco de dados, como chave estrangeira, campo obrigatório ou valor duplicado.
 *
 * Exemplos:
 * throw new ConstraintException("Não é possível excluir uma categoria vinculada a produtos.");
 * throw new ConstraintException("Já existe um cliente cadastrado com este CPF.", e);
 */
public class ConstraintException extends RuntimeException {

    /**
     * Identifica a versão da classe na serialização.
     * O valor {@code 1L} representa a versão 1, sendo {@code L} o tipo long.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria uma nova exceção de restrição com uma mensagem explicativa.
     *
     * @param msg mensagem que descreve a restrição violada
     */
    public ConstraintException(String msg) {
        super(msg);
    }

    /**
     * Cria uma nova exceção de restrição com uma mensagem explicativa
     * e a causa original do erro.
     *
     * @param msg mensagem que descreve a restrição violada
     * @param cause exceção original que causou este erro
     */
    public ConstraintException(String msg, Throwable cause) {
        super(msg, cause);
    }
}