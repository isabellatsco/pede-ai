package br.com.ajufood.pedeai.service;


import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.ProdutoRepository;
import br.com.ajufood.pedeai.repository.CategoriaProdutoRepository;

import br.com.ajufood.pedeai.rest.dto.request.ProdutoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ProdutoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProdutoResponseDTO obterPorId(Integer id) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Produto com id " + id + " não encontrado")
                );
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> obterTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, ProdutoResponseDTO.class))
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {
        try {
            ProdutoModel produtoModel = modelMapper.map(produtoRequestDTO, ProdutoModel.class);
            produtoModel.setId(0);

            var categoria = categoriaProdutoRepository.findById(produtoRequestDTO.getCategoriaId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Categoria com id " + produtoRequestDTO.getCategoriaId() + " não encontrada"
                    ));

            produtoModel.setCategoria(categoria);

            ProdutoModel salvo = produtoRepository.save(produtoModel);
            return modelMapper.map(salvo, ProdutoResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "erro de integridade ao salvar o produto " + produtoRequestDTO.getNome(), e);
        }
    }


    @Transactional
    public ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO produtoAtualizadoDTO) {
        try {
            ProdutoModel produtoExistente = produtoRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Produto com ID " + id + " não encontrado"
                    ));

            var categoria = categoriaProdutoRepository.findById(produtoAtualizadoDTO.getCategoriaId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Categoria com id " + produtoAtualizadoDTO.getCategoriaId() + " não encontrada"
                    ));

            produtoExistente.setNome(produtoAtualizadoDTO.getNome());
            produtoExistente.setDescricao(produtoAtualizadoDTO.getDescricao());
            produtoExistente.setPreco(produtoAtualizadoDTO.getPreco());
            produtoExistente.setDisponivel(produtoAtualizadoDTO.getDisponivel());
            produtoExistente.setCategoria(categoria);

            ProdutoModel salvo = produtoRepository.save(produtoExistente);
            return modelMapper.map(salvo, ProdutoResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro de integridade ao atualizar o produto " + produtoAtualizadoDTO.getNome() + ".", e);
        }
    }

    @Transactional
    public void deletar(int id) {
        try {
            obterPorId(id);
            produtoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o produto, pois ele possui vínculos com outros registros", e
            );
        }
    }

}
