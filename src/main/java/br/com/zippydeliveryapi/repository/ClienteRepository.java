package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUsuario(Optional<Usuario> usuario);

    Cliente findByIdAndHabilitadoTrue(Long id);

}