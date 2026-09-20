package com.juanlucas.encurtador_de_links_api.Controller;

import com.juanlucas.encurtador_de_links_api.Exception.EncurtadorNaoEncontradoException;
import com.juanlucas.encurtador_de_links_api.Exception.UrlInvalidaException;
import com.juanlucas.encurtador_de_links_api.Model.DTO.CriarLinkRequest;
import com.juanlucas.encurtador_de_links_api.Model.DTO.SaidaLinkRequest;
import com.juanlucas.encurtador_de_links_api.Service.LinkService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("link")
public class LinkController {
    private final LinkService Service;

    public LinkController(LinkService service) {
        Service = service;
    }

    @PostMapping
    public SaidaLinkRequest encurtarLink(@RequestBody @Valid CriarLinkRequest url) throws UrlInvalidaException {
        return new SaidaLinkRequest(Service.encurtarLink(url));
    }

    @GetMapping("/{codigo}")
    public RedirectView buscarLInk(@PathVariable String codigo) throws EncurtadorNaoEncontradoException {
        var link = Service.buscarLink(codigo);
        return new RedirectView(link.getUrlOriginal());
    }

    @GetMapping("/{codigo}/status")
    public SaidaLinkRequest status(@PathVariable String codigo) throws EncurtadorNaoEncontradoException{
        return new SaidaLinkRequest(Service.buscarcliques(codigo));
    }
}
