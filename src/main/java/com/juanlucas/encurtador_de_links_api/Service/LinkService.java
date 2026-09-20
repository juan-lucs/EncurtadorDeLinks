package com.juanlucas.encurtador_de_links_api.Service;

import com.juanlucas.encurtador_de_links_api.Exception.EncurtadorNaoEncontradoException;
import com.juanlucas.encurtador_de_links_api.Exception.UrlVaziaException;
import com.juanlucas.encurtador_de_links_api.Model.DTO.CreateLinkRequest;
import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import com.juanlucas.encurtador_de_links_api.Repository.linkRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
public class LinkService {
    private final linkRepository Repository;

    public LinkService(linkRepository repository) {

        Repository = repository;
    }

    private static String gerarCodigo() {
        var caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        var codigo = new StringBuilder("");
        var random = new Random();
        for (var i = 0; i <5; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));

        }
        return codigo.toString();
    }

    public Link encurtarLink(CreateLinkRequest url) throws UrlVaziaException {
        // ADICIONAR VERIFICADOR DE URL (SE ELA EXISTE E NAO DA 404)
        var link = new Link(UUID.randomUUID().toString(), url.getUrl(), gerarCodigo(), LocalDate.now());
        Repository.save(link);
        return link;
    }

    public Link buscarLink(String codigo) throws EncurtadorNaoEncontradoException{
            var link = Repository.findByCodigo(codigo);
            if (link == null) {
                throw new EncurtadorNaoEncontradoException("Url encurtada não encontrada!");
            }
            link.setCliques(link.getCliques() + 1);
            Repository.save(link);
            return link;
        }

    public Link buscarcliques(String codigo) {
        var link = Repository.findByCodigo(codigo);
        return link;
    }
}
