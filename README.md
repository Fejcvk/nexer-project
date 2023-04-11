# Recruitment task

This project is created with help Serverless framework. The architecture is fairly simple, there is API Gateway with three endpoints
* One for adding new fruits to the database
* One for listing all the fruits by the price range
* One for registering email address to SNS Topic

All of these endpoints have their individual lambda to handle the request. Records are stored in DynamoDB

## Prerequisites
* Java 11 installed
* Maven installed
* Serverless installed

## How to run testing script
```bash 
chmod+x run_task.sh
```

```bash 
./run_task.sh
```

And later follow the prompts in the script