package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.repository.ItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    @Transactional(readOnly = true)
    public boolean buscarPorProdutoId(Integer produtoId) {
        return itemPedidoRepository.existsByProdutoId(produtoId);
    }
}
