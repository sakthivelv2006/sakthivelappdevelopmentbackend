# Use lightweight Java image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper & config first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make wrapper executable
RUN chmod +x mvnw

# Download dependencies (cache layer)
RUN ./mvnw dependency:go-offline

# Copy source
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# Render provides dynamic PORT
ENV PORT=8080

# Expose container port
EXPOSE 8080

# Run app using dynamic port
CMD ["sh", "-c", "java -jar target/*.jar --server.port=$PORT"]
