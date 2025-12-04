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
public class Cliente {
    
    private int idCliente;
    private String nome;
    private String telefone;
    private int status;

    public Cliente() {
    }

    public Cliente(int idCliente, String nome, String telefone, int status) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.telefone = telefone;
        this.status = status;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + ", nome=" + nome + ", telefone=" + telefone + ", status=" + status + '}';
    }
    
    
    
    
    
}
