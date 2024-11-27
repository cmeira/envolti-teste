package com.ms.order.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ms.order.dtos.ProdutoDTO;
import com.ms.order.service.ProdutoService;

class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProdutos() {
        // Mockando o retorno do serviço
        ProdutoDTO produto1 = new ProdutoDTO(1L, 10, 100.0);
        ProdutoDTO produto2 = new ProdutoDTO(2L, 5, 50.0);
        List<ProdutoDTO> produtosMock = Arrays.asList(produto1, produto2);

        when(produtoService.listarProdutos()).thenReturn(produtosMock);

        // Chamando o método do controlador
        ResponseEntity<List<ProdutoDTO>> response = produtoController.listarProdutos();

        // Asserts
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    @Test
    void testObterProdutoPorId() {
        // Mockando o retorno do serviço
        ProdutoDTO produtoMock = new ProdutoDTO(1L, 10, 100.0);
        when(produtoService.obterProdutoPorId(1L)).thenReturn(produtoMock);

        // Chamando o método do controlador
        ResponseEntity<ProdutoDTO> response = produtoController.obterProdutoPorId(1L);

        // Asserts
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals(10, response.getBody().getQuantidade());
    }

    @Test
    void testCriarProduto() {
        // Criando um produto DTO
        ProdutoDTO produtoDTO = new ProdutoDTO(null, 5, 50.0);

        // Chamando o método do controlador
        ResponseEntity<?> response = produtoController.criarProduto(produtoDTO);

        // Verificando se o serviço foi chamado corretamente
        verify(produtoService, times(1)).criarProduto(produtoDTO);

        // Asserts
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Produto criado com sucesso!", response.getBody());
    }

    @Test
    void testAtualizarProduto() {
        // Criando um produto DTO
        ProdutoDTO produtoDTO = new ProdutoDTO(null, 15, 150.0);

        // Chamando o método do controlador
        ResponseEntity<?> response = produtoController.atualizarProduto(1L, produtoDTO);

        // Verificando se o serviço foi chamado corretamente
        verify(produtoService, times(1)).atualizarProduto(1L, produtoDTO);

        // Asserts
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Produto atualizado com sucesso!", response.getBody());
    }

    @Test
    void testDeletarProduto() {
        // Chamando o método do controlador
        ResponseEntity<?> response = produtoController.deletarProduto(1L);

        // Verificando se o serviço foi chamado corretamente
        verify(produtoService, times(1)).deletarProduto(1L);

        // Asserts
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Produto deletado com sucesso!", response.getBody());
    }
}
