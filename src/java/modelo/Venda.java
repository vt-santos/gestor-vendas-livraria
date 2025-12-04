/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author adm_pfelacio
 */
public class Venda {
    
    private int idVenda;
    private Date dataVenda;
    private Usuario vendedor;
    private Cliente cliente;
    private ArrayList<VendaProduto> carrinho;
    private int status;

    public Venda() {
    }

    public Venda(int idVenda, Date dataVenda, Usuario vendedor, Cliente cliente, ArrayList<VendaProduto> carrinho, int status) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.carrinho = carrinho;
        this.status = status;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<VendaProduto> getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(ArrayList<VendaProduto> carrinho) {
        this.carrinho = carrinho;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Venda{" + "idVenda=" + idVenda + ", dataVenda=" + dataVenda + ", vendedor=" + vendedor + ", cliente=" + cliente + ", carrinho=" + carrinho + ", status=" + status + '}';
    }

    
    
    
}
