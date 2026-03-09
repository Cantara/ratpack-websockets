# ratpack-websockets

## Purpose
Library to mitigate Ratpack websocket issues. Provides fixes and improvements for WebSocket handling in Ratpack-based applications.

## Tech Stack
- Language: Java 8+
- Framework: Ratpack
- Build: Maven
- Key dependencies: Ratpack

## Architecture
Small utility library that patches or improves WebSocket behavior in Ratpack applications. Designed as a drop-in dependency for any Ratpack project that uses WebSockets.

## Key Entry Points
- WebSocket utility classes in `src/main/java/`
- `pom.xml` - Maven coordinates: `no.cantara:ratpack-websockets`

## Development
```bash
# Build
mvn clean install

# Test
mvn test
```

## Domain Context
WebSocket infrastructure for Ratpack-based services. Used by Cantara services built on the Ratpack framework (such as Whydah-CRMService) that need reliable WebSocket support.
