package com.clientesapi.rest;

import com.clientesapi.entities.Cliente;
import com.clientesapi.entities.Telefone;
import com.clientesapi.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/telefones")
public class TelefoneController {

    private final TelefoneRepository repository;

    @Autowired
    public TelefoneController(TelefoneRepository repository) { this.repository = repository; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Telefone salvar(@Valid @RequestBody Telefone telefone) {

        return repository.save(telefone);
    }

    @GetMapping
    public List<Telefone> listar() { return  repository.findAll(); }

    @GetMapping("{id}")
    public Telefone acharPorid(@PathVariable Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O telefone não está cadastrado."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        Optional<Telefone> telefone = repository.findById(id);

        if (!telefone.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Telefone não localizado.");
        }

        repository.deleteById(telefone.get().getId());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Telefone atualizar(@PathVariable Long id, @Valid @RequestBody Telefone dados) {
        Telefone telefone = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Telefone não localizado."));

        telefone.setNumero(dados.getNumero());

        return repository.save(telefone);
    }

}