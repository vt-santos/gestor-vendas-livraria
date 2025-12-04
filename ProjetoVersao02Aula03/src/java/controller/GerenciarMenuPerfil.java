/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.MenuPerfil;
import modelo.MenuPerfilDAO;
import modelo.Perfil;
import modelo.PerfilDAO;

/**
 *
 * @author adm_pfelacio
 */
public class GerenciarMenuPerfil extends HttpServlet {

   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String mensagem = "";
        
        String idPerfilStr = request.getParameter("idPerfil");
        String acao = request.getParameter("acao");
        Perfil p = new Perfil();
        try{
            PerfilDAO pDAO = new PerfilDAO();
            MenuPerfilDAO mpDAO = new MenuPerfilDAO();
            MenuPerfil mp = new MenuPerfil();
            int idPerfil=0;
            if(acao.equals("gerenciar")){
                try{
                   idPerfil = Integer.parseInt(idPerfilStr);
                   p = pDAO.getCarregaPorID(idPerfil);
                   
                   if(p.getIdPerfil()>0){
                       RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_menu_perfil.jsp");
                       request.setAttribute("perfil", p);
                       disp.forward(request, response);
                   }else{
                       exibirMensagem("Perfil Não Encontrado", false, "", response);
                   }
                }catch(NumberFormatException e){
                    exibirMensagem("idPerfil invalido", false, "", response);
                }
            }
            if(acao.equals("desvincular")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    String idMenuStr = request.getParameter("idMenu");

                    try{
                        if(idMenuStr.equals("")||idMenuStr.isEmpty())
                            exibirMensagem("O menu deve ser selecionado", false, "", response); 
                        else{
                            if(mpDAO.desvincular(Integer.parseInt(idMenuStr), Integer.parseInt(idPerfilStr)))
                                exibirMensagem("Desvinculado com sucesso", true,"gerenciar_menu_perfil.do?acao=gerenciar&idPerfil="+idPerfilStr, response);
                            else
                                 exibirMensagem("Erro ao desvincular", false, "", response); 
                        }

                    }catch(NumberFormatException e){
                          exibirMensagem("idPerfil ou idMenu inválido", false, "", response); 
                    }
                 }else{
                       exibirMensagem("Acesso Negado", false, "",response);
                }
            }
        }catch(Exception e){
            out.println(e);
            exibirMensagem("Erro ao acessar o banco", false, "", response);
        }    
        
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        String idMenuStr = request.getParameter("idMenu");
        String idPerfilStr = request.getParameter("idPerfil");
        
        try{
            MenuPerfilDAO mpDAO = new MenuPerfilDAO();
            if(idMenuStr.equals("")||idMenuStr.isEmpty()||idPerfilStr.equals("")||idPerfilStr.isEmpty())
                exibirMensagem("Campos obrigatórios deverão ser preenchidos", false,"", response);
            else{
                if(mpDAO.vincular(Integer.parseInt(idMenuStr), Integer.parseInt(idPerfilStr)))
                    exibirMensagem("Vinculado com sucesso", true,"gerenciar_menu_perfil.do?acao=gerenciar&idPerfil="+idPerfilStr, response);
                else
                    exibirMensagem("Erro ao vincular", false,"", response);
            }
                
        }catch(Exception e){
            out.print(e);
            e.printStackTrace();
        }
        
        
    }
    
     private static void exibirMensagem(String mensagem, boolean resposta, String link, HttpServletResponse response){
            
        try{
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('"+mensagem+"')");
            if(resposta){
                out.println("location.href='"+link+"'");
            }else{
                out.println("history.back();");
            }
            out.println("</script>");
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
