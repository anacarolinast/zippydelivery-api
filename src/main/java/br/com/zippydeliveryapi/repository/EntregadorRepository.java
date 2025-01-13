package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Entregador;
import br.com.zippydeliveryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EntregadorRepository extends JpaRepository<Entregador, Long> {

    Entregador findByUsuario(Optional<Usuario> usuario);

    Entregador findByIdAndHabilitadoTrue(Long id);
}