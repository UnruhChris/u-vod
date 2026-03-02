#!/bin/bash
set -e

COSMOS_HOST="172.20.0.10"
COSMOS_PORT="8081"

echo "⏳ Waiting for Cosmos DB Emulator at $COSMOS_HOST:$COSMOS_PORT..."

# Loop di attesa
until curl -k -f "https://$COSMOS_HOST:$COSMOS_PORT/_explorer/emulator.pem" -o /tmp/emulator.cert; do
  echo "zzz... Cosmos DB not ready yet. Retrying in 5s..."
  sleep 5
done

echo "✅ Cosmos DB found! Certificate downloaded."

# TROVA IL PERCORSO CORRETTO DI CACERTS
if [ -f "$JAVA_HOME/lib/security/cacerts" ]; then
    CACERTS_PATH="$JAVA_HOME/lib/security/cacerts"
elif [ -f "$JAVA_HOME/conf/security/cacerts" ]; then
    CACERTS_PATH="$JAVA_HOME/conf/security/cacerts"
else
    CACERTS_PATH=$(find $JAVA_HOME -name cacerts | head -n 1)
fi

echo "🔑 Importing certificate into Java Keystore at: $CACERTS_PATH"

# Importiamo il certificato
keytool -importcert -alias cosmos-emulator \
    -keystore "$CACERTS_PATH" \
    -file /tmp/emulator.cert \
    -storepass changeit \
    -noprompt

echo "🚀 Starting Catalog Service..."

exec java -Dcom.azure.cosmos.disableServerCertificateValidation=true -jar app.jar
