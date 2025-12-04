/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.MovimentacaoEstoque;
import modelo.MovimentacaoEstoqueDAO;
import modelo.Produto;
import modelo.ProdutoDAO;
import modelo.Usuario;

/**
 *
 * @author Sistema de Estoque
 */
public class GerenciarEstoque extends HttpServlet {

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
        
        HttpSession sessao = request.getSession();
        Usuario ulogado = (Usuario) sessao.getAttribute("ulogado");
        
        // Verifica se o usuário está logado
        if (ulogado == null) {
            response.sendRedirect("form_login.jsp");
            return;
        }
        
        String acao = request.getParameter("acao");
        
        try {
            MovimentacaoEstoqueDAO mDAO = new MovimentacaoEstoqueDAO();
            ProdutoDAO pDAO = new ProdutoDAO();
            
            if (acao == null) {
                acao = "listarMovimentacoes";
            }
            
            // Ação: Listar todas as movimentações
            if (acao.equals("listarMovimentacoes")) {
                ArrayList<MovimentacaoEstoque> listarTodos = mDAO.getLista();
                RequestDispatcher disp = getServletContext().getRequestDispatcher("/listar_movimentacao_estoque.jsp");
                request.setAttribute("listarTodos", listarTodos);
                disp.forward(request, response);
            }
            
            // Ação: Listar movimentações de um produto específico
            if (acao.equals("listarPorProduto")) {
                try {
                    int idProduto = Integer.parseInt(request.getParameter("idProduto"));
                    ArrayList<MovimentacaoEstoque> listarPorProduto = mDAO.getListaPorProduto(idProduto);
                    Produto produto = pDAO.getCarregaPorID(idProduto);
                    
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/listar_movimentacao_estoque.jsp");
                    request.setAttribute("listarTodos", listarPorProduto);
                    request.setAttribute("produtoFiltro", produto);
                    disp.forward(request, response);
                    
                } catch (NumberFormatException e) {
                    exibirMensagem("ID de produto inválido", false, "", response);
                }
            }
            
            // Ação: Exibir formulário de movimentação
            if (acao.equals("novaMovimentacao")) {
                ArrayList<Produto> produtos = pDAO.getLista();
                RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_movimentacao_estoque.jsp");
                request.setAttribute("produtos", produtos);
                disp.forward(request, response);
            }
            
            // Ação: Exibir relatório de estoque
            if (acao.equals("relatorio")) {
                ArrayList<Produto> produtos = pDAO.getLista();
                ArrayList<Produto> produtosEstoqueBaixo = mDAO.getProdutosEstoqueBaixo(10);
                
                // Calcular estatísticas
                int totalProdutos = produtos.size();
                int produtosAtivos = 0;
                int produtosSemEstoque = 0;
                double valorTotalEstoque = 0;
                
                for (Produto p : produtos) {
                    if (p.getStatus() == 1) {
                        produtosAtivos++;
                        valorTotalEstoque += (p.getQtd() * p.getValor());
                    }
                    if (p.getQtd() == 0) {
                        produtosSemEstoque++;
                    }
                }
                
                request.setAttribute("produtos", produtos);
                request.setAttribute("produtosEstoqueBaixo", produtosEstoqueBaixo);
                request.setAttribute("totalProdutos", totalProdutos);
                request.setAttribute("produtosAtivos", produtosAtivos);
                request.setAttribute("produtosSemEstoque", produtosSemEstoque);
                request.setAttribute("valorTotalEstoque", valorTotalEstoque);
                
                RequestDispatcher disp = getServletContext().getRequestDispatcher("/relatorio_estoque.jsp");
                disp.forward(request, response);
            }
            
            // Ação: Verificar estoque de um produto (retorna JSON)
            if (acao.equals("verificarEstoque")) {
                try {
                    int idProduto = Integer.parseInt(request.getParameter("idProduto"));
                    int estoqueAtual = mDAO.getEstoqueAtual(idProduto);
                    
                    response.setContentType("application/json");
                    out.print("{\"estoque\": " + estoqueAtual + "}");
                    
                } catch (NumberFormatException e) {
                    response.setContentType("application/json");
                    out.print("{\"erro\": \"ID inválido\"}");
                }
            }
            
        } catch (Exception e) {
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
        response.setContentType("text/html");
        
        HttpSession sessao = request.getSession();
        Usuario ulogado = (Usuario) sessao.getAttribute("ulogado");
        
        // Verifica se o usuário está logado
        if (ulogado == null) {
            response.sendRedirect("form_login.jsp");
            return;
        }
        
        String acao = request.getParameter("acao");
        
        try {
            MovimentacaoEstoqueDAO mDAO = new MovimentacaoEstoqueDAO();
            ProdutoDAO pDAO = new ProdutoDAO();
            
            // Ação: Registrar movimentação (entrada, saída ou ajuste)
            if (acao.equals("registrarMovimentacao")) {
                try {
                    int idProduto = Integer.parseInt(request.getParameter("idProduto"));
                    String tipoMovimentacao = request.getParameter("tipoMovimentacao");
                    int quantidade = Integer.parseInt(request.getParameter("quantidade"));
                    String motivo = request.getParameter("motivo");

                    Produto p = pDAO.getCarregaPorID(idProduto);
                    
                    if (p == null) {
                        exibirMensagem("Produto não encontrado", false, "", response);
                        return;
                    }

                    MovimentacaoEstoque m = new MovimentacaoEstoque();
                    m.setProduto(p);
                    m.setUsuario(ulogado);
                    m.setTipoMovimentacao(tipoMovimentacao);
                    m.setQuantidade(quantidade);
                    m.setMotivo(motivo);

                    if (mDAO.registrarMovimentacao(m)) {
                        exibirMensagem("Movimentação registrada com sucesso!", true, 
                                     "gerenciar_estoque.do?acao=listarMovimentacoes", response);
                    } else {
                        exibirMensagem("Erro ao registrar movimentação. Verifique se há estoque suficiente.", 
                                     false, "", response);
                    }

                } catch (NumberFormatException e) {
                    exibirMensagem("Dados inválidos", false, "", response);
                }
            }
            
        } catch (Exception e) {
            out.println(e);
            exibirMensagem("Erro ao processar requisição", false, "", response);
        }
    }

    /**
     * Exibe mensagem JavaScript e redireciona
     */
    private static void exibirMensagem(String mensagem, boolean resposta, String link, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('" + mensagem + "')");
            if (resposta) {
                out.println("location.href='" + link + "'");
            } else {
                out.println("history.back();");
            }
            out.println("</script>");
            out.close();
        } catch (Exception e) {
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
        return "Servlet para gerenciamento de estoque";
    }// </editor-fold>

}
