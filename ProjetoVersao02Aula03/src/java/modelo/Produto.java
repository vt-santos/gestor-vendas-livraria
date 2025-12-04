/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author adm_pfelacio
 */
public class Produto {
    
    private int idProduto;
    private String nome;
    private int qtd;
    private double valor;
    private int status;

    public Produto() {
    }

    public Produto(int idProduto, String nome, int qtd, double valor, int status) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.qtd = qtd;
        this.valor = valor;
        this.status = status;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Produto{" + "idProduto=" + idProduto + ", nome=" + nome + ", qtd=" + qtd + ", valor=" + valor + ", status=" + status + '}';
    }
    
    
    
    
}
