package com.ms.order.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProdutoDTO {

    private Long id;
    private Integer quantidade;
    private Double preco;

    public ProdutoDTO() {
    
    }

    
    @JsonCreator
    public ProdutoDTO(@JsonProperty("id") Long id, 
                      @JsonProperty("quantidade") int quantidade, 
                      @JsonProperty("preco") double preco) {
        this.id = id;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
