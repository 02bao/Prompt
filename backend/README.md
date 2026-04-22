Spring Boot backend (login-demo).

Run locally:
- Requires JDK 17 and Maven.
- Start a local Postgres and set environment variables or edit `application.properties`.
- Build and run:
  mvn -f backend clean package
  java -jar backend/target/login-demo-0.0.1-SNAPSHOT.jar

Deploy to Render:
- Create a Web Service on Render from this repo, Docker or Maven build.
- Set environment variables: DATABASE_URL, DB_USER, DB_PASS, PORT.

Database:
- I used PostgreSQL in config. You can run Postgres in Render (managed DB) or use Supabase.
  The user originally requested "sal" DB — I assume you meant Supabase / Postgres. If you meant a different DB, tell me and I'll adapt.
