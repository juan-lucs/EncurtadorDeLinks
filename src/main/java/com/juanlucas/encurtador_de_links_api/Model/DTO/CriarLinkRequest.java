package com.juanlucas.encurtador_de_links_api.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarLinkRequest {

    @NotBlank
    private String url;
    public CriarLinkRequest() {
    }

}
