<%-- 
    Document   : index
    Created on : 12/08/2025, 21:29:37
    Author     : adm_pfelacio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="stylesheet" href="css/estilo.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
        <script type="text/javascript">
            function confirmarDesativar(idVenda, nome){
                
                if(confirm('Deseja realmente cancelar a venda do cliente '+nome+'?')){
                    window.open("gerenciar_venda.do?acao=cancelar&idVenda="+idVenda,"_self");
                }
                
            }
        </script>    
        <title>Listar Vendas</title>
    </head>
    <body>
        <div class="banner">
            <%@include file="banner.jsp" %>
        </div>
        <!--Menu de navegação -->
        <%@include file="menu.jsp" %>
        
        <div class="content">
            <h2>Listar Venda</h2>
            <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-primary">Nova Venda</a>
            <table class="table table-hover table-striped table-bordered display" id="listarVenda">
            
                <thead>
                    <tr>
                        <th>idVenda</th>
                        <th>Vendedor</th>
                        <th>Cliente</th>
                        <th>Data da Venda</th>
                        <th>Status</th>
                        <th>Opções</th>
                    </tr>
                </thead>
                 <tfoot>
                    <tr>
                       <th>idVenda</th>
                        <th>Vendedor</th>
                        <th>Cliente</th>
                        <th>Data da Venda</th>
                        <th>Status</th>
                        <th>Opções</th>
                    </tr>
                </tfoot>
                <tbody>
                    <c:forEach var="v" items="${listarTodos}">
                        <tr>
                            <td>${v.idVenda}</td> 
                            <td>${v.vendedor.nome}</td>
                            <td>${v.cliente.nome}</td>
                            <td><fmt:formatDate pattern="dd/MM/yyyy" value="${v.dataVenda}"/></td>
                            <td><c:if test="${v.status==1}">Ativo</c:if>
                                <c:if test="${v.status==2}">Cancelada</c:if>
                            </td>
                            <td>
                                <a class="btn btn-warning" href="gerenciar_venda.do?acao=visualizar&idVenda=${v.idVenda}">
                                    <i class="glyphicon glyphicon-eye-open"></i>
                                </a>
                                <a class="btn btn-primary" href="gerenciar_venda.do?acao=alterar&idVenda=${v.idVenda}">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger" onclick="confirmarDesativar(${v.idVenda},'${v.cliente.nome}')">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
                            </td>
                        </tr>
                        
                       
                        
                    </c:forEach>
                    
                </tbody>
            
            </table>    
            
            
        </div> 
        <script type="text/javascript" src="datatables/jquery.js"></script>
        <script type="text/javascript" src="datatables/jquery.dataTables.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#listarUsuario").DataTable({
                    "language":{
                    "url": "datatables/portugues.json"     
                    }
                });
            });
        
        <!-- ativar menu responsivo-->
        
            function toggleMenu(){
                var menu = document.getElementById("nav-links");
                menu.classList.toggle("show");
            }
        </script>    
        
    </body>
</html>


