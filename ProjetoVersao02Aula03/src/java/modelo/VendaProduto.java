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
public class VendaProduto {
    
    private long idVendaProduto;
    private int qtdVendida;
    private double valorVendido;
    private Venda venda;
    private Produto produto;

    public VendaProduto() {
    }

    public VendaProduto(long idVendaProduto, int qtdVendida, double valorVendido, Venda venda, Produto produto) {
        this.idVendaProduto = idVendaProduto;
        this.qtdVendida = qtdVendida;
        this.valorVendido = valorVendido;
        this.venda = venda;
        this.produto = produto;
    }

    public long getIdVendaProduto() {
        return idVendaProduto;
    }

    public void setIdVendaProduto(long idVendaProduto) {
        this.idVendaProduto = idVendaProduto;
    }

    public int getQtdVendida() {
        return qtdVendida;
    }

    public void setQtdVendida(int qtdVendida) {
        this.qtdVendida = qtdVendida;
    }

    public double getValorVendido() {
        return valorVendido;
    }

    public void setValorVendido(double valorVendido) {
        this.valorVendido = valorVendido;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "VendaProduto{" + "idVendaProduto=" + idVendaProduto + ", qtdVendida=" + qtdVendida + ", valorVendido=" + valorVendido + ", venda=" + venda + ", produto=" + produto + '}';
    }
    
    
    
    
}
