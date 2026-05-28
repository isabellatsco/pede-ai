package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.*;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ItemPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PedidoResponseDTO obterPorId(int id) {
        PedidoModel pedido = buscarPorId(id);
        return converterParaPedidoResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obterTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::converterParaPedidoResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obterPorCliente(int clienteId) {
        clienteService.buscarPorId(clienteId);

        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::converterParaPedidoResponseDTO)
                .toList();
    }

    @Transactional
    public PedidoResponseDTO salvar(PedidoRequestDTO dto) {
        try {
            ClienteModel cliente = clienteService.buscarPorId(dto.getClienteId());

            EnderecoModel enderecoEntrega = cliente.getEnderecos().stream()
                    .filter(e -> e.getId() == dto.getEnderecoEntregaId())
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Endereço com id " + dto.getEnderecoEntregaId()
                                    + " não encontrado para o cliente com id " + dto.getClienteId()
                    ));

            PedidoModel pedido = modelMapper.map(dto, PedidoModel.class);
            pedido.setId(0);
            pedido.setDataHora(LocalDateTime.now());
            pedido.setStatus("AGUARDANDO PAGAMENTO");
            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(enderecoEntrega);

            List<ItemPedidoModel> itens = montarItens(dto.getItens(), pedido);
            pedido.setItens(itens);
            pedido.calcularValorTotal();

            return converterParaPedidoResponseDTO(pedidoRepository.save(pedido));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro de integridade ao salvar o pedido.", e);
        }
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(int id, String novoStatus) {
        PedidoModel pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        return converterParaPedidoResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public void deletar(int id) {
        try {
            buscarPorId(id);
            pedidoRepository.deleteById(id);
            pedidoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o pedido, pois ele possui vínculos com outros registros", e
            );
        }
    }

    public PedidoModel buscarPorId(int id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Pedido com id " + id + " não encontrado"
                ));
    }

    @Transactional
    public PedidoModel salvar(PedidoModel pedido) {
        return pedidoRepository.save(pedido);
    }

    private PedidoResponseDTO converterParaPedidoResponseDTO(PedidoModel pedido) {
        PedidoResponseDTO dto = modelMapper.map(pedido, PedidoResponseDTO.class);
        if (dto.getPagamentos() != null) {
            java.math.BigDecimal totalPago = pedido.getPagamentos().stream()
                    .map(PagamentoModel::getValorPago)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal restante = pedido.getValorTotal().subtract(totalPago);
            for (PagamentoResponseDTO p : dto.getPagamentos()) {
                p.setValorRestante(restante);
            }
        }
        return dto;
    }
    private List<ItemPedidoModel> montarItens(List<ItemPedidoRequestDTO> itensDto, PedidoModel pedido) {
        return itensDto.stream().map(itemDto -> {
            ProdutoModel produto = produtoService.buscarPorId(itemDto.getProdutoId());

            if (!produto.getDisponivel()) {
                throw new BusinessRuleException(produto.getNome() + " não está disponível");
            }

            ItemPedidoModel item = modelMapper.map(itemDto, ItemPedidoModel.class);
            item.setId(0);
            item.setPrecoUnitario(produto.getPreco());
            item.setProduto(produto);
            item.setPedido(pedido);
            item.calcularSubTotal();

            return item;
        }).toList();
    }

}