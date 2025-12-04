<%@page import="modelo.Usuario"%>
<%@page import="controller.GerenciarLogin"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Usuario ulogado = GerenciarLogin.verificarAcesso(request, response);
    request.setAttribute("ulogado", ulogado);
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <!-- LINKS DO MENU (ESQUERDA) -->
    <ul class="navbar-nav" id="nav-links">
        <c:if test="${ulogado != null && ulogado.perfil != null}">
            <c:forEach var="menu" items="${ulogado.perfil.menus.menusVinculados}">
                <c:if test="${menu.exibir == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${menu.link}">${menu.nome}</a>
                    </li>
                </c:if>
            </c:forEach>
        </c:if>
    </ul>

    <!-- ÁREA DO USUÁRIO (DIREITA) -->
    <div class="user-area ml-auto">
        <span>
            Bem-vindo,
            <c:if test="${ulogado != null}">${ulogado.nome}</c:if>
        </span>
        <a href="gerenciar_login.do" class="btn btn-outline-danger btn-sm ml-2">Sair</a>
    </div>
</nav>
