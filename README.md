# MeetMe

## How to Run
> ## Project Requirements:
> - [MySQL](https://www.mysql.com/downloads/)
> - [Apache Maven](https://maven.apache.org/download.cgi)
> - [Docker for Mac](https://docs.docker.com/docker-for-mac/install/)
> - [Docker for Windows](https://docs.docker.com/docker-for-windows/install/)

To build the project and package the resulting JAR file into the target directory, run the following command in the parent directory:
```console
$ mvn package
```

After the target directory has been successfully created, the payara server can be started by running the following command:
```console
$ docker-compose up
```

Now that the payara server has been successfully set up, all you need to do to view the application is go to the browser of your choice to the link "http://localhost:8080/MeetMe-5.13/"

To later gracefully stop the payara server, press `ctrl + c` and enter the following command in the parent directory:
```console
$ docker-compose down
```

## Abstract
### Motivation:
Scheduling meetings through email can often be tedious and unreliable as participants must negotiate a time and date that works for everyone. The process of emailing back-and-forth can also lead to miscommunication between participants. Our website will allow users to schedule meetings in a simple and convenient manner.

### The advantage of using a web platform to implement the project:
Because our scheduling system is web-based, this allows end users to access the website and create appointments or meetings at any time of the day. Our website will have an intuitive design to optimize ease of use while offering maximum functionality.

### Project goals and planned outcomes:
To schedule a meeting, the user must input the names of the participants as well as the date in which the meeting will be set. Given the date, the user will be prompted with a list of available time blocks for that day. The user will also have the option to give the meeting a title as well as a subject line or description. Not all meetings are closed meetings, therefore, our site will include the option to make any meeting private or public. Finally, once the user inputs the required information, the system will send an email invitation to each of the attendees.
