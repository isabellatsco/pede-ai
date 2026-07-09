package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.repository.FormaPagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaPagamentoService {

    private final FormaPagamentoRepository formaPagamentoRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public FormaPagamentoResponseDTO obterPorId(int id) {
        return converterParaResponse(buscarPorId(id));
    }

    public FormaPagamentoModel buscarPorId(int id) {
        return formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Forma de pagamento com ID " + id + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoResponseDTO> obterTodas() {
        return formaPagamentoRepository.findAll().stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional
    public FormaPagamentoResponseDTO salvar(FormaPagamentoRequestDTO dto) {
        validarNomeUnico(dto.getNome());

        FormaPagamentoModel novaForma = modelMapper.map(dto, FormaPagamentoModel.class);
        FormaPagamentoModel salva = formaPagamentoRepository.save(novaForma);

        return converterParaResponse(salva);
    }

    @Transactional
    public FormaPagamentoResponseDTO atualizar(int id, FormaPagamentoRequestDTO dto) {
        FormaPagamentoModel formaExistente = buscarPorId(id);

        validarNomeUnico(dto.getNome(), id);

        modelMapper.map(dto, formaExistente);
        FormaPagamentoModel atualizada = formaPagamentoRepository.save(formaExistente);

        return converterParaResponse(atualizada);
    }

    @Transactional
    public void deletar(int id) {
        try {
            FormaPagamentoModel forma = buscarPorId(id);

            formaPagamentoRepository.delete(forma);
            formaPagamentoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não é possível excluir esta forma de pagamento, pois ela possui vínculos com outros registros", e
            );
        }
    }

    private FormaPagamentoResponseDTO converterParaResponse(FormaPagamentoModel model) {
        return modelMapper.map(model, FormaPagamentoResponseDTO.class);
    }

    private void validarNomeUnico(String nome) {
        if (formaPagamentoRepository.existsByNomeIgnoreCase(nome)) {
            throw new ConstraintException("Já existe uma forma de pagamento cadastrada com o nome " + nome);
        }
    }

    private void validarNomeUnico(String nome, int id) {
        formaPagamentoRepository.findByNomeIgnoreCase(nome)
                .filter(f -> f.getId() != id)
                .ifPresent(f -> {
                    throw new ConstraintException("Já existe outra forma de pagamento cadastrada com o nome " + nome + ".");
                });
    }
}