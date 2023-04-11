#!/bin/bash

echo 'Installing dependencies'
mvn clean install

echo 'Compiling the code'
mvn clean package

echo 'Deploying assets to the cloud with Serverless framework'
sls deploy

echo 'Please input the Api Gateway identifier'

read apiGatewayId

echo 'Please pass the Operations Team email address. Make sure it is the same as in serverless.yaml!'

read emailAddress

echo -e 'Subscribing with Operations Team email to the Fruit Notification Topic\n'

curl -X GET https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/sns/$emailAddress

echo -e 'Creating new fruits\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Apple", "description": "Test description", "price": 0.99}'

echo -e '\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Orange", "description": "Test description", "price": 1.99}'

echo -e '\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Melon", "description": "Test description", "price": 2.99}'

echo -e '\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Watermelon", "description": "Test description", "price": 3.99}'

echo -e '\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Blueberry", "description": "Test description", "price": 4.99}'

echo -e '\n'

curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits/create -d '{"name": "Banana", "description": "Test description", "price": 9.99}'

echo -e '\nListing fruits in price range 0 to 3\n'
curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits -d '{"minPrice": 0, "maxPrice": 3 }'

echo -e '\nListing fruits in price range 2 to 5\n'
curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits -d '{"minPrice": 2, "maxPrice": 5 }'

echo -e '\nListing fruits in price range 0 to 10\n'
curl -X POST https://$apiGatewayId.execute-api.us-east-1.amazonaws.com/dev/fruits -d '{"minPrice": 0, "maxPrice": 10 }'

echo -e '\nCleaning up all the infrastructure from AWS account\n'
sls remove