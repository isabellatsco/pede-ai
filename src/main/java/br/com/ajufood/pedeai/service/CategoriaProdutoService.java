package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.CategoriaProdutoModel;
import br.com.ajufood.pedeai.repository.CategoriaProdutoRepository;
import br.com.ajufood.pedeai.rest.dto.request.CategoriaProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.CategoriaProdutoResponseDTO;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaProdutoService {

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public CategoriaProdutoResponseDTO obterPorId(Integer id) {
        CategoriaProdutoModel categoria = buscarPorId(id);
        return modelMapper.map(categoria, CategoriaProdutoResponseDTO.class);
    }

    public CategoriaProdutoModel buscarPorId(Integer id) {
        return categoriaProdutoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Categoria com id " + id + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public List<CategoriaProdutoResponseDTO> obterTodos() {
        return categoriaProdutoRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoriaProdutoResponseDTO.class))
                .toList();
    }

    @Transactional
    public CategoriaProdutoResponseDTO salvar(CategoriaProdutoRequestDTO novoDTO) {
        try {
            CategoriaProdutoModel novoModel = modelMapper.map(novoDTO, CategoriaProdutoModel.class);
            validarNomeParaCadastro(novoModel);
            CategoriaProdutoModel salvo = categoriaProdutoRepository.save(novoModel);
            return modelMapper.map(salvo, CategoriaProdutoResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "erro de integridade ao salvar a categoria " + novoDTO.getNome() + ": " + e
            );
        }
    }

    @Transactional
    public CategoriaProdutoResponseDTO atualizar(int id, CategoriaProdutoRequestDTO categoriaAtualizadaDTO) {
        try {

            CategoriaProdutoModel categoriaAtualizadaModel = modelMapper.map(categoriaAtualizadaDTO, CategoriaProdutoModel.class);
            CategoriaProdutoModel categoriaExistenteModel = categoriaProdutoRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Categoria com ID " + id + " não encontrada"
                    ));
            validarNomeParaAtualizacao(id, categoriaAtualizadaModel);

            modelMapper.map(categoriaAtualizadaDTO, categoriaExistenteModel);

            CategoriaProdutoModel categoriaSalva = categoriaProdutoRepository.save(categoriaExistenteModel);

            return modelMapper.map(categoriaSalva, CategoriaProdutoResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar a categoria " + categoriaAtualizadaDTO.getNome(), e
            );
        }
    }

    @Transactional
    public void deletar(Integer id) {
        try {
            obterPorId(id);
            categoriaProdutoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir a categoria, pois ela possui vínculos com outros registros", e
            );
        }
    }

    private void validarNomeParaCadastro(CategoriaProdutoModel categoria) {
        if (categoriaProdutoRepository.existsByNome(categoria.getNome())) {
            throw new ConstraintException(
                    "Já existe uma categoria cadastrada com o nome " + categoria.getNome() + "."
            );
        }
    }

    private void validarNomeParaAtualizacao(int id, CategoriaProdutoModel categoria) {
        categoriaProdutoRepository.findByNome(categoria.getNome())
                .filter(categoriaEncontrada -> categoriaEncontrada.getId() != id)
                .ifPresent(categoriaEncontrada -> {
                    throw new ConstraintException(
                            "Já existe outra categoria cadastrada com o nome " + categoria.getNome() + "."
                    );
                });
    }
}