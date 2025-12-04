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
public class Perfil {
    
    private int idPerfil;
    private String nome;
    private int status;
    private MenuPerfil menus;

    public Perfil() {
    }

    public Perfil(int idPerfil, String nome, int status, MenuPerfil menus) {
        this.idPerfil = idPerfil;
        this.nome = nome;
        this.status = status;
        this.menus = menus;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MenuPerfil getMenus() {
        return menus;
    }

    public void setMenus(MenuPerfil menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Perfil{" + "idPerfil=" + idPerfil + ", nome=" + nome + ", status=" + status + ", menus=" + menus + '}';
    }

   
    
    
    
}
