/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adm_pfelacio
 */
public class MenuPerfilDAO extends DataBaseDAO{

    public MenuPerfilDAO() throws ClassNotFoundException {
    }
    
     public boolean vincular(int idMenu, int idPerfil){
        
        boolean sucesso = false;
        String sql;
        
        sql = "INSERT INTO menu_perfil (Menu_idMenu, Perfil_idPerfil) VALUES (?,?)";
        
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idMenu);
                pstm.setInt(2, idPerfil);
                
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(MenuPerfilDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
     
     public boolean desvincular(int idMenu, int idPerfil){
        
        boolean sucesso = false;
        String sql;
        
        sql = "DELETE FROM menu_perfil WHERE Menu_idMenu=? AND Perfil_idPerfil=?";
        
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idMenu);
                pstm.setInt(2, idPerfil);
                
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(MenuPerfilDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
     
     
      public ArrayList<Menu> menusVinculadosPorPerfil(int idPerfil){
        
        ArrayList<Menu> lista = new ArrayList<>();
        String sql = "SELECT m.* FROM menu_perfil as mp, menu as m "
                + " WHERE mp.Menu_idMenu = m.idMenu AND "
                + " mp.Perfil_idPerfil=? ";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1,idPerfil);
                try(ResultSet rs = pstm.executeQuery()){
                
                    while(rs.next()){
                        Menu m =new Menu();
                        m.setIdMenu(rs.getInt("m.idMenu"));
                        m.setNome(rs.getString("m.nome"));
                        m.setLink(rs.getString("m.link"));
                        m.setIcone(rs.getString("m.icone"));
                        m.setExibir(rs.getInt("m.exibir"));
                        lista.add(m);
                    }
            }
         }        
        }catch(SQLException e){
            Logger.getLogger(MenuPerfilDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
      
     public ArrayList<Menu> menusNaoVinculadosPorPerfil(int idPerfil){
        
        ArrayList<Menu> lista = new ArrayList<>();
        String sql = "SELECT * FROM menu m WHERE idMenu "
                + " NOT IN (SELECT Menu_idMenu FROM menu_perfil "
                + "         WHERE Perfil_idPerfil=? )";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1,idPerfil);
                try(ResultSet rs = pstm.executeQuery()){
                
                    while(rs.next()){
                        Menu m =new Menu();
                        m.setIdMenu(rs.getInt("m.idMenu"));
                        m.setNome(rs.getString("m.nome"));
                        m.setLink(rs.getString("m.link"));
                        m.setIcone(rs.getString("m.icone"));
                        m.setExibir(rs.getInt("m.exibir"));
                        lista.add(m);
                    }
            }
         }        
        }catch(SQLException e){
            Logger.getLogger(MenuPerfilDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    } 
     
    public MenuPerfil getCarregaMenusPorPerfil(int idPerfil){
        MenuPerfil mp = new MenuPerfil();
        mp.setMenusVinculados(menusVinculadosPorPerfil(idPerfil));
        mp.setMenusNaoVinculados(menusNaoVinculadosPorPerfil(idPerfil));
        return mp;
    } 
}
