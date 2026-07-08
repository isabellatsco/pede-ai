package br.com.ajufood.pedeai.service;


import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.model.ProdutoModel;
import br.com.ajufood.pedeai.repository.ProdutoRepository;

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
    private CategoriaProdutoService categoriaProdutoService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ProdutoResponseDTO obterPorId(Integer id) {
        ProdutoModel produto = buscarPorId(id);
        return converterParaResponse(produto);
    }

    @Transactional(readOnly = true)
    public ProdutoModel buscarPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Produto com id " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> obterTodos(Integer categoriaId) {
        var produtos = (categoriaId == null)
                ? produtoRepository.findByDisponivelTrue()
                : produtoRepository.findByDisponivelTrueAndCategoria_Id(categoriaId);

        return produtos.stream()
                .map(c -> modelMapper.map(c, ProdutoResponseDTO.class))
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoRequestDTO) {
        try {
            ProdutoModel produtoModel = modelMapper.map(produtoRequestDTO, ProdutoModel.class);

            var categoria = categoriaProdutoService.buscarPorId(produtoRequestDTO.getCategoriaId());
            produtoModel.setCategoria(categoria);

            ProdutoModel salvo = produtoRepository.save(produtoModel);
            return converterParaResponse(salvo);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "erro de integridade ao salvar o produto " + produtoRequestDTO.getNome(), e);
        }
    }


    @Transactional
    public ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO produtoAtualizadoDTO) {
        try {
            ProdutoModel produtoExistente = buscarPorId(id);

            var categoria = categoriaProdutoService.buscarPorId(produtoAtualizadoDTO.getCategoriaId());

            modelMapper.map(produtoAtualizadoDTO, produtoExistente);
            produtoExistente.setId(id);
            produtoExistente.setCategoria(categoria);

            ProdutoModel salvo = produtoRepository.save(produtoExistente);
            return converterParaResponse(salvo);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Erro de integridade ao atualizar o produto " + produtoAtualizadoDTO.getNome() + ".", e);
        }
    }

    @Transactional
    public void deletar(Integer id) {
        try {
            buscarPorId(id);
            produtoRepository.deleteById(id);
            produtoRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o produto, pois ele possui vínculos com outros registros", e
            );
        }
    }

    private ProdutoResponseDTO converterParaResponse(ProdutoModel produto) {
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }
}
