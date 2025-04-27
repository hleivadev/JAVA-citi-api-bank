# Usa una imagen base de OpenJDK 17 (ajusta según lo que necesites)
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y la carpeta src (esto ayuda a aprovechar la caché de Docker en las compilaciones)
COPY pom.xml .
COPY src ./src

# Descarga las dependencias y compila el proyecto usando Maven
# Nota: Usa la versión del wrapper de Maven (mvnw) que ya tienes en el proyecto
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Expón el puerto que usa tu aplicación (por ejemplo, 8080)
EXPOSE 8080

# Define el comando para ejecutar la aplicación
# Suponiendo que el jar generado se llama "apiCiti-0.0.1-SNAPSHOT.jar" en target; ajústalo si es necesario.
CMD ["java", "-jar", "target/apiCiti-0.0.1-SNAPSHOT.jar"]
