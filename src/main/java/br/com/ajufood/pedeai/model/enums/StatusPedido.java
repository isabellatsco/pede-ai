package br.com.ajufood.pedeai.model.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum StatusPedido {
    AGUARDANDO_CONFIRMACAO,
    CONFIRMADO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    public List<StatusPedido> getTransicoes() {
        return switch (this) {
            case AGUARDANDO_CONFIRMACAO -> Arrays.asList(CONFIRMADO, CANCELADO);
            case CONFIRMADO -> Arrays.asList(EM_PREPARO, CANCELADO);
            case EM_PREPARO -> Arrays.asList(SAIU_PARA_ENTREGA, CANCELADO);
            case SAIU_PARA_ENTREGA -> Arrays.asList(ENTREGUE, CANCELADO);
            default -> Collections.emptyList();
        };
    }
}
