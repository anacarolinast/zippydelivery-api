package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
