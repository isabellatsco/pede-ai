package br.com.ajufood.pedeai.service;

import br.com.ajufood.pedeai.exception.BusinessRuleException;
import br.com.ajufood.pedeai.exception.DataIntegrityException;
import br.com.ajufood.pedeai.exception.ObjectNotFoundException;
import br.com.ajufood.pedeai.exception.UnprocessableContentException;
import br.com.ajufood.pedeai.model.ClienteModel;
import br.com.ajufood.pedeai.model.EnderecoModel;
import br.com.ajufood.pedeai.repository.ClienteRepository;
import br.com.ajufood.pedeai.rest.dto.request.ClientePatchRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.ClienteRequestDTO;
import br.com.ajufood.pedeai.rest.dto.request.EnderecoRequestDTO;
import br.com.ajufood.pedeai.rest.dto.response.ClienteResponseDTO;
import br.com.ajufood.pedeai.rest.dto.response.EnderecoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public ClienteResponseDTO obterPorId(int id) {
        ClienteModel cliente = buscarPorId(id);
        return converterParaResponse(cliente);
    }

    public ClienteModel buscarPorId(int id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cliente com id " + id + " não encontrado"));
    }

    private EnderecoModel buscarEndereco(ClienteModel cliente, int enderecoId) {
        return cliente.getEnderecos().stream()
                .filter(e -> e.getId() == enderecoId)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Endereço com id " + enderecoId + " não encontrado para o cliente."));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obterTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO novoDTO) {
        try {
            ClienteModel novoModel = modelMapper.map(novoDTO, ClienteModel.class);
            if (novoDTO.getEndereco() != null) {
                EnderecoModel endereco = modelMapper.map(novoDTO.getEndereco(), EnderecoModel.class);
                novoModel.addEndereco(endereco);
            }
            validarCpfEmailParaCadastro(novoModel, null);
            ClienteModel salvo = clienteRepository.save(novoModel);
            return converterParaResponse(salvo);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "erro de integridade ao salvar o cliente " + novoDTO.getNome() + ": " + e
            );
        }
    }


    @Transactional
    public ClienteResponseDTO atualizar(int id, ClientePatchRequestDTO clienteAtualizadoDTO) {
        try {
            ClienteModel clienteExistenteModel = buscarPorId(id);

            if (clienteAtualizadoDTO.getEmail() != null) {
                verificarDuplicado(clienteRepository.findByEmail(clienteAtualizadoDTO.getEmail()), id, "e-mail",
                        clienteAtualizadoDTO.getEmail());
                clienteExistenteModel.setEmail(clienteAtualizadoDTO.getEmail());
            }

            if (clienteAtualizadoDTO.getNome() != null) {
                clienteExistenteModel.setNome(clienteAtualizadoDTO.getNome());
            }

            if (clienteAtualizadoDTO.getTelefone() != null) {
                clienteExistenteModel.setTelefone(clienteAtualizadoDTO.getTelefone());
            }

            ClienteModel clienteSalvo = clienteRepository.save(clienteExistenteModel);

            return converterParaResponse(clienteSalvo);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Erro de integridade ao atualizar o cliente " + id, e
            );
        }
    }

    @Transactional
    public void deletar(int id) {
        try {
            buscarPorId(id);
            clienteRepository.deleteById(id);
            clienteRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não foi possível excluir o cliente, pois ele possui vínculos com outros registros", e
            );
        }
    }

    private void validarCpfEmailParaCadastro(ClienteModel cliente, Integer idExistente) {
        verificarDuplicado(clienteRepository.findByCpf(cliente.getCpf()), idExistente, "CPF", cliente.getCpf());
        verificarDuplicado(clienteRepository.findByEmail(cliente.getEmail()), idExistente, "e-mail", cliente.getEmail());
    }

    private void verificarDuplicado(Optional<ClienteModel> encontrado, Integer idExistente, String campo, String valor) {
        encontrado
                .filter(clienteEncontrado -> idExistente == null || clienteEncontrado.getId() != idExistente)
                .ifPresent(clienteEncontrado -> {
                    throw new BusinessRuleException(
                            "Já existe um cliente cadastrado com o " + campo + " " + valor + "."
                    );
                });
    }

    private ClienteResponseDTO converterParaResponse(ClienteModel cliente) {
        ClienteResponseDTO dto = modelMapper.map(cliente, ClienteResponseDTO.class);
        dto.setCpf(dto.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<EnderecoResponseDTO> obterEnderecos(int clienteId) {
        ClienteModel cliente = buscarPorId(clienteId);
        return cliente.getEnderecos().stream()
            .map(e -> modelMapper.map(e, EnderecoResponseDTO.class))
            .toList();
    }

    @Transactional(readOnly = true)
    public EnderecoResponseDTO obterEnderecoPorId(int clienteId, int enderecoId) {
        ClienteModel cliente = buscarPorId(clienteId);
        EnderecoModel endereco = buscarEndereco(cliente, enderecoId);

        return modelMapper.map(endereco, EnderecoResponseDTO.class);
    }

    @Transactional
    public List<EnderecoResponseDTO> salvarEndereco(int clienteId, EnderecoRequestDTO enderecoRequestDTO) {
        ClienteModel cliente = buscarPorId(clienteId);

        if (cliente.getEnderecos().size() >= 5) {
            throw new UnprocessableContentException("O cliente não pode ter mais de 5 endereços");
        }

        boolean duplicado = cliente.getEnderecos().stream().anyMatch(e ->
                        e.getEndereco().equalsIgnoreCase(enderecoRequestDTO.getEndereco()) &&
                        e.getNumero().equals(enderecoRequestDTO.getNumero()) &&
                        e.getCep().equals(enderecoRequestDTO.getCep()));

        if (duplicado) {
            throw new BusinessRuleException("Endereço já existente");
        }

        if (enderecoRequestDTO.getPadrao()) {
            cliente.getEnderecos().forEach(e -> e.setPadrao(false));
        }

        EnderecoModel endereco = modelMapper.map(enderecoRequestDTO, EnderecoModel.class);
        if (endereco.getPadrao() == null) {
            endereco.setPadrao(false);
        }

        cliente.addEndereco(endereco);

        clienteRepository.save(cliente);
        return cliente.getEnderecos().stream()
                .map(e -> modelMapper.map(e, EnderecoResponseDTO.class))
                .toList();
    }

    @Transactional
    public EnderecoResponseDTO atualizarEndereco(int clienteId, int enderecoId, EnderecoRequestDTO enderecoRequestDTO) {
        ClienteModel cliente = buscarPorId(clienteId);
        EnderecoModel endereco = buscarEndereco(cliente, enderecoId);

        modelMapper.map(enderecoRequestDTO, endereco);

        clienteRepository.save(cliente);
        return modelMapper.map(endereco, EnderecoResponseDTO.class);
    }

    @Transactional
    public void deletarEndereco(int clienteId, int enderecoId) {
        ClienteModel cliente = buscarPorId(clienteId);

        buscarEndereco(cliente, enderecoId);
        cliente.getEnderecos().removeIf(e -> e.getId() == enderecoId);

        clienteRepository.save(cliente);
    }

}