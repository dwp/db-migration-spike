docker run \
    --name personal-details-server \
    --link mongo-server:mongo \
    -d \
    personal-details-server