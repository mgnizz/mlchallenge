ubicarse en la carpeta root del proyecto y ejecutar:

*docker build -t mlchallenge .*

(va a tardar un poco mientras se configura maven)


luego ejecutar

*docker run --rm -it -p 8080:8080 mlchallenge*

hecho eso ya esta ejecutando el war entrar al link:

*http://localhost:8080/ipinfo*
