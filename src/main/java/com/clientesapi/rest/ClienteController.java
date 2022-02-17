package com.clientesapi.rest;

import com.clientesapi.dtos.ClienteDto;
import com.clientesapi.entities.Cliente;
import com.clientesapi.entities.Endereco;
import com.clientesapi.entities.Telefone;
import com.clientesapi.repository.ClienteRepository;
import com.clientesapi.repository.EnderecoRepository;
import com.clientesapi.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository repository;
    private final TelefoneRepository telefoneRepository;
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public ClienteController(ClienteRepository repository,
                             TelefoneRepository telefoneRepository,
                             EnderecoRepository enderecoRepository) {
        this.repository = repository;
        this.telefoneRepository = telefoneRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping
    public List<Cliente> listar() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Cliente acharPorId(@PathVariable Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O cliente " + id + " não foi localizado."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(@Valid @RequestBody ClienteDto clienteDto) {
        if (repository.existsByCpf(clienteDto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este CPF já está cadastrado.");
        }

        Cliente cliente = new Cliente();
        cliente.setCpf(clienteDto.getCpf());
        cliente.setNome(clienteDto.getNome());

        List<Telefone> telefoneList = new ArrayList<>();
        clienteDto.getTelefones().forEach(data -> {
            Telefone telefone = new Telefone();
            telefone.setNumero(data.getNumero());
            telefone.setCliente(cliente);

            telefoneList.add(telefone);
        });

        cliente.setTelefones(telefoneList);

        List<Endereco> enderecoList = new ArrayList<>();
        clienteDto.getEnderecos().forEach(data -> {
            Endereco endereco = new Endereco();
            endereco.setRua(data.getRua());
            endereco.setCep(data.getCep());
            endereco.setBairro(data.getBairro());
            endereco.setCidade(data.getCidade());
            endereco.setUf(data.getUf());
            endereco.setCliente(cliente);

            enderecoList.add(endereco);
        });

        cliente.setEnderecos(enderecoList);

        if (clienteDto.getEnderecoPrincipal() == null) {
            Optional<Endereco> enderecoPrincipal = cliente.getEnderecos().stream().findFirst();
            enderecoPrincipal.ifPresent(cliente::setEnderecoPrincipal);
        } else {
            cliente.setEnderecoPrincipal(clienteDto.getEnderecoPrincipal());
        }

        return repository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto clienteDto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não localizado."));

        if (repository.existsByIdNotAndCpf(id, clienteDto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este CPF já está cadastrado.");
        }

        cliente.setNome(clienteDto.getNome());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setEnderecoPrincipal(clienteDto.getEnderecoPrincipal());

        List<Telefone> telefoneList = new ArrayList<>();
        clienteDto.getTelefones().forEach(data -> {
            Telefone telefone = new Telefone();
            if (Objects.nonNull(data.getId())) {
                telefone = telefoneRepository.findById(data.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Telefone '" + data.getNumero() + "' não localizado."));
            }

            telefone.setNumero(data.getNumero());
            telefone.setCliente(cliente);

            telefoneList.add(telefone);
        });

        cliente.setTelefones(telefoneList);

        List<Endereco> enderecoList = new ArrayList<>();
        clienteDto.getEnderecos().forEach(data -> {
            Endereco endereco = new Endereco();
            if (Objects.nonNull(data.getId())) {
                endereco = enderecoRepository.findById(data.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço '" + data.getRua() + "' não localizado."));
            }

            endereco.setRua(data.getRua());
            endereco.setBairro(data.getBairro());
            endereco.setCidade(data.getCidade());
            endereco.setUf(data.getUf());
            endereco.setCep(data.getCep());
            endereco.setCliente(cliente);

            enderecoList.add(endereco);
        });

        cliente.setEnderecos(enderecoList);

        return repository.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        Optional<Cliente> cliente = repository.findById(id);

        if (!cliente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não localizado.");
        }

        repository.delete(cliente.get());
    }

}
