<%
    // 1. Java destruye la sesión del servidor
    session.invalidate();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Desconectando...</title>
</head>
<body style="background: #050505; color: #D4FF00; font-family: monospace; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;">
<h2>[ PURGANDO CONEXIÓN DEL SISTEMA... ]</h2>

<script>
    // 2. JavaScript destruye la memoria del navegador
    localStorage.removeItem('vh_cart_items');
    localStorage.removeItem('vh_fav_items');

    // 3. Transición elegante al catálogo
    setTimeout(() => {
        window.location.href = 'index.jsp';
    }, 1000);
</script>
</body>
</html>