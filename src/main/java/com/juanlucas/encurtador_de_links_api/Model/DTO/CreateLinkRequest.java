package com.juanlucas.encurtador_de_links_api.Model.DTO;


public class CreateLinkRequest {
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
