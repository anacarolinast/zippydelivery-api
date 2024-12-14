package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaEmpresaRepository extends JpaRepository<CategoriaEmpresa, Long> {

    List<CategoriaEmpresa> findByHabilitadoTrue();

}
