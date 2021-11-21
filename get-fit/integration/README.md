# Integration

This module contains integration tests for the project.
An integration test is a test that checks whether two independent pieces of code are able to cooperate.
For this project that means testing wether the client and server are able to comunicate.
Or in other words, do they adhere to the same [API schema](get-fit/schema.md).
This is important because otherwise a developer making a change on an API endpoint on the server might inadvertently break the entire application, without knowledge of it.

Due to how Springboot works this module also includes a package and class that doesn't do anything. And is therefore named dummy.
The existance of the dummy class also necessitates a dummy test.
However, the point of the module is to test the integration of the server and the client.