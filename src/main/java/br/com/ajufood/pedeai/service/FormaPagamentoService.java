package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.FormaPagamentoModel;
import br.com.ajufood.pedeai.repository.FormaPagamentoRepository;
import br.com.ajufood.pedeai.rest.dto.request.FormaPagamentoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.FormaPagamentoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public FormaPagamentoResponseDTO obterPorId(int id) {
        FormaPagamentoModel forma = buscarPorId(id);
        return modelMapper.map(forma, FormaPagamentoResponseDTO.class);
    }

    public FormaPagamentoModel buscarPorId(int id) {
        return formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Forma de pagamento com ID " + id + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<FormaPagamentoResponseDTO> obterTodas() {
        return formaPagamentoRepository.findAll().stream()
                .map(f -> modelMapper.map(f, FormaPagamentoResponseDTO.class))
                .toList();
    }

    @Transactional
    public FormaPagamentoResponseDTO salvar(FormaPagamentoRequestDTO dto) {
        if (formaPagamentoRepository.existsByNome(dto.getNome())) {
            throw new ConstraintException("Já existe uma forma de pagamento cadastrada com o nome " + dto.getNome());
        }

        FormaPagamentoModel novaForma = modelMapper.map(dto, FormaPagamentoModel.class);
        FormaPagamentoModel salva = formaPagamentoRepository.save(novaForma);
        return modelMapper.map(salva, FormaPagamentoResponseDTO.class);
    }

    @Transactional
    public FormaPagamentoResponseDTO atualizar(int id, FormaPagamentoRequestDTO dto) {
        FormaPagamentoModel formaExistente = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Forma de pagamento com ID " + id + " não encontrada"));

        formaPagamentoRepository.findByNome(dto.getNome())
                .filter(f -> f.getId() != id)
                .ifPresent(f -> {
                    throw new ConstraintException("Já existe outra forma de pagamento cadastrada com o nome " + dto.getNome() + ".");
                });

        modelMapper.map(dto, formaExistente);

        FormaPagamentoModel atualizada = formaPagamentoRepository.save(formaExistente);
        return modelMapper.map(atualizada, FormaPagamentoResponseDTO.class);
    }

    @Transactional
    public void deletar(int id) {
        try {
            FormaPagamentoModel forma = formaPagamentoRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Forma de pagamento com ID " + id + " não encontrada"));

            formaPagamentoRepository.delete(forma);
            formaPagamentoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não é possível excluir esta forma de pagamento, pois ela possui vínculos com outros registros", e
            );
        }
    }
}