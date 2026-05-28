package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.model.PagamentoModel;
import br.com.ajufood.pedeai.model.PedidoModel;
import br.com.ajufood.pedeai.repository.PagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.PagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentoResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.PagamentosPedidoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PagamentosPedidoDTO obterPorPedidoId(int pedidoId) {
        PedidoModel pedido = pedidoService.buscarPorId(pedidoId);

        List<PagamentoModel> pagamentos = pagamentoRepository.findByPedidoId(pedidoId);
        BigDecimal totalPago = pagamentos.stream()
                .map(PagamentoModel::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PagamentoResponseDTO> pagamentosDTO = pagamentos.stream()
                .map(p -> {
                    PagamentoResponseDTO dto = modelMapper.map(p, PagamentoResponseDTO.class);
                    dto.setValorRestante(pedido.getValorTotal().subtract(totalPago));
                    return dto;
                })
                .toList();

        return new PagamentosPedidoDTO(pagamentosDTO);
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO obterPorId(int id) {
        PagamentoModel pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Pagamento com id " + id + " não encontrado"));

        List<PagamentoModel> pagamentos = pagamentoRepository.findByPedidoId(pagamento.getPedido().getId());
        BigDecimal totalPago = pagamentos.stream()
                .map(PagamentoModel::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagamentoResponseDTO dto = modelMapper.map(pagamento, PagamentoResponseDTO.class);
        dto.setValorRestante(pagamento.getPedido().getValorTotal().subtract(totalPago));
        return dto;
    }

    @Transactional
    public PagamentoResponseDTO salvar(PagamentoRequestDTO dto) {
        PedidoModel pedido = pedidoService.buscarPorId(dto.getPedidoId());

        List<PagamentoModel> pagamentosExistentes = pagamentoRepository.findByPedidoId(dto.getPedidoId());
        BigDecimal totalPagoAnterior = pagamentosExistentes.stream()
                .map(PagamentoModel::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagoAnterior.compareTo(pedido.getValorTotal()) >= 0) {
            throw new ConstraintException("Pedido já está 100% pago");
        }

        BigDecimal novoTotalPago = totalPagoAnterior.add(dto.getValorPago());
        if (novoTotalPago.compareTo(pedido.getValorTotal()) > 0) {
            throw new ConstraintException("O valor informado é maior que o restante do pedido");
        }

        FormaPagamentoModel formaPagamento = formaPagamentoService.buscarPorId(dto.getFormaPagamentoId());

        PagamentoModel pagamento = modelMapper.map(dto, PagamentoModel.class);
        pagamento.setId(0);
        pagamento.setPedido(pedido);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setDataHora(LocalDateTime.now());

        PagamentoModel salvo = pagamentoRepository.save(pagamento);

        if (novoTotalPago.compareTo(pedido.getValorTotal()) == 0) {
            pedido.setStatus("PAGO");
        } else {
            pedido.setStatus("AGUARDANDO PAGAMENTO");
        }
        pedidoService.salvar(pedido);

        PagamentoResponseDTO responseDTO = modelMapper.map(salvo, PagamentoResponseDTO.class);
        responseDTO.setValorRestante(pedido.getValorTotal().subtract(novoTotalPago));
        return responseDTO;
    }
}