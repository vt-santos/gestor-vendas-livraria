/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
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
public class UsuarioDAO extends DataBaseDAO{

    public UsuarioDAO() throws ClassNotFoundException {
    }
    
    public ArrayList<Usuario> getLista(){
        
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    Usuario u =new Usuario();
                    u.setIdUsuario(rs.getInt("idUsuario"));
                    u.setNome(rs.getString("nome"));
                    u.setLogin(rs.getString("login"));
                    u.setSenha(rs.getString("senha"));
                    u.setStatus(rs.getInt("status"));
                    u.setDataNasc(rs.getDate("dataNasc"));
                    
                    try {
                        //setar o Perfil do Usuario
                        PerfilDAO pDAO = new PerfilDAO();
                        u.setPerfil(pDAO.getCarregaPorID(rs.getInt("Perfil_idPerfil")));
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    lista.add(u);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
    
    public boolean gravar(Usuario u){
        
        boolean sucesso = false;
        String sql;
        
        //define a query dependendo do IdMenu
        if(u.getIdUsuario()==0){
            sql = "INSERT INTO usuario (nome, login, senha, dataNasc, status, Perfil_idPerfil) VALUES (?,?,?,?,?,?)";
        }else{
            sql = "UPDATE usuario SET nome=?, login=?, senha=?, dataNasc=?, status=?, Perfil_idPerfil=? WHERE idUsuario=?";
        }
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, u.getNome());
                pstm.setString(2, u.getLogin());
                pstm.setString(3, u.getSenha());
                pstm.setDate(4,new Date(u.getDataNasc().getTime()));
                pstm.setInt(5, u.getStatus());
                pstm.setInt(6,u.getPerfil().getIdPerfil());
                //verifica se o idMenu é maior que zero, se for realiza o UPDaTE
                if(u.getIdUsuario()>0){
                    pstm.setInt(7, u.getIdUsuario());
                }
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
    
    public Usuario getCarregaPorID(int idUsuario){
        
        Usuario u =null;
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        try{
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idUsuario);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        u = new Usuario();
                        u.setIdUsuario(rs.getInt("idUsuario"));
                        u.setNome(rs.getString("nome"));
                        u.setLogin(rs.getString("login"));
                        u.setSenha(rs.getString("senha"));
                        u.setStatus(rs.getInt("status"));
                        u.setDataNasc(rs.getDate("dataNasc"));

                        try {
                            //setar o Perfil do Usuario
                            PerfilDAO pDAO = new PerfilDAO();
                            u.setPerfil(pDAO.getCarregaPorID(rs.getInt("Perfil_idPerfil")));
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }    
        return u;
    }
    
    public boolean desativar(Usuario u){
    
        boolean sucesso = false;
        try{
            this.conectar();
            String SQL = "UPDATE usuario SET status=2 WHERE idUsuario=?";
            try(PreparedStatement pstm = conn.prepareStatement(SQL)){
                pstm.setInt(1, u.getIdUsuario());
                int rowsffected = pstm.executeUpdate();
                if(rowsffected > 0){
                    sucesso = true;
                }
            }
        
        }catch(SQLException e){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    }
    
    public Usuario getRecuperarUsuario(String login){
        
        Usuario u =null;
        String sql = "SELECT u.*, p.nome FROM usuario u "
                + "INNER JOIN perfil p ON "
                + "p.idPerfil = u.Perfil_idPerfil WHERE login = ?";
        try{
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, login);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        u = new Usuario();
                        u.setIdUsuario(rs.getInt("u.idUsuario"));
                        u.setNome(rs.getString("u.nome"));
                        u.setLogin(rs.getString("u.login"));
                        u.setSenha(rs.getString("u.senha"));
                        u.setStatus(rs.getInt("u.status"));
                        u.setDataNasc(rs.getDate("u.dataNasc"));

                        try {
                            //setar o Perfil do Usuario
                            PerfilDAO pDAO = new PerfilDAO();
                            u.setPerfil(pDAO.getCarregaPorID(rs.getInt("u.Perfil_idPerfil")));
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }    
        return u;
    }
    
    
    
}
