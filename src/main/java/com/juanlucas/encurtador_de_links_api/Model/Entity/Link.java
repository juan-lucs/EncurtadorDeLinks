package com.juanlucas.encurtador_de_links_api.Model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "Link")
public class Link {
    @Id
    @Column
    private String id;

    @Column
    private String urlOriginal;

    @Column
    private String codigo;
    @OneToOne(cascade = CascadeType.ALL) // quando eu der .save no Repo de link, ele tambem vai salvar estatistica (eu espero)
    @JoinColumn(name = "estatisticaLink_id")
    @NotNull
    private EstatisticaLink estatisticaLink;

    public Link(String id, String urlOriginal, String codigo) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.codigo = codigo;
    }

    public Link() {
    }

}
