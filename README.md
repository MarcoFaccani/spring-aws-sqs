## Introduction
This project is designed to demonstrate the creation of a Spring Boot microservice integrated with **AWS Simple Queue Service (SQS)** and **AWS Simple Email Service (SES)**. <br>
The application functions both as a **producer** and a **consumer**.The consumption of a new message triggers the dispatch of an email.<br>
The project includes integration tests, utilizing **LocalStack** and **TestContainers**. <br>
> Please note, this project does not contain unit tests, in order to speed up development. <br>
> However, you can review my approach and style regarding unit testing in other publicly accessible repositories, such as the following: [spring-aws-s3 project](https://github.com/MarcoFaccani/spring-aws-s3)


### Stack
- Gradle
- Java 17
- Spring Boot 3
- Spring Web
- [Spring Cloud AWS 3](https://docs.awspring.io/spring-cloud-aws/docs/3.0.0/reference/html/index.html#sqs-integration)
- TestContainers

### Features
* Verify the existence of a queue by a given name.
* Create a queue with specified attributes.
* Transmit a message to the queue.
* Receive and process messages from the queue.
* Incorporate an inbound interceptor.
* Perform integration tests utilizing LocalStack and TestContainers.
* Dispatch a basic email.

#### Automatic Queue creation
The `SqsQueueService` automatically creates the queue, for your convenience, if it does not already exist. Please refer to the `application.yml` file to find the name of the queue and the AWS region where it is created. <br>
If you are using the AWS Dashboard, remember to switch to the correct region; otherwise, the created queue may not be visible. <br>

#### Automatic Queue deletion
At application shutdown, the queue is automatically deleted (for your convenience) by the `SqsQueueService`.
> Please be aware that AWS doesn't permit the deletion of a queue and the immediate creation of a new one with the same name within a 60-second timeframe.
> You may face this limitation if you stop and reboot within a minute the app. If this limitation poses an issue, you can resolve it by commenting out the `@PreDestroy` annotation in the `SqsQueueService` class.<br><br>
> If you are using IntelliJ, please **do not double-click on "stop"** button as it may brutally force close the app and throw an error during queue deletion.

### Run the App
> For the email feature, you need to own two valid emails (you will receive an email to verify them). Otherwise comment out the `sendEmail` method in the `AppSqsListener`.
1. To run the app you have to set your AWS access keys in the `application.yml` file. <br>
2. Go to `application.yml` and insert a valid email in `app.aws.ses.sender`.
2. Simply run the app and call the following API `POST http:localhost:8080/sqs/send`. You can find a sample request body
   in `/resources/json/sample-message.json` (make sure to insert a valid email that you own as recipient).


### Error Handling
By default, if a listener encounters an error while processing a message, the message will not be acknowledged. Consequently, once the visibility timeout period elapses, the message becomes available for polling once again.<br>
If the ON_SUCCESS acknowledgement mode is selected and the message should not be acknowledged, it's critical to propagate the error accordingly. If you intend to execute specific actions upon encountering errors, it would be more suitable to use an interceptor. The interceptor can check for the existence of a throwable argument, which would signify a failed execution.
#### Interceptor

The interceptor in this app is used to flatten and clean the error message in case an exception is thrown while consuming a message from the Queue, avoiding to print the whole stack trace in the logs.

### TODO:
1. Handle idempotency (wanna-have)
2. Add IT tests for Simple Email Service