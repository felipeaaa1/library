package com.github.felipeaaa1.libraryapi.repository;

import com.github.felipeaaa1.libraryapi.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransacaoTest {

    @Autowired
    public TransacaoService transacaoService;

    @Test
    public void transacaoTest(){
        transacaoService.executar();
    }

    @Test
    public void salvarSemSave(){
        transacaoService.atualizarSemRepositorySave();
    }
}
