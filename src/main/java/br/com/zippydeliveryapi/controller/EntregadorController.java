package br.com.zippydeliveryapi.controller;

import java.util.List;

import br.com.zippydeliveryapi.model.Entregador;
import br.com.zippydeliveryapi.service.EntregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entregador")
@CrossOrigin
public class EntregadorController {
    
    @Autowired
    private final EntregadorService entregadorService;

    public EntregadorController(EntregadorService entregadorService) {
        this.entregadorService = entregadorService;
    }

    @GetMapping
    public List<Entregador> findAll() {
        return this.entregadorService.findAll();
    }

}
