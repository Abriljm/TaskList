Instrucciones para generar e instalar la aplicación en un dispositivo Android
1. Clona el repositorio abrindo una terminal y poniendo el siguiente comando:
   git clone [Enlace del repositorio]
2. Navega dentro de la carpeta del proyecto clonado:
   cd Control_de_tareas
3. Instalar Gradle (si no lo tienes ya instalado):
   Si no tienes Gradle instalado en tu sistema, sigue los pasos de instalación desde aquí: https://gradle.org/install/
   Asegúrate de que Gradle esté correctamente configurado en tu PATH.
4. Generar el archivo APK:
   En la terminal, genera el siguiente comando:
   ./gradlew assembleDebug
   Este comando compilará la versión de depuración de la aplicación y generará el APK.
   El APK se encontrará en la carpeta app/build/outputs/apk/debug/ dentro de tu proyecto.
6. Transferir el APK al dispositivo Android:
   Conecta tu dispositivo Android a la computadora mediante un cable USB.
   Copia el archivo APK generado (app/build/outputs/apk/debug/app-debug.apk) a la carpeta de descargas del dispositivo o cualquier ubicación accesible.
7. Habilitar la instalación desde fuentes desconocidas:
   Antes de instalar el APK, asegúrate de permitir la instalación de aplicaciones de fuentes desconocidas en tu dispositivo Android:
   Ve a Configuración -> Seguridad -> Fuentes desconocidas y actívalo.
8. Instalar el APK:
   En tu dispositivo Android, abre un explorador de archivos y navega hasta la ubicación donde copiaste el APK.
   Pulsa sobre el archivo APK para iniciar la instalación.
   Sigue las instrucciones en pantalla para completar la instalación.
9. Abrir la aplicación:
    Una vez instalada, podrás abrir la aplicación directamente desde el menú de aplicaciones de tu dispositivo.

Esta aplicación móvil cumple con los requerimientos establecidos, pero además, me tomé la libertad de agregar validaciones de usuario y contraseña en el login y signup.
USUARIO: Debe ser un correo electrónico válido, es decir, que tenga un nombreDeUsuario@organización.tipo.
CONTRASEÑA: Mayor a 6 caracteres.
Estas son las validaciones que añadí para habilitar los botones correspondientes.
Cabe destacar que se usaron principios básicos de Clean Architecture y se agregó una arquitectura tipo MVVM.

He realizado aplicaciones responsivas anteriormente, utilizando jetpack compose y kotlin, en este caso no me dio tiempo de ordenar todo para que la app acepte el modo oscuro.
La forma de realizarlo es mediante el tema de la app, allí se definen los colores que tendrá la app tanto en el modo claro como en el oscuro y, cuando estas editando un archivo
de jetpack compose para la vista, en la línea donde configuras el color se le asigna el nombre que le asignaste en el theme. De esta manera cuando cambias a modo claro y a modo oscuro
la app detectará la configuración y cambiará los colores automáticamente.
