<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
    <title>Listar Clientes | Livraria Aurora</title>

    <script>
        function confirmarDesativar(idCliente, nome){
            if(confirm("Deseja realmente desativar o cliente " + nome + "?")){
                window.open("gerenciar_cliente.do?acao=desativar&idCliente=" + idCliente, "_self");
            }
        }
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Clientes Cadastrados</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Listar Clientes</h2>
    <p>Gerencie os clientes da livraria.</p>
    
    <div class="text-center mb-4">
        <a href="form_cliente.jsp" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Novo Cadastro
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarCliente">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Telefone</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Telefone</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="c" items="${listarTodos}">
                    <tr>
                        <td>${c.idCliente}</td>
                        <td>${c.nome}</td>
                        <td>${c.telefone}</td>
                        <td>
                            <c:if test="${c.status==1}">Ativo</c:if>
                            <c:if test="${c.status==2}">Inativo</c:if>
                        </td>
                        <td>
                            <div class="btn-group-acao">
                                <a href="gerenciar_cliente.do?acao=alterar&idCliente=${c.idCliente}"
                                   class="btn btn-primary btn-sm" title="Editar Cliente">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="confirmarDesativar(${c.idCliente}, '${c.nome}')"
                                        title="Desativar Cliente">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
                                <c:if test="${c.status == 1}">
                                <a href="gerenciar_venda.do?acao=novaVenda&idCliente=${c.idCliente}"
                                   class="btn btn-success btn-sm" title="Nova Venda">
                                    Nova Venda
                                </a>
                                </c:if>
                                <c:if test="${c.status == 2}">
                                <button class="btn btn-secondary btn-sm" disabled title="Cliente Inativo">
                                    Inativo
                                </button>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<script src="datatables/jquery.js"></script>
<script src="datatables/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function(){
        $("#listarCliente").DataTable({
            "language":{
                "url": "datatables/portugues.json"
            },
            "pagingType": "full_numbers",
            "pageLength": 10,
            "lengthMenu": [5, 10, 25, 50],
            "dom": '<"top"lf>rt<"bottom"ip><"clear">'
        });
    });
</script>

</body>
</html>
