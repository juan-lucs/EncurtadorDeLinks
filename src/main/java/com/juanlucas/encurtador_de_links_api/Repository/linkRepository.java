package com.juanlucas.encurtador_de_links_api.Repository;

import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface linkRepository extends JpaRepository<Link, String> {
    public Link findByCodigo(String codigo);
}
