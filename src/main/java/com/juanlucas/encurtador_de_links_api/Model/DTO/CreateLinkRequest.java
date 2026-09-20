package com.juanlucas.encurtador_de_links_api.Model.DTO;

import jakarta.validation.constraints.NotBlank;

public class CreateLinkRequest {

    @NotBlank
    private String url;
    public CreateLinkRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
