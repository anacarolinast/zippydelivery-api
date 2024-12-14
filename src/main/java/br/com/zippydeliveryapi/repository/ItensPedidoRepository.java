package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.ItensPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Long> {

    @Query("SELECT i FROM ItensPedido i WHERE i.pedido.id = :idPedido")
    List<ItensPedido> findByidPedido(@Param("idPedido") Long idPedido);

}
