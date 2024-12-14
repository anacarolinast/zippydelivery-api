package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.empresa.id = :idEmpresa")
    List<Pedido> findByEmpresaId(@Param("idEmpresa") Long idEmpresa);

    @Query(value = "SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
    List<Pedido> findByClienteId(@Param("idCliente") Long idCliente);

}
