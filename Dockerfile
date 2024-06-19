# Stage 1: Build the Quarkus application
FROM eclipse-temurin:17-jdk-alpine AS build

# Install Maven
RUN apk add --no-cache curl tar bash \
 && curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz -o /tmp/maven.tar.gz \
 && mkdir -p /opt/maven \
 && tar -xz -f /tmp/maven.tar.gz -C /opt/maven --strip-components=1 \
 && ln -s /opt/maven/bin/mvn /usr/bin/mvn

# Set working directory
WORKDIR /workspace

# Copy the root pom.xml and Maven wrapper files
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/

# Ensure the mvnw script is executable
RUN chmod +x mvnw

# Copy the full source tree
COPY . .

# Download dependencies (leveraging Docker cache)
RUN ./mvnw dependency:go-offline -B

# Build the project, package the JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the Docker image for running the Quarkus application
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /work/

# Copy the quarkus-run.jar and dependencies into the container
COPY --from=build /workspace/rest/target/quarkus-app/quarkus-run.jar app.jar
COPY --from=build /workspace/rest/target/quarkus-app/lib/ lib/
COPY --from=build /workspace/rest/target/quarkus-app/app/ app/
COPY --from=build /workspace/rest/target/quarkus-app/quarkus/ quarkus/
COPY --from=build /workspace/rest/target/quarkus-app/quarkus-app-dependencies.txt quarkus-app-dependencies.txt

# Expose the ports your application uses
EXPOSE 8080
EXPOSE 8443

# Set the entry point to run the application
ENTRYPOINT ["sh", "-c", "echo 'Sleeping for 45 seconds...' && sleep 45 && java -jar app.jar"]