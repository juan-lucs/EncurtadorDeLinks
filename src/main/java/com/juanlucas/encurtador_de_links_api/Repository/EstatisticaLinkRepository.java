package com.juanlucas.encurtador_de_links_api.Repository;

import com.juanlucas.encurtador_de_links_api.Model.Entity.EstatisticaLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstatisticaLinkRepository extends JpaRepository<EstatisticaLink, String> {
    EstatisticaLink findByLinkId(String linkId);
}
