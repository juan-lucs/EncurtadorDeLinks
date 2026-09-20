package com.juanlucas.encurtador_de_links_api.Controller;

import com.juanlucas.encurtador_de_links_api.Exception.EncurtadorNaoEncontradoException;
import com.juanlucas.encurtador_de_links_api.Exception.UrlVaziaException;
import com.juanlucas.encurtador_de_links_api.Model.DTO.CreateLinkRequest;
import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import com.juanlucas.encurtador_de_links_api.Repository.linkRepository;
import com.juanlucas.encurtador_de_links_api.Service.LinkService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("link")
public class linkController {
    private final LinkService Service;

    public linkController(LinkService service) {
        Service = service;
    }

    @PostMapping
    public Link encurtarLink(@RequestBody CreateLinkRequest url) throws UrlVaziaException {
        return Service.encurtarLink(url);
    }

    @GetMapping("/{codigo}")
    public Link buscarLInk(@PathVariable String codigo) throws EncurtadorNaoEncontradoException {
        var link = Service.buscarLink(codigo);
        return link;
    }
}
