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
    <title>Listar Menus | Livraria Aurora</title>

    <script>
        function confirmarExclusao(idMenu, nome) {
            if (confirm('Deseja realmente excluir o menu ' + nome + '?')) {
                window.open("gerenciar_menu.do?acao=excluir&idMenu=" + idMenu, "_self");
            }
        }
        function toggleMenu() {
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
        <p>Menus de Navegação</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>
        
<section class="content">
    <h2>Listar Menus</h2>
    <p>Gerencie os itens do menu da Livraria Aurora.</p>
    
    <div class="text-center mb-4">
        <a href="form_menu.jsp" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Novo Cadastro
        </a>
    

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarMenu">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome do Menu</th>
                    <th>Link</th>
                    <th>Ícone</th>
                    <th>Exibir</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Nome do Menu</th>
                    <th>Link</th>
                    <th>Ícone</th>
                    <th>Exibir</th>
                    <th>Opções</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="m" items="${listarTodos}">
                    <tr>
                        <td>${m.idMenu}</td> 
                        <td>${m.nome}</td>
                        <td>${m.link}</td>
                        <td>${m.icone}</td>
                        <td>
                            <c:if test="${m.exibir==1}">Sim</c:if>
                            <c:if test="${m.exibir==2}">Não</c:if>
                        </td>
                        <td>
                            <div class="btn-group-acao">
                                <a class="btn btn-primary btn-sm"
                                   href="gerenciar_menu.do?acao=alterar&idMenu=${m.idMenu}"
                                   title="Editar Menu">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="confirmarExclusao(${m.idMenu}, '${m.nome}')"
                                        title="Excluir Menu">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
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
    $(document).ready(function () {
        $("#listarMenu").DataTable({
            "language": {
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
