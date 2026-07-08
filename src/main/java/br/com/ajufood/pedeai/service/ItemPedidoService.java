package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Transactional(readOnly = true)
    public boolean buscarPorProdutoId(Integer produtoId) {
        return itemPedidoRepository.existsByProdutoId(produtoId);
    }
}
