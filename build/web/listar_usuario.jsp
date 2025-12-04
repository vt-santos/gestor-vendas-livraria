<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
    <title>Listar Usuários | Livraria Aurora</title>
    <script type="text/javascript">
        function confirmarDesativar(idUsuario, nome){
            if(confirm('Deseja realmente desativar o usuário '+nome+'?')){
                window.open("gerenciar_usuario.do?acao=desativar&idUsuario="+idUsuario,"_self");
            }
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Usuários do Sistema</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>
        
<section class="content">
    <h2>Listar Usuários</h2>
    <p>Gerencie os acessos dos colaboradores ao sistema.</p>
    
    <div class="text-center mb-4">
        <a href="form_usuario.jsp" class="btn btn-success btn-lg">
            <i class="glyphicon glyphicon-plus"></i> Novo Cadastro
        </a>
    </div>

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered display" id="listarUsuario">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome do Usuário</th>
                    <th>Login</th>
                    <th>Data de Nascimento</th>
                    <th>Perfil</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <th>ID</th>
                    <th>Nome do Usuário</th>
                    <th>Login</th>
                    <th>Data de Nascimento</th>
                    <th>Perfil</th>
                    <th>Status</th>
                    <th>Opções</th>
                </tr>
            </tfoot>
            <tbody>
                <c:forEach var="u" items="${listarTodos}">
                    <tr>
                        <td>${u.idUsuario}</td> 
                        <td>${u.nome}</td>
                        <td>${u.login}</td>
                        <td><fmt:formatDate pattern="dd/MM/yyyy" value="${u.dataNasc}"/></td>
                        <td>${u.perfil.nome}</td>
                        <td>
                            <c:if test="${u.status==1}">Ativo</c:if>
                            <c:if test="${u.status==2}">Inativo</c:if>
                        </td>
                        <td>
                            <div class="btn-group-acao">
                                <a class="btn btn-primary btn-sm"
                                   href="gerenciar_usuario.do?acao=alterar&idUsuario=${u.idUsuario}"
                                   title="Editar Usuário">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </a>
                                <button class="btn btn-danger btn-sm"
                                        onclick="confirmarDesativar(${u.idUsuario},'${u.nome}')"
                                        title="Desativar Usuário">
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
    $(document).ready(function(){
        $("#listarUsuario").DataTable({
            "language":{
                "url": "datatables/portugues.json"     
            },
            "pagingType": "full_numbers",
            "pageLength": 10,
            "lengthMenu": [5, 10, 25, 50],
            "dom": '<"top"lf>rt<"bottom"ip><"clear">'
        });
    });

    function toggleMenu(){
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>    

</body>
</html>
