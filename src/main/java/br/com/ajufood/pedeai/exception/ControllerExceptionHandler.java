package br.com.ajufood.pedeai.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Classe responsável por tratar as exceções lançadas pela aplicação.
 *
 * <p>
 * Centraliza o tratamento dos erros e padroniza as respostas retornadas pela API.
 * </p>
 *
 * <p>Exemplos:</p>
 * <pre>
 * throw new ObjectNotFoundException("Cliente não encontrado.");
 * throw new BusinessRuleException("Não é possível cancelar um pedido já entregue.");
 * </pre>
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Trata exceções de objeto não encontrado.
     *
     * @param e exceção lançada quando o objeto não é localizado
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 404
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    /**
     * Trata exceções de conteúdo não processável
     *
     * @param e exceção lançada quando requisição não pode ser processado
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 422
     */
    @ExceptionHandler(UnprocessableContentException.class)
    public ResponseEntity<StandardError> unprocessableContent(UnprocessableContentException e, HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Conteúdo não processável",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(err);
    }

    /**
     * Trata exceções relacionadas à integridade dos dados.
     *
     * @param e exceção lançada ao violar a integridade dos dados
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 400
     */
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrity(
            DataIntegrityException e,
            HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Integridade de dados",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Trata exceções de violação de integridade de dados
     *
     * @param e exceção lançada pelo Spring Data
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 400
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(
            DataIntegrityViolationException e,
            HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Integridade de dados",
                "Esse registro possui vínculos com outros",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Trata exceções relacionadas a restrições de dados.
     *
     * @param e exceção lançada quando alguma restrição é violada
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 400
     */
    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<StandardError> constraint(
            ConstraintException e,
            HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Restrição de dados",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    /**
     * Trata exceções relacionadas a regras de negócio.
     *
     * @param e exceção lançada quando uma regra de negócio é violada
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 409
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandardError> businessRule(
            BusinessRuleException e,
            HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.CONFLICT.value(),
                "Regra de negócio",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    /**
     * Trata exceções relacionadas ao banco de dados.
     *
     * @param e exceção lançada ao ocorrer falha em operações SQL
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 500
     */
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> sql(
            DatabaseException e,
            HttpServletRequest request) {

        StandardError err = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro de conexão com o banco de dados",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    /**
     * Trata erros de validação dos dados enviados na requisição.
     *
     * <p>
     * Esse método é acionado quando algum campo anotado com validações,
     * como {@code @NotBlank}, {@code @NotNull} ou {@code @Email}, é inválido.
     * </p>
     *
     * @param ex exceção gerada pela falha de validação
     * @param request dados da requisição HTTP
     * @return resposta padronizada com status 400 e lista de campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        ValidationError err = new ValidationError();
        err.setTimestamp(System.currentTimeMillis());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Erro de validação");
        err.setMessage("Um ou mais campos estão inválidos.");
        err.setPath(request.getRequestURI());

        /*
         * Percorre os campos inválidos e adiciona cada erro à resposta.
         * Exemplo: campo "email" com a mensagem "E-mail inválido".
         */
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            err.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}