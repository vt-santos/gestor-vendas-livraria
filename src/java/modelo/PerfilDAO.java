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
public class PerfilDAO extends DataBaseDAO{

    public PerfilDAO() throws ClassNotFoundException {
    }
    
    public ArrayList<Perfil> getLista(){
        
        ArrayList<Perfil> lista = new ArrayList<>();
        String sql = "SELECT * FROM perfil";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    Perfil p =new Perfil();
                    p.setIdPerfil(rs.getInt("idPerfil"));
                    p.setNome(rs.getString("nome"));
                    p.setStatus(rs.getInt("status"));
                    lista.add(p);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
    
    public boolean gravar(Perfil p){
        
        boolean sucesso = false;
        String sql;
        
        //define a query dependendo do IdPerfil
        if(p.getIdPerfil()==0){
            sql = "INSERT INTO perfil (nome, status) VALUES (?,?)";
        }else{
            sql = "UPDATE perfil SET nome=?, status=? WHERE idPerfil=?";
        }
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, p.getNome());
                pstm.setInt(2,p.getStatus());
                
                //verifica se o idPerfil é maior que zero, se for realiza o UPDaTE
                if(p.getIdPerfil()>0){
                    pstm.setInt(3, p.getIdPerfil());
                }
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
    
    public Perfil getCarregaPorID(int idPerfil){
        
        Perfil p =null;
        String sql = "SELECT * FROM perfil WHERE idPerfil = ?";
        try{
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idPerfil);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        p = new Perfil();
                        p.setIdPerfil(rs.getInt("idPerfil"));
                        p.setNome(rs.getString("nome"));
                        p.setStatus(rs.getInt("status"));
                        MenuPerfilDAO mpDAO = new MenuPerfilDAO();
                        MenuPerfil mp;
                        mp = mpDAO.getCarregaMenusPorPerfil(idPerfil);
                        p.setMenus(mp);
                        
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MenuPerfilDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }    
        return p;
    }
    
    public boolean desativar(Perfil p){
    
        boolean sucesso = false;
        try{
            this.conectar();
            String SQL = "UPDATE perfil SET status=2 WHERE idPerfil=?";
            try(PreparedStatement pstm = conn.prepareStatement(SQL)){
                pstm.setInt(1, p.getIdPerfil());
                int rowsffected = pstm.executeUpdate();
                if(rowsffected > 0){
                    sucesso = true;
                }
            }
        
        }catch(SQLException e){
            Logger.getLogger(PerfilDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    }
    
}
