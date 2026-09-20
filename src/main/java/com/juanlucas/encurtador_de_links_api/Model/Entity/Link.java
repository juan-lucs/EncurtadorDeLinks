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
public class Link {
    @Id
    @Column
    private String id;

    @Column
    private String urlOriginal;

    @Column
    private String codigo;

    @Column
    private LocalDate Datacriacao;

    @Column
    private int cliques;

    public Link(String id, String urlOriginal, String codigo, LocalDate datacriacao) {
        this.id = id;
        this.urlOriginal = urlOriginal;
        this.codigo = codigo;
        this.Datacriacao = datacriacao;
    }

    public Link() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDatacriacao() {
        return Datacriacao;
    }

    public void setDatacriacao(LocalDate datacriacao) {
        Datacriacao = datacriacao;
    }

    public int getCliques() {
        return cliques;
    }

    public void setCliques(int cliques) {
        this.cliques = cliques;
    }

}
