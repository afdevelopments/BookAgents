# Sistema de Compra y Venta de Libros con Agentes JADE

## Descripción General

Este proyecto implementa un sistema de compra y venta de libros utilizando agentes en la plataforma JADE (Java Agent DEvelopment Framework). El sistema consiste en agentes compradores y vendedores que interactúan entre sí para comprar y vender libros. Cada agente cuenta con una interfaz gráfica (GUI) que facilita la interacción del usuario con el agente.

## Autores

- **Luis Fernando Chávez Jiménez**
- **Guillermo Moreno Rivera**
- **César Joel Ramírez Maciel**

## Componentes Principales

1. **Agentes Compradores (`Comprador.java`)**:
   - Buscan libros para comprar basándose en títulos específicos.
   - Interactúan con agentes vendedores para obtener propuestas y realizar compras.
   - Utilizan la GUI `BookBuyerGui` para recibir entrada del usuario y mostrar información sobre el proceso de compra.

2. **Agentes Vendedores (`Vendedor.java`)**:
   - Poseen un catálogo de libros con sus respectivos precios.
   - Responden a las solicitudes de los compradores proporcionando propuestas y gestionando órdenes de compra.
   - Utilizan la GUI `BookSellerGui` para permitir al usuario actualizar el catálogo de libros.

3. **Comportamientos**:
   - `OfferRequestServer.java`: Gestiona las solicitudes de oferta de los compradores.
   - `PurchaseOrderServer.java`: Gestiona las órdenes de compra de los compradores.
   - `RequestPerformer.java`: Implementa el proceso de solicitud y compra de libros para el agente comprador.

4. **Interfaces Gráficas**:
   - `BookBuyerGui.java`: GUI para el agente comprador. Permite al usuario ingresar el título de un libro que desea comprar y muestra mensajes relacionados con el proceso de compra.
   - `BookSellerGui.java`: GUI para el agente vendedor. Permite al usuario agregar libros al catálogo o actualizar los precios de los libros existentes.

## Funcionamiento Básico

1. El agente vendedor se inicializa con un catálogo vacío y espera a que el usuario agregue libros y establezca precios a través de `BookSellerGui`.

2. El agente comprador, a través de `BookBuyerGui`, espera a que el usuario ingrese el título del libro que desea comprar.

3. Una vez ingresado el título, el agente comprador busca agentes vendedores disponibles y solicita propuestas para el libro deseado.

4. Los agentes vendedores responden con una propuesta si tienen el libro en su catálogo o rechazan la solicitud si no lo tienen.

5. El agente comprador evalúa las propuestas recibidas, selecciona la mejor oferta y realiza la compra.

6. El agente vendedor confirma la venta y actualiza su catálogo.

## Uso del Sistema

Para utilizar el sistema:

1. Inicie los agentes vendedores y configure sus catálogos utilizando `BookSellerGui`.

2. Inicie los agentes compradores y utilice `BookBuyerGui` para ingresar títulos de libros que desea comprar.

3. Observe las interacciones entre los agentes y las transacciones realizadas a través de las consolas y las interfaces gráficas.

## Conclusión

El sistema de compra y venta de libros con agentes JADE demuestra cómo se pueden modelar y simular interacciones de mercado en un entorno de agentes. Los agentes actúan de forma autónoma y toman decisiones basadas en la información disponible y las interacciones con otros agentes, lo que hace que el sistema sea dinámico y adaptable a diferentes situaciones.
