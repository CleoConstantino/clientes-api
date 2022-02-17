package com.clientesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "telefones")
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telefone_sequence")
    @SequenceGenerator(name = "telefone_sequence", sequenceName = "tel_seq")
    private Long id;

    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @JsonIgnoreProperties("telefones")
    private Cliente cliente;

}
