package com.juanlucas.encurtador_de_links_api.Repository;

import com.juanlucas.encurtador_de_links_api.Model.Entity.EstatisticaLink;
import com.juanlucas.encurtador_de_links_api.Model.Entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface estatisticaLinkRepository extends JpaRepository<EstatisticaLink, String> {
    EstatisticaLink findByLinkId(String linkId);
}
