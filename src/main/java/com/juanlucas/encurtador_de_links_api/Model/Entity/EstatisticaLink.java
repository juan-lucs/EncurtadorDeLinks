package com.juanlucas.encurtador_de_links_api.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "estatistica_link")
public class EstatisticaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @OneToOne(mappedBy = "estatisticaLink")
    private Link link;

    @Column
    private int cliques;

    @Column
    private LocalDate dataCriacao;

    @ElementCollection // O Hibernate vai criar uma tabela separada com esses valores
    @Column
    private List<LocalDateTime> ultimoAcessos;

    public EstatisticaLink(int cliques, LocalDate dataCriacao, LinkedList<LocalDateTime> ultimoAcessos) {
        this.cliques = cliques;
        this.dataCriacao = dataCriacao;
        this.ultimoAcessos = ultimoAcessos;
    }
    public EstatisticaLink() {
    }


}
