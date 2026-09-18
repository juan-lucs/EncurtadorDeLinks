package com.juanlucas.encurtador_de_links_api.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "Link")
@Getter
@Setter
public class Link {
    @Id
    @Column
    private String id;

    @Column
    private String urlOriginal;

    @Column
    private Long codigo;

    @Column
    private LocalDate Datacriacao;

    @Column
    private int cliques;
}
