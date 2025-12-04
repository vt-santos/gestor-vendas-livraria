<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Livraria Aurora | Home</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp"%>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Conhecimento que ilumina</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp"%>
</nav>

<section class="content">
    <h2>Bem-vindo à Livraria Aurora</h2>
    <p>Explore nosso catálogo, descubra novos autores e aproveite nossas promoções semanais.</p>

    <div class="cards">
        <div class="card">
            <img src="imagens/livro1.jpg" alt="Novidades da Livraria Aurora"/>
            <h3>Novidades</h3>
            <p>Lançamentos fresquinhos para você.</p>
        </div>

        <div class="card">
            <img src="imagens/livro2.jpg" alt="Promoções da Livraria Aurora"/>
            <h3>Promoções</h3>
            <p>Descontos imperdíveis em títulos selecionados.</p>
        </div>

        <div class="card">
            <img src="imagens/livro3.jpg" alt="Categorias de livros"/>
            <h3>Categorias</h3>
            <p>Ficção, romance, educação, fantasia e muito mais.</p>
        </div>
    </div>
</section>

<script>
    function toggleMenu() {
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>

</body>
</html>
