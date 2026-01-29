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
# In JDK 17+ spesso è in 'conf/security/cacerts', non in 'lib'
if [ -f "$JAVA_HOME/lib/security/cacerts" ]; then
    CACERTS_PATH="$JAVA_HOME/lib/security/cacerts"
elif [ -f "$JAVA_HOME/conf/security/cacerts" ]; then
    CACERTS_PATH="$JAVA_HOME/conf/security/cacerts"
else
    # Fallback brutale: cerca il file ovunque sotto JAVA_HOME
    CACERTS_PATH=$(find $JAVA_HOME -name cacerts | head -n 1)
fi

echo "🔑 Importing certificate into Java Keystore at: $CACERTS_PATH"

# Importiamo il certificato
# Nota: 'changeit' è la password di default di Java
keytool -importcert -alias cosmos-emulator \
    -keystore "$CACERTS_PATH" \
    -file /tmp/emulator.cert \
    -storepass changeit \
    -noprompt

echo "🚀 Starting User Service..."

# AGGIUNTA FONDAMENTALE: -Dcom.azure.cosmos.disableServerCertificateValidation=true
# Questo flag dice all'SDK Azure di ignorare se il nome dell'host (cosmos-db) 
# non corrisponde al nome nel certificato (localhost).
# È essenziale in ambiente Docker Dev.
exec java -Dcom.azure.cosmos.disableServerCertificateValidation=true -jar app.jar