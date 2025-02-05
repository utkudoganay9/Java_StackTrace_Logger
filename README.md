🚀 Exception & Request Logging Middleware
A lightweight, flexible, and production-ready solution for capturing both request and exception logs in your Spring Boot applications, storing them in PostgreSQL. This project uses Lombok for cleaner code, Hibernate/JPA for seamless database integration, JWT for user identification, and a Filter-based middleware approach.

🔍 Key Features
Automatic Request Logging: Captures essential HTTP request data (URL, IP, userId from JWT, etc.) in a RequestLog table.
Powerful Exception Handling: Intercepts any CustomException or unhandled exceptions, logs them in an ExceptionLog table, and returns a structured JSON response.
JWT Integration: Extracts userId from the Authorization: Bearer <token> header.
Configurable Server Types: Easily adjust via the ServerType enum (e.g., SQL, PAYLOAD, GRAPH, VIDEO).
Minimal Code Intrusion: Managed by a single ExceptionLoggingFilter.
📂 Project Structure
Java_Middleware_StackTrace
├─ src
│  └─ main
│     ├─ java
│     │  └─ Java_Middleware_StackTrace
│     │     ├─ Main.java
│     │     ├─ entity
│     │     │  ├─ ExceptionLog.java
│     │     │  └─ RequestLog.java
│     │     ├─ helper
│     │     │  ├─ ServerType.java
│     │     │  ├─ CustomException.java
│     │     │  └─ LogHelper.java
│     │     ├─ middleware
│     │     │  └─ ExceptionLoggingFilter.java
│     │     ├─ repository
│     │     │  ├─ ExceptionLogRepository.java
│     │     │  └─ RequestLogRepository.java
│     │     └─ response
│     │        ├─ TransactionResult.java
│     │        └─ TransactionStatus.java
│     └─ resources
│        └─ application.properties
└─ pom.xml
Entities define database tables for RequestLog and ExceptionLog.
Helper contains utilities (CustomException, LogHelper, ServerType).
Middleware manages request & exception logging via ExceptionLoggingFilter.
Repository provides Spring Data JPA interfaces.
Response includes standardized JSON structures (TransactionResult, TransactionStatus).

🏁 Getting Started
✅ Prerequisites
Java 17 (or later)
PostgreSQL (installed locally or accessible remotely)
An IDE (IntelliJ/Eclipse/VSCode) with Lombok plugin enabled
Maven 3.x (see below if mvn is not recognized)
⚙️ Installation & Setup
Clone the Repository

bash
git clone https://github.com/yourcompany/yourproject.git
cd yourproject
Configure PostgreSQL
In src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/logdb
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword
spring.jpa.hibernate.ddl-auto=update
Build & Package

If mvn is recognized, just do:
bash
mvn clean package
If you see "mvn is not recognized" on Windows (or "mvn: command not found" on Linux/Mac):
Use Maven Wrapper
In many Spring Boot projects, you can run:
bash
# Windows:
.\mvnw.cmd clean package

# Linux/Mac:
./mvnw clean package
This downloads and uses Maven locally without requiring a global installation.
Install Maven
Download Maven from Apache Maven.
Unzip it to a folder, e.g., C:\Maven\apache-maven-3.x.y\.
Add the bin folder path (e.g., C:\Maven\apache-maven-3.x.y\bin) to your system PATH.
Open a new terminal and check:
bash
Kopyala
Düzenle
mvn -v
If it shows Maven version info, you’re good to go. Then try mvn clean package again.
Run the Application (Optional Check)

bash
java -jar target/yourproject-1.0.0.jar
The application typically starts at http://localhost:8080.

💡 How It Works
Incoming Request
ExceptionLoggingFilter creates a new RequestLog entry (IP, URL, userId if JWT is valid).
Processing
The filter passes control onward via chain.doFilter().
Response
On normal completion, it updates the ResponseCode in RequestLog.
Exception Handling
If an exception is thrown, the filter catches it.
A new ExceptionLog record is saved with stack trace info.
A TransactionResult JSON is returned with an HTTP status 200.
🔧 Customizing the Server Type
In ExceptionLoggingFilter.java, you can set:

java
private final ServerType serverType = ServerType.SQL;
Change it to match your desired value, e.g., PAYLOAD, GRAPH, VIDEO, etc., or load from properties (@Value("${app.serverType}")).

🚀 Usage & Testing
JWT Header: Include Authorization: Bearer <token>. LogHelper attempts to parse and log the userId.
Trigger Exceptions: Throw a CustomException or let your business logic raise any exception to see how logs are created.
Check Logs: Inspect request and exception tables in PostgreSQL.
🏗 Using the Packaged JAR in Other Projects
Local Installation:
bash
mvn clean install
This copies the .jar file to your local Maven repo (~/.m2/repository/).
Dependency Declaration (in another pom.xml):
xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>yourproject</artifactId>
    <version>1.0.0</version>
</dependency>
Remote Repository: If you have a Nexus/Artifactory, configure <distributionManagement> or credentials in your settings.xml and run:
bash
mvn deploy
🤝 Contributing
Fork this repository
Create a feature branch
Commit your changes
Open a Pull Request
📝 License
This project is available under the MIT License. Feel free to modify and use it in your own applications.

<br/>
Happy Logging! For feedback or questions, open an issue or submit a pull request.