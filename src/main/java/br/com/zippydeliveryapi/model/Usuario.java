package br.com.zippydeliveryapi.model;

import java.util.ArrayList;
import java.util.List;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario extends EntidadeNegocio {

   private static final long serialVersionUID = -2660334839251150243L;
   public static final String ROLE_CLIENTE = "CLIENTE"; //Realizar compras no sistema
   public static final String ROLE_EMPRESA = "EMPRESA";  //READ, DELETE, WRITE, UPDATE.
   public static final String ROLE_ADMIN = "ADMIN";  //READ, DELETE, WRITE, UPDATE.

   @Column(nullable = false)
   private String username;

   @JsonIgnore
   @Column(nullable = false)
   private String password;

   @JsonIgnore
   @ElementCollection(fetch = FetchType.EAGER)
   @Builder.Default
   private List<String> roles = new ArrayList<>();

}
