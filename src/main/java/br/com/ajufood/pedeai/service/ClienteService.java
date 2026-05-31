package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.ConstraintException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
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
        ClienteModel cliente = buscarPorId(id);
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    public ClienteModel buscarPorId(int id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public ClienteResponseDTO atualizar(int id, ClienteRequestDTO clienteAtualizadoDTO) {
        try {
            ClienteModel clienteExistenteModel = clienteRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "Cliente com ID " + id + " não encontrado"
                    ));

            ClienteModel clienteAtualizadoModel = modelMapper.map(clienteAtualizadoDTO, ClienteModel.class);
            validarCpfEmailParaAtualizacao(id, clienteAtualizadoModel);

            modelMapper.map(clienteAtualizadoDTO, clienteExistenteModel);

            ClienteModel clienteSalvo = clienteRepository.save(clienteExistenteModel);

            return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar o cliente " + clienteAtualizadoDTO.getNome(), e
            );
        }
    }

    @Transactional
    public void deletar(int id) {
        try {
            obterPorId(id);
            clienteRepository.deleteById(id);
            clienteRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o cliente, pois ele possui vínculos com outros registros", e
            );
        }
    }

    private void validarCpfEmailParaCadastro(ClienteModel cliente) {
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ConstraintException(
                    "Já existe um cliente cadastrado com o CPF " + cliente.getCpf()
            );
        }
        
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

    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> obterEnderecos(int clienteId) {
        ClienteModel cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + clienteId + " não encontrado"));
        return cliente.getEnderecos().stream()
            .map(e -> modelMapper.map(e, EnderecoResponseDTO.class))
            .toList();
    }

    @Transactional(readOnly = true)
    public EnderecoResponseDTO obterEnderecoPorId(int clienteId, int enderecoId) {
        ClienteModel cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + clienteId + " não encontrado"));
        EnderecoModel endereco = cliente.getEnderecos().stream()
            .filter(e -> e.getId() == enderecoId)
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException("Endereço com id " + enderecoId + " não encontrado para o cliente."));

        return modelMapper.map(endereco, EnderecoResponseDTO.class);
    }

    @Transactional
    public EnderecoResponseDTO salvarEndereco(int clienteId, EnderecoRequestDTO enderecoRequestDTO) {
        ClienteModel cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + clienteId + " não encontrado"));
        EnderecoModel endereco = modelMapper.map(enderecoRequestDTO, EnderecoModel.class);

        cliente.addEndereco(endereco);

        clienteRepository.save(cliente);
        return modelMapper.map(endereco, EnderecoResponseDTO.class);
    }

    @Transactional
    public EnderecoResponseDTO atualizarEndereco(int clienteId, int enderecoId, EnderecoRequestDTO enderecoRequestDTO) {
        ClienteModel cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + clienteId + " não encontrado"));

        EnderecoModel endereco = cliente.getEnderecos().stream()
            .filter(e -> e.getId() == enderecoId)
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException("Endereço com id " + enderecoId + " não encontrado para o cliente."));

        modelMapper.map(enderecoRequestDTO, endereco);

        clienteRepository.save(cliente);
        return modelMapper.map(endereco, EnderecoResponseDTO.class);
    }

    @Transactional
    public void deletarEndereco(int clienteId, int enderecoId) {
        ClienteModel cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + clienteId + " não encontrado"));

        boolean removed = cliente.getEnderecos().removeIf(e -> e.getId() == enderecoId);
        if (!removed) {
            throw new ObjectNotFoundException("Endereço com id " + enderecoId + " não encontrado para o cliente.");
        }
        clienteRepository.save(cliente);
    }
}
