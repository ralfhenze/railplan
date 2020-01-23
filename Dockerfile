#
# With this multi-stage build Dockerfile, the final Docker image will only contain
# the compiled JAR files and no source-code.
#
# See: https://docs.docker.com/develop/develop-images/multistage-build/
#

#-------------------
#  1. Build Stage
#-------------------
FROM maven:3-jdk-11 AS build

COPY 01-domain /build/01-domain
COPY 02-application /build/02-application
COPY 03-infrastructure /build/03-infrastructure
COPY 04-userinterface /build/04-userinterface
COPY pom.xml /build

RUN mvn -f /build/pom.xml clean package

#-------------------
#  2. Run Stage
#-------------------
FROM openjdk:11-jre

COPY --from=build /build/target/lib /app/lib
COPY --from=build /build/target/modules /app/modules

WORKDIR /app/modules
CMD java -cp ./*:../lib/* com.ralfhenze.railplan.userinterface.web.RailPlanApplication

EXPOSE 8080
