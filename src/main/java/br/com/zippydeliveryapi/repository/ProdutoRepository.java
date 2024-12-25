package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT c AS categoria, p FROM CategoriaProduto c LEFT JOIN Produto p ON c.id = p.categoria.id")
    List<Object[]> agruparPorCategoria();

    @Query("SELECT c AS categoria, p FROM CategoriaProduto c LEFT JOIN Produto p ON c.id = p.categoria.id WHERE c.empresa.id = :empresaId")
    List<Object[]> findByEmpresaGroupByCategoria(@Param("empresaId") Long empresaId);

    List<Produto> findByCategoriaIdAndHabilitadoTrue(Long idCategoria);

    List<Produto> findByEmpresaIdAndHabilitadoTrue(Long idEmpresa);

}