package com.juanlucas.encurtador_de_links_api.Repository;

import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, String> {
    public Link findByCodigo(String codigo);

    @Query("SELECT c.codigo FROM Link c")
    List<String> findAllCodigos();
}
