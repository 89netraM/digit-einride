# API
The class `DonkeyCar` provides `fetchFrame()` and `send(angle, throttle)` for interacting with the car through the web API. You need to instantiate the class and then initialize it with `init()` before calling the other methods.

# OpenCV
The project is also configured with OpenCV which can be used to process the images captured by the camera. The OpenCV library consists of C++ bindings so you can make use of documentation and help for both languages. Javadoc for a slightly older version which has method descriptions can be found [here](https://docs.opencv.org/java/2.4.9/).

# Setup
The project is configured with maven and has two dependencies which is JUnit and OpenCV. You can initialize and run the project using maven commands or directly through an IDE such as IntelliJ Idea.

## Initialize:
```
mvn init
```

## Run:
```
mvn clean compile exec:java
```

