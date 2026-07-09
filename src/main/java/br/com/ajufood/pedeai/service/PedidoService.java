package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.UnprocessableContentException;
import br.com.ajufood.pedeai.model.*;
import br.com.ajufood.pedeai.repository.PedidoRepository;
import br.com.ajufood.pedeai.rest.dto.request.ItemPedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.PedidoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.ItemPedidoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PedidoResumoDTO;
import br.com.ajufood.pedeai.rest.dto.response.StatusPedidoResponseDTO;
import br.com.ajufood.pedeai.model.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PedidoResponseDTO obterPorId(int id) {
        PedidoModel pedido = buscarPorId(id);
        return paraPedidoResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> obterTodos(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "dataHora"));
        return pedidoRepository.findAll(pageable)
                .map(this::paraPedidoResponseDTO);
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
            pedidos = pedidoRepository.findByClienteIdAndStatusIgnoreCase(clienteId, status, pageable);
        }

        return pedidos.map(this::paraPedidoResumoDTO);
    }

    @Transactional
    public StatusPedidoResponseDTO atualizarStatus(int id, String novoStatusStr) {
        PedidoModel pedido = buscarPorId(id);
        String statusAnterior = pedido.getStatus();
        
        StatusPedido novoStatus;
        try {
            novoStatus = StatusPedido.valueOf(novoStatusStr);
        } catch (IllegalArgumentException e) {
            throw new ConstraintException("Status inválido: " + novoStatusStr);
        }

        StatusPedido statusAtualEnum;
        try {
            statusAtualEnum = StatusPedido.valueOf(statusAnterior);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableContentException("Status atual do pedido é inválido para transição: " + statusAnterior);
        }

        List<StatusPedido> transicoesPossiveis = statusAtualEnum.getTransicoes();

        if (transicoesPossiveis.isEmpty()) {
            throw new UnprocessableContentException("Não é possível alterar o status de um pedido " + statusAnterior);
        }

        if (!transicoesPossiveis.contains(novoStatus)) {
            throw new UnprocessableContentException("Transição inválida. Do status " + statusAnterior + " só é possível ir para: " + transicoesPossiveis);
        }

        pedido.setStatus(novoStatus.name());
        pedidoRepository.save(pedido);

        return new StatusPedidoResponseDTO(
                pedido.getId(),
                statusAnterior,
                novoStatus.name(),
                LocalDateTime.now()
        );
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
    public PedidoResponseDTO salvar(PedidoRequestDTO dto) {
        try {
            ClienteModel cliente = clienteService.buscarPorId(dto.getClienteId());

            EnderecoModel enderecoEntrega = cliente.getEnderecos().stream()
                    .filter(e -> e.getId() == dto.getEnderecoEntregaId())
                    .findFirst()
                    .orElseThrow(() -> new UnprocessableContentException(
                            "Endereço com id " + dto.getEnderecoEntregaId()
                                    + " não encontrado para o cliente com id " + dto.getClienteId()
                    ));

            Set<Integer> vistos = new HashSet<>();
            if (dto.getItens().stream().anyMatch(item -> !vistos.add(item.getProdutoId()))) {
                throw new ConstraintException("Não é permitido produtos duplicados na lista de itens");
            }

            PedidoModel pedido = new PedidoModel();
            pedido.setDataHora(LocalDateTime.now());
            pedido.setStatus("AGUARDANDO_CONFIRMACAO");
            pedido.setCliente(cliente);
            pedido.setEnderecoEntrega(enderecoEntrega);

            for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
                ProdutoModel produto;
                try {
                    produto = produtoService.buscarPorId(itemDto.getProdutoId());
                } catch (ObjectNotFoundException e) {
                    throw new UnprocessableContentException("Produto com id " + itemDto.getProdutoId() + " não encontrado");
                }

                if (!produto.getDisponivel()) {
                    throw new UnprocessableContentException(produto.getNome() + " não está disponível");
                }
                pedido.adicionarItem(produto, itemDto.getQuantidade());
            }

            return paraPedidoResponseDTO(pedidoRepository.save(pedido));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro de integridade ao salvar o pedido.", e);
        }
    }

    private PedidoResponseDTO paraPedidoResponseDTO(PedidoModel pedido) {
        PedidoResponseDTO dto = modelMapper.map(pedido, PedidoResponseDTO.class);
        if (dto.getPagamentos() != null) {
            BigDecimal totalPago = pedido.getPagamentos().stream()
                    .map(PagamentoModel::getValorPago)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal restante = pedido.getValorTotal().subtract(totalPago);
            for (PagamentoResponseDTO p : dto.getPagamentos()) {
                p.setValorRestante(restante);
            }
        }
        return dto;
    }

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