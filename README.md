# digIT Einride - Hackathon

Write a program to make your car self-driving.

## Getting started

Choose a language by checking out a branch.

## C#

This is a normal .NET Core C# project. To run the program, simply open your
terminal and enter `dotnet run`. It can also be run from Visual Studio by
opening the `donkeycar.csproj` file.

The project contains a `DonkeyCarClient` class that is setup to communicate with
the DonkeyCar. Create an instance by providing host name (or IP address) and
port, then use the `Send` method to send commands to the car, and the
`FetchFrame` method to collect the latest frame from the cars camera.

Take a look at the `Main` function to see the client and it's methods in action.

### Linux or MacOS?

Watch out for common pitfalls when using Emgu.CV on Linux of MaxOS. Look thought
the [Download And Installation](https://www.emgu.com/wiki/index.php/Download_And_Installation)
wiki page for instructions and remember to update the nuget dependency in
`donkeycar.csproj`.
