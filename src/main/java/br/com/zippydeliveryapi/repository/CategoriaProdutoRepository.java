package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    List<CategoriaProduto> findByEmpresaIdAndHabilitadoTrue(Long empresaId);

}
