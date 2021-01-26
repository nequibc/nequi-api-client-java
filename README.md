# nequi-api-client-java

### Ejemplo para el consumo del API de Nequi en Java  ###

El propósito de este ejemplo es ilustrativo para aquellos interesados en la integración con el API de Nequi. 
Con este ejemplo podrá consumir algunos de los recursos ofrecidos por el API, y si lo desea podrá utilizar este código 
como base para el consumo del resto de recursos expuestos en el API. Para más información visite nuestro 
portal para desarrolladores [https://conecta.nequi.com.co](https://conecta.nequi.com.co "Nequi Conecta").

## 1. Preparación del ambiente

### Credenciales de acceso

Asegúrese de tener las credenciales necesarias para hacer el consumo del API, los datos mínimos que debe tener son:
- Client Id
- Client Secret
- API Key

Los anteriores datos los podrá encontrar en la sección **Credenciales** en el siguiente enlace [https://conecta.nequi.com/content/consultas](https://conecta.nequi.com/content/consultas "Nequi Conecta").

## 2. Ejemplos de integración

Recuerde que podrá encontrar el detalle de los recursos ofrecidos por el API en el siguiente enlace [https://docs.conecta.nequi.com.co/](https://docs.conecta.nequi.com.co/).

En todos los ejemplos ofrecidos en este repositorio encontrará que el consumo de los servicios se hace mediante ```ApiClientFactory``` de Amazon,
usando el siguiente código para instanciar dicho cliente:
```
// Clase de AWS que permite crear el cliente del API para consumir los servicios
ApiClientFactory factory = new ApiClientFactory();


// Establecer el API key necesario para consumir los servicios,
// el cual puede encontrar en su cuenta de Nequi Conecta.
factory.apiKey(System.getenv(Constants.ENV_VAR_NEQUI_API_KEY));

// Creación del cliente del API con el cual podrá llamar los servicios ofrecidos
NequiPaymentsGatewayClient client = factory.build(NequiPaymentsGatewayClient.class);
```

Para el código anterior, el **API key** está como una variable de entorno, pero usted puede usar cualquier mecanismo que
considere apropiado para almacenarla. Lo recomendable es que no se use como un valor fijo en el código que dificulte el cambio
en caso de ser necesario.

### Autenticación en Nequi Conecta

En el archivo ```/com/nequi/controllers/AuthController.java``` podrá encontrar el código necesario para autenticarse, 
el cual le permite obtener un token de acceso que deberá usar en las integraciones del API.

Tenga presente que la autenticación se puede hacer mediante 2 medios:
1.  **Variables de entorno**: *Esta es la opción recomendada, siempre que pueda suministrar las variables de entorno tal cual se mencionan a continuación*.
   El ejemplo de este repositorio usa este medio para obtener el token. Si desea usar este mecanismo
debe suministrar las siguientes variables de entorno:
    - ```NEQUI_CLIENT_ID```: Proveida por Conecta Nequi(ver sección *"Credenciales de acceso"*).
    - ```NEQUI_CLIENT_SECRET```: Proveida por Conecta Nequi(ver sección *"Credenciales de acceso"*).
    - ```NEQUI_AUTH_URI```: Es la URI del endpoint que debe consumir para autenticarse, para pruebas es ```https://oauth.sandbox.nequi.com/oauth2/token```.
    - ```NEQUI_AUTH_GRANT_TYPE```: Tipo de acceso a los recursos del API, el valor recomendado es ```client_credentials```.
    
    Una vez listas las variables de entorno, basta con instanciar la clase ```NequiAuth``` de la siguiente forma ```NequiAuth.getInstance().fromEnvVars();```.
    ####
2. Asignación de los datos necesarios manualmente: Con este mecanismo podrá asignar manualmente los valores que se necesitan para autenticarse
   usando alguno de los siguientes métodos de la clase ```NequiAuth```:
    - ```NequiAuth.getInstance().with```: Este método recibe los siguientes valores:
        - ```String clientId```: Proveida por Conecta Nequi(ver sección *"Credenciales de acceso"*).
        - ```String clientSecret```: Proveida por Conecta Nequi(ver sección *"Credenciales de acceso"*).
        - ```String authUri```: Es la URI del endpoint que debe consumir para autenticarse, para pruebas es ```https://oauth.sandbox.nequi.com/oauth2/token```.
        - ```String authGranType```: : Tipo de acceso a los recursos del API, el valor recomendado es ```client_credentials```.
          *(Este argumento es opcional, si no se envía tomará el valor recomendado)*.
        ####
        Ejemplo 1: 
        ```
        // Se omite el argumento "authGranType" para que tome el valor por defecto
        NequiAuth.getInstance().with("myClientId", "myClienteSecret", "https://oauth.sandbox.nequi.com/oauth2/token");        
        ```
        Ejemplo 2:
        ```
        NequiAuth.getInstance().with("myClientId", "myClienteSecret", "https://oauth.sandbox.nequi.com/oauth2/token", "client_credentials");
        ```        
        ####
    - **Usando los métodos ```.withXYZ```**: La clase ```NequiAuth``` cuenta con 4 métodos concatenables que permiten asignar los valores manualmente:
        - ```.withClientId(String clientId)```
        - ```.withClientSecret(String clientSecret)```
        - ```.withAuthUri(String authUri)```
        - ```.withAuthGranType(String authGranType)```
        ####
        Ejemplo 1:
        ```
        NequiAuth.getInstance()
            .withClientId("myClientId")
            .withClientSecret("myClienteSecret")
            .withAuthUri("https://oauth.sandbox.nequi.com/oauth2/token")
            .withAuthGranType("client_credentials");
        ```

      Ejemplo 2:
        ```
        // Se instancia usando las variables de entorno, pero se puede sobre-escribir cualquier valor
        NequiAuth.getInstance().fromEnvVars()
            .withClientId("myClientId")
            .withClientSecret("myClienteSecret");
        ```
    *Nota: Es recomendable que tengas los datos necesarios para autenticarse en variables de entorno, archivos de configuración, o
    cualquier otro mecanismo que te permita hacer el cambio fácilmente, sin tener que cambiar todas las clases donde lo uses.*
   
    ####
    Una vez instanciada la clase ```NequiAuth```, puede obtener un token de autorización usando el método ```.getToken();```, 
    el cual puede recibir un flag que indica si se desea obtener concatenado el tipo de token y el token(el resultado es la cabecera HTTP "Authorization" ya lista); 
    o si solo se desea el token. Si no se pasa el flag, por defecto será ```true```.
    
### Pagos con QR code

- **Generar pago**: En la clase ```/com/nequi/controllers/PaymentQRController.java``` y el método ```generateQrCode``` podrá encontrar el código para generar un pago con código QR.

### Pagos con notificación

- **Solicitu de pago**: En la clase ```/com/nequi/controllers/PaymentPushController.java``` y el método ```unregisteredPayment``` podrá encontrar el código para solicitar un pago mediante notificación push.

## 3. Ejecutar los ejemplos

Estos ejemplos están basados en una aplicación Spring Boot, así que para verlos en funcionamiento descargue los fuentes, despliéguelos como cualquier aplicación Spring Boot, y luego acceda a [http://localhost:8080](http://127.0.0.1:8080/) desde un navegador web.

## 4. Información adicional

- Paquete ```/com/nequi/utils/```: Aquí podrá encontrar clases que se reusan en los ejemplos de integración, y constantes para validar resultados del API.

----------
*Made with ♥ at Nequi*