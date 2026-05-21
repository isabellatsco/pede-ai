package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ClienteResponseDTO obterPorId(int id) {
        ClienteModel cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + id + " não encontrado")
                );

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Transactional
    public List<ClienteResponseDTO> obterTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .toList();
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO novoDTO) {
        try {
            ClienteModel novoModel = modelMapper.map(novoDTO, ClienteModel.class);
            validarCpfEmailParaCadastro(novoModel);
            ClienteModel salvo = clienteRepository.save(novoModel);
            return modelMapper.map(salvo, ClienteResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "erro de integridade ao salvar o cliente " + novoDTO.getNome() + ": " + e
            );
        }
    }


    private void validarCpfEmailParaCadastro(ClienteModel cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o CPF " + cliente.getCpf()
            );

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o e-mail " + cliente.getEmail()
            );

        }
    }
        private void validarCpfEmailParaAtualizacao(int id, ClienteModel cliente) {
            clienteRepository.findByCpf(cliente.getCpf())
                    .filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                    .ifPresent(clienteEncontrado -> {
                        throw new ConstraintException(
                                "Já existe outro cliente cadastrado com o CPF " + cliente.getCpf() + "."
                        );
                    });

            clienteRepository.findByEmail(cliente.getEmail())
                    .filter(clienteEncontrado -> clienteEncontrado.getId() != id)
                    .ifPresent(clienteEncontrado -> {
                        throw new ConstraintException(
                                "Já existe outro cliente cadastrado com o e-mail " + cliente.getEmail() + "."
                        );
                    });
        }
    