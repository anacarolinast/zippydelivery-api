package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Empresa;
import br.com.zippydeliveryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByUsuario(Usuario usuario);

    Empresa findByIdAndHabilitadoTrue(Long id);
}
