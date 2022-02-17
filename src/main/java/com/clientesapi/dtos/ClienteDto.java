package com.clientesapi.dtos;

import com.clientesapi.entities.Endereco;
import com.clientesapi.entities.Telefone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ClienteDto {

    private Long id;

    @NotEmpty(message = "Deve ser informado um nome válido.")
    private String nome;

    @CPF
    @NotEmpty(message = "Deve ser informado um CPF válido.")
    @Length(max = 11, message = "O CPF deve conter 11 digitos.")
    private String cpf;

    private Endereco enderecoPrincipal;

    private List<Endereco> enderecos;

    private List<Telefone> telefones;

}
