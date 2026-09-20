package com.juanlucas.encurtador_de_links_api.Service;

import com.juanlucas.encurtador_de_links_api.Exception.EncurtadorNaoEncontradoException;
import com.juanlucas.encurtador_de_links_api.Exception.UrlInvalidaException;
import com.juanlucas.encurtador_de_links_api.Model.DTO.CriarLinkRequest;
import com.juanlucas.encurtador_de_links_api.Model.DTO.SaidaLinkRequest;
import com.juanlucas.encurtador_de_links_api.Model.Entity.EstatisticaLink;
import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import com.juanlucas.encurtador_de_links_api.Repository.estatisticaLinkRepository;
import com.juanlucas.encurtador_de_links_api.Repository.linkRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LinkService {
    private final linkRepository Repositorylink;
    private final estatisticaLinkRepository Repositoryesta;

    public LinkService(linkRepository repository, estatisticaLinkRepository repositoryesta) {
        Repositoryesta = repositoryesta;
        Repositorylink = repository;
    }

    private String gerarCodigo() {
        var caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        var codigo = new StringBuilder("");
        var random = new Random();
        for (var i = 0; i <5; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        List<String> codigos = Repositorylink.findAllCodigos();
        if (codigos.contains(codigo.toString())) {
            return gerarCodigo();
        }
        return codigo.toString();
    }
    private static boolean validaUrl(String url) {
        try {
            var uri = new URI(url); // analisa a estrutura da url
            if ("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()) && uri.getHost() != null) {
                return true;
            } else { return false;}
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public Link encurtarLink(CriarLinkRequest url) throws UrlInvalidaException {
        if (!validaUrl(url.getUrl())) {
            throw new UrlInvalidaException("Url inválida!");
        }
        var link = new Link(UUID.randomUUID().toString(), url.getUrl(), gerarCodigo());
        var estatisticas = new EstatisticaLink(0 , LocalDate.now(), new LinkedList<LocalDateTime>());
        link.setEstatisticaLink(estatisticas);
        return Repositorylink.save(link);
    }

    public Link buscarLink(String codigo) throws EncurtadorNaoEncontradoException{
        var link = Repositorylink.findByCodigo(codigo);
        if (link == null) {
                throw new EncurtadorNaoEncontradoException("Url encurtada não encontrada!");
        }
        EstatisticaLink estatistica = Repositoryesta.findByLinkId(link.getId());
        estatistica.setCliques(estatistica.getCliques() + 1);// Tentar UPDATE ATÔMICO BOOM
        estatistica.getUltimoAcessos().add(LocalDateTime.now());
        link.setEstatisticaLink(estatistica);
        return Repositorylink.save(link);
        }

    public Link buscarcliques(String codigo) {
        var link = Repositorylink.findByCodigo(codigo);
        return link;
    }
}
