package com.juanlucas.encurtador_de_links_api.Model.DTO;

import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class SaidaLinkRequest {
    private String url;
    private String codigo;
    private LocalDate dataCriacao;
    private int cliques;
    private List<LocalDateTime> ultimosAcessos;

    public SaidaLinkRequest(Link link) {
        this.url = link.getUrlOriginal();
        this.codigo = link.getCodigo();
        this.dataCriacao = link.getEstatisticaLink().getDataCriacao();
        this.cliques = link.getEstatisticaLink().getCliques();
        this.ultimosAcessos = link.getEstatisticaLink().getUltimoAcessos();
    }

    @Override
    public String toString() {
        return "SaidaLinkRequest{" +
                "url='" + url + '\'' +
                ", codigo='" + codigo + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", cliques=" + cliques +
                ", ultimosAcessos=" + ultimosAcessos +
                '}';
    }
}

