<%@ page pageEncoding="utf-8" %>
<html>
    <head>
        <title>Productos</title>
        <meta charset="utf-8"/>
        <style>
            body {
                grid-template-columns: 50%;
                display: grid;
            }

            form {
                border-style: inset;
                border-color: black;
                border-width: 5px;
                border-radius: 6%;
                background: white;
                margin-left: 3%;
                margin-right: 3%;
                margin-top: 3%;
                margin-bottom: 3%;

            }

            .add input[type="text"],input[type="date"],input[type="number"],h2,div {
                width: 80%;
                height: auto;
                margin: auto;
                margin-bottom: 1%;
                margin-top: 1%;
                display: block;
            }

            .add input[type="submit"] {
                display: block;
                margin: 0 auto;
                margin-bottom: 3%;
                margin-top: 3%;
            }

            .add {
                grid-column: 1/1;
                grid-row: 1/2;
            }

            .mostrar {
                grid-column: 2/2;
                grid-row: 1;
            }
        </style>
    </head>
    <body>
        <form class="add">
            <h2>Añadir producto</h2>
            <input name="action" value="addProducto" hidden/>
            <input name="codProd" type="text" placeholder="Código de producto" autofocus/>
            <input name="seccion" type="text" placeholder="Seccion"/>
            <input name="nombreProd" type="text" placeholder="Nombre de producto"/>
            <input name="precio" pattern="[0-9]{1,}" type="text" placeholder="precio"/>
            <input name="fecha" type="date" placeholder="fecha"/>
            <div>
                <input id="importado-true" name="importado" type="radio" value="true"/>
                <label for=importado-true">Es importado</label>
                <br/>
                <input id="importado-false" name="importado" type="radio" value="false"/>
                <label for=importado-false">No es importado</label>
            </div>
            <input name="pais" type="text" placeholder="pais"/>
            <input name="stock" type="number" placeholder="stock"/>
            <input type="submit" value="Enviar"/>
        </form>
        <form class="mostrar">
            <h2>Mostrar todos los productos</h2>
            <br/>
            <a href='./productos?action=mostrarProductos'> Mostrar todos los productos</a>
        </form>
    </body>
</html>
