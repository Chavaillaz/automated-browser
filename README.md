# Automated Browser

![Quality Gate](https://github.com/chavaillaz/automated-browser/actions/workflows/sonarcloud.yml/badge.svg)
![Dependency Check](https://github.com/chavaillaz/automated-browser/actions/workflows/snyk.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.chavaillaz/automated-browser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.chavaillaz/automated-browser)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This library allows to quickly do an automated application controlling a browser for multiple purposes. An example of
usage is to automate the generation of reports for external parties by accessing internal services, highlighting
elements, taking screenshots and creating documents with them.

## Installation

The dependency is available in maven central (see badge for version):

```xml
<dependency>
    <groupId>com.chavaillaz</groupId>
    <artifactId>automated-browser</artifactId>
</dependency>
```

## Usage

An example of usage is available in the ```MavenCentralTest``` class.

### Implementing your requirements

In order to benefit from this library, create new classes extending `AutomatedBrowser` and use the methods available in 
it to simplify the implementation of your requirements.

### Running as a standalone application

Creates your preferred the driver using the methods in `BrowserUtils`:

- Chrome (`getChromeDriver`)
- Firefox (`getFirefoxDriver`)
- Edge (`getEdgeDriver`)

Then pass it to the classes you created (see chapter above) and start using them directly. Note that this will use the
browsers available on your computer, meaning when you choose a browser driver in the code, you need that browser
installed on your computer to make it working.

### Running in a docker container

Thanks to test containers, you can also directly run it in a docker container containing the browser you want to use.
For that, include the following dependencies in your project:

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>selenium</artifactId>
    <version>${test-containers.version}</version>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>${test-containers.version}</version>
</dependency>
```

When it's done, add the `@Testcontainers` annotation on your test classes and the following attribute for the
container (in this example Google Chrome):

```java
@Container
public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
        .withCapabilities(getChromeOptions())
        .withRecordingMode(SKIP, null);
```

You can then access the driver with `chrome.getWebDriver()` and pass it when instantiating your classes.

## Contributing

If you have a feature request or found a bug, you can:

- Write an issue
- Create a pull request

If you want to contribute then

- Please write tests covering all your changes
- Ensure you didn't break the build by running `mvn test`
- Fork the repo and create a pull request

## License

This project is under Apache 2.0 License.