package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.*;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ItemPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItemPedidoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<PedidoResumoDTO> obterPorCliente(int clienteId, String status, int pagina, int tamanho) {
        clienteService.buscarPorId(clienteId);

        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "dataHora"));
        Page<PedidoModel> pedidos;

        if (status == null || status.isEmpty()) {
            pedidos = pedidoRepository.findByClienteId(clienteId, pageable);
        }
        else {
            pedidos = pedidoRepository.findByClienteIdAndStatus(clienteId, status, pageable);
        }

        return pedidos.map(this::paraPedidoResumoDTO);
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
    private PedidoResumoDTO paraPedidoResumoDTO(PedidoModel pedido) {
        PedidoResumoDTO dto = new PedidoResumoDTO();
        dto.setId(pedido.getId());
        dto.setDataHora(pedido.getDataHora());
        dto.setStatus(pedido.getStatus());
        dto.setValorTotal(pedido.getValorTotal());

        if (pedido.getEnderecoEntrega() != null) {
            dto.setEnderecoEntrega(paraEnderecoResumoDTO(pedido.getEnderecoEntrega()));
        }

        if (pedido.getItens() != null) {
            List<ItemPedidoResumoDTO> itensDto = pedido.getItens()
                    .stream()
                    .map(this::paraItemPedidoResumoDTO)
                    .toList();
            dto.setItens(itensDto);
        }

        return dto;
    }

    private EnderecoResumoDTO paraEnderecoResumoDTO(EnderecoModel endereco) {
        EnderecoResumoDTO endDto = new EnderecoResumoDTO();
        endDto.setRua(endereco.getEndereco());
        endDto.setNumero(endereco.getNumero());
        endDto.setBairro(endereco.getBairro());
        endDto.setCidade(endereco.getCidade());
        return endDto;
    }

    private ItemPedidoResumoDTO paraItemPedidoResumoDTO(ItemPedidoModel item) {
        ItemPedidoResumoDTO itemDto = new ItemPedidoResumoDTO();
        if (item.getProduto() != null) itemDto.setNomeProduto(item.getProduto().getNome());
        itemDto.setQuantidade(item.getQuantidade());
        itemDto.setPrecoUnitario(item.getPrecoUnitario());
        itemDto.setSubTotal(item.getSubTotal());
        return itemDto;
    }
}