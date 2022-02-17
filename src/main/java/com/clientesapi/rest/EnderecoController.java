package com.clientesapi.rest;

import com.clientesapi.entities.Endereco;
import com.clientesapi.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoRepository repository;

    @Autowired
    public EnderecoController(EnderecoRepository repository) {this.repository = repository;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Endereco salvar(@Valid @RequestBody Endereco endereco) {
        return repository.save(endereco);
    }

    @GetMapping
    public List<Endereco> listar() { return repository.findAll(); }

    @GetMapping("{id}")
    public Endereco acharPorId(@PathVariable Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O endereço" + id + " não foi localizado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        Optional<Endereco> endereco = repository.findById(id);

        if (!endereco.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não localizado.");
        }

        repository.deleteById(endereco.get().getId());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Endereco atualizar(@PathVariable Long id, @Valid @RequestBody Endereco dados) {
        Endereco endereco = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não localizado."));

        endereco.setRua(dados.getRua());
        endereco.setBairro(dados.getBairro());
        endereco.setCep(dados.getCep());
        endereco.setCidade(dados.getCidade());
        endereco.setUf(dados.getUf());

        return repository.save(endereco);
    }

}
