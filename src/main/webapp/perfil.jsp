<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 1. ESCUDO DE SEGURIDAD ABSOLUTO:
    // Si alguien intenta entrar a perfil.jsp sin iniciar sesión, lo pateamos al login.
    if (session.getAttribute("usuario_valido") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 2. Extraemos la identidad de la memoria del servidor
    String nombre = (String) session.getAttribute("usuario_nombre");
    int rol = (Integer) session.getAttribute("usuario_rol");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Centro de Mando // VECTORHEART</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Space+Mono:wght@400;700&display=swap" rel="stylesheet">
</head>
<body style="background: #050505; color: #FFF; padding: 50px; font-family: var(--font-tech); background-image: radial-gradient(circle at 50% 0%, #1a1a1a 0%, #050505 70%); min-height: 100vh;">

<div style="max-width: 1000px; margin: 0 auto;">

    <header style="border-bottom: 2px solid var(--primary); padding-bottom: 20px; margin-bottom: 30px; display: flex; justify-content: space-between; align-items: flex-end;">
        <div>
            <span style="color: var(--primary); letter-spacing: 2px; font-size: 0.8rem;">[ SISTEMA DE IDENTIFICACIÓN ]</span>
            <h1 style="font-family: var(--font-heading); font-size: 3.5rem; margin: 0; line-height: 1;">BIENVENIDO, <span style="color: var(--primary);"><%= nombre %></span></h1>
        </div>
        <a href="index.jsp" style="color: #AAA; text-decoration: none; font-size: 0.9rem; transition: 0.3s;"><< VOLVER AL CATÁLOGO</a>
    </header>

    <div style="border: 1px solid #333; padding: 40px; background: rgba(10,10,10,0.8); border-radius: 4px;">

        <% if (rol == 1) { %>
        <h2 style="color: #FF003C; font-family: var(--font-heading); font-size: 2rem; margin-top:0;">[ PANEL DE ADMINISTRADOR SUPREMO ]</h2>
        <p style="color: #888;">Nivel de autorización Máximo. Acceso a métricas de ventas y control de la base de datos de usuarios.</p>
        <div style="padding: 20px; border: 1px dashed #FF003C; margin-top: 20px; text-align: center; color: #555;">
            (Aquí construiremos las gráficas Chart.js en la Fase 4)
        </div>

        <% } else if (rol == 2) { %>
        <h2 style="color: var(--primary); font-family: var(--font-heading); font-size: 2rem; margin-top:0;">[ PANEL DE GESTIÓN DE INVENTARIO ]</h2>
        <p style="color: #888;">Autorización Logística. Acceso para crear, editar, aplicar descuentos o eliminar módulos del catálogo.</p>
        <div style="padding: 20px; border: 1px dashed var(--primary); margin-top: 20px; text-align: center; color: #555;">
            (Aquí construiremos la tabla CRUD de productos en la Fase 2)
        </div>

        <% } else { %>
        <h2 style="color: #00F0FF; font-family: var(--font-heading); font-size: 2rem; margin-top:0;">[ TERMINAL DE CLIENTE ]</h2>
        <p style="color: #888;">Autorización Estándar. Acceso a historial de compras y archivos clasificados (Favoritos).</p>
        <div style="padding: 20px; border: 1px dashed #00F0FF; margin-top: 20px; text-align: center; color: #555;">
            (Aquí conectaremos PostgreSQL para guardar tu carrito permanentemente en la Fase 3)
        </div>
        <% } %>

    </div>
</div>

</body>
</html>