package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    List<CategoriaProduto> findByEmpresaId(Long empresaId);

}
