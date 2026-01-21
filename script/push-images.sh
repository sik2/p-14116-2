#!/bin/bash

REGISTRY="sik2dev/msa-k8s"
SERVICES=("member-service" "post-service" "payout-service" "cash-service" "market-service")

#echo "🔨 Building images..."

echo ""
echo "🏷️  Pushing images to $REGISTRY..."

for SERVICE in "${SERVICES[@]}"; do
    echo ""
    echo "📦 Processing $SERVICE..."
    docker compose build $SERVICE

    # Push image
    docker push "$REGISTRY:$SERVICE"

    if [ $? -eq 0 ]; then
        echo "✅ $SERVICE pushed successfully"
    else
        echo "❌ Failed to push $SERVICE"
        exit 1
    fi
done

echo ""
echo "🎉 All images pushed successfully!"
echo ""
echo "Images:"
for SERVICE in "${SERVICES[@]}"; do
    echo "  - $REGISTRY:$SERVICE"
done
