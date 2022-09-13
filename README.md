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

#### Stateful

In order to benefit from this library, create new classes extending `AutomatedBrowser` and use the methods available in 
it to simplify the implementation of your requirements.

An example of extension available in the tests of the project is the `MavenCentral` class. Its goal is to get the 
markdown of the last version of an artifact and take a screenshot of it. It can be used with:

```java
try (MavenCentral browser = new MavenCentral(driver)) {
    browser.searchArtifactBadge("org.slf4j:slf4j-api");
}
```

#### Stateful - Flow

There is also a possibility to have a flow with multiple steps defining your requirements, using the classes extending 
`AutomatedBrowser` you created:

```java
try (MavenCentral browser = new MavenCentral(driver)) {
    browser.setData(new MavenCentralData("org.slf4j:slf4j-api"));
    new AutomatedBrowserFlow<>(browser)
        .withStep(MavenCentral::stepSearchArtifact)
        .withStep(MavenCentral::stepLogLastVersion)
        .withStep(MavenCentral::stepHighlightBadge);
}
```

Note that you can also give a default exception handler for all step or a specific one overriding it for some steps.

#### Stateless - Flow

Also with the same flow system, you can have stateless calls to static methods. Those methods can have:
- No parameter
- One parameter: the class extending `AutomatedBrowser` you gave in the constructor (defaulting to `AutomatedBrowser`)
- Two parameters: first same as above and second the context data instance you can set with `withContext` method

```java
try (MavenCentral browser = new MavenCentral(driver)) {
    new AutomatedBrowserFlow<MavenCentral, MavenCentralData>(browser)
        .withContext(new MavenCentralData("org.slf4j:slf4j-api"))
        .withStep(MavenCentralStep::stepSearchArtifact)
        .withStep(MavenCentralStep::stepLogLastVersion)
        .withStep(MavenCentralStep::stepHighlightBadge);
}
```

### Running your application

#### As a standalone application

Creates your preferred the driver using the methods in `BrowserUtils`:

- Chrome (`getChromeDriver`)
- Firefox (`getFirefoxDriver`)
- Edge (`getEdgeDriver`)

Then pass it to the classes you created (see chapter above) and start using them directly. Note that this will use the
browsers available on your computer, meaning when you choose a browser driver in the code, you need that browser
installed on your computer to make it working.

#### As a docker container

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