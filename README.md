# QA Automation Portfolio

A small Java 17 project demonstrating UI and REST API test automation with TestNG.
The suite contains five independent smoke tests:

- **Successful login:** log in as `standard_user` and verify the inventory URL and product catalog.
- **Negative login:** attempt login as `locked_out_user` and verify the exact error message while the login form remains visible.
- **Add product to cart:** add one Sauce Labs Backpack, verify the cart badge is `1`, and check the product name and quantity in the cart.
- **Complete checkout:** add one Sauce Labs Backpack, enter `Nikita`, `Test`, and postal code `6000`, verify the order overview, finish checkout, and check the success confirmation.
- **Restful Booker API:** create a booking, check HTTP 200 and JSON content type, and verify the generated ID and every returned booking field.

## Stack

| Tool | Purpose |
| --- | --- |
| Java 17 and Maven | Compilation, dependency management, and test execution |
| TestNG | Test lifecycle, assertions, and `ui`, `api`, and `smoke` groups |
| Selenide | Browser automation, automatic waits, and browser driver management |
| REST Assured | HTTP requests and response validation |
| Allure | Test reports, UI steps, failure screenshots, and HTTP attachments |
| Lombok | Model getters, setters, constructors, builders, and equality |
| Jackson | Convert Java models to and from JSON |

Dependency and plugin versions are pinned in `pom.xml`. All automation code and its dependencies use Maven's test scope.

## Prerequisites

- JDK 17, with `JAVA_HOME` pointing to that JDK.
- Maven 3.9 or newer on `PATH`.
- Google Chrome installed for the default UI run.
- Internet access to Maven Central, browser driver downloads, SauceDemo, and Restful Booker.

Check **both** commands: Maven can use a different Java installation from the one on `PATH`.

```shell
java -version
mvn -version
```

Both should report Java 17. To select an installed JDK for the current PowerShell session:

```powershell
$env:JAVA_HOME = 'C:\path\to\your\jdk-17'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn -version
```

In an IDE, import the project as Maven, select JDK 17, and enable annotation processing for Lombok if the IDE requires it.

## Run tests

From the repository root, run all smoke tests:

```shell
mvn test
```

Chrome runs headless by default. Selenide/Selenium Manager resolves the browser driver automatically.
Each UI test closes its browser in an `alwaysRun` teardown, including after failures.
The next test starts a fresh browser session with its own login and cart state.

Run one group or one class:

```shell
mvn test "-Dgroups=ui"
mvn test "-Dgroups=api"
mvn test "-Dtest=SauceDemoSmokeTest"
mvn test "-Dtest=RestfulBookerSmokeTest"
```

Show the browser, or use an installed Microsoft Edge:

```shell
mvn test "-Dgroups=ui" "-Dselenide.headless=false"
mvn test "-Dgroups=ui" "-Dselenide.browser=edge"
```

Quotes around `-D` arguments also make these commands safe to copy into PowerShell.

## Configuration

| System property | Default | Purpose |
| --- | --- | --- |
| `ui.baseUrl` | `https://www.saucedemo.com` | UI application URL |
| `api.baseUrl` | `https://restful-booker.herokuapp.com` | REST API URL |
| `selenide.browser` | `chrome` | Browser name |
| `selenide.headless` | `true` | Run without a visible browser window |

Override a value using Maven, for example `mvn test "-Dgroups=api" "-Dapi.baseUrl=http://localhost:3001"`.
Alternative URLs must serve applications with the same UI/API contract.

`UiConfig` sets a 10-second element timeout and a 30-second page load timeout.
`ApiConfig` sets a 10-second connection timeout and a 30-second socket timeout,
JSON headers, Jackson mapping, and request/response logging when REST Assured validation fails.
Tests run sequentially by default, and UI and API tests have no dependency on each other.

These tests exercise public demo services, so availability and network access affect results.
The API test creates one demo booking per run and validates its creation response. It does not depend on an existing booking ID.
It leaves the demo record for the service's automatic reset; [Restful Booker resets its data every 10 minutes](https://restful-booker.herokuapp.com/).
The SauceDemo credentials are public sample credentials displayed by the demo site.

## Allure reports

The Allure TestNG adapter automatically records the test lifecycle. `Allure.step(...)`
adds readable steps without requiring an AspectJ agent. The Selenide listener attaches
screenshots and page HTML for failed Selenide checks; the REST Assured filter attaches
HTTP requests and responses.

Run a clean suite and generate the report:

```shell
mvn clean test
mvn allure:report
```

To generate and open the report in a local server:

```shell
mvn allure:serve
```

Stop the server with `Ctrl+C`. The Maven plugin downloads the pinned Allure 2 CLI on first use;
no separate Allure or Node.js installation is required.

Generated artifacts (ignored by Git):

- `target/surefire-reports/`: Maven/TestNG execution results.
- `target/allure-results/`: raw Allure results and attachments.
- `target/site/allure-maven-plugin/`: generated HTML report.
- `target/selenide-reports/`: UI failure screenshots and page sources.
- `.allure/`: downloaded Allure CLI.

Use `mvn clean test` when you want reports containing only the latest run.

## Project structure

```text
.
|-- pom.xml
|-- .gitignore
|-- README.md
`-- src/test/
    |-- java/
    |   |-- ui/
    |   |   `-- SauceDemoSmokeTest.java
    |   |-- api/
    |   |   `-- RestfulBookerSmokeTest.java
    |   |-- pages/
    |   |   |-- LoginPage.java
    |   |   |-- InventoryPage.java
    |   |   |-- CartPage.java
    |   |   |-- CheckoutPage.java
    |   |   |-- CheckoutOverviewPage.java
    |   |   `-- CheckoutCompletePage.java
    |   |-- models/
    |   |   |-- Booking.java
    |   |   |-- BookingDates.java
    |   |   `-- BookingResponse.java
    |   |-- config/
    |   |   |-- UiConfig.java
    |   |   `-- ApiConfig.java
    |   `-- utils/
    |       `-- TestData.java
    `-- resources/
        `-- allure.properties
```

| File | Responsibility |
| --- | --- |
| `pom.xml` | Java 17 compilation, dependencies, Lombok annotation processing, Surefire test runner, and Allure report plugin |
| `.gitignore` | Excludes build output, reports, IDE files, and local environment files |
| `SauceDemoSmokeTest.java` | Four independent UI scenarios, shared flow helpers, and browser setup/teardown |
| `RestfulBookerSmokeTest.java` | Booking creation request and assertions |
| `LoginPage.java` | Login form interactions and error assertions |
| `InventoryPage.java` | Inventory assertions, product selection, cart badge checks, and cart navigation |
| `CartPage.java` | Selected product and quantity assertions, plus navigation to checkout |
| `CheckoutPage.java` | Customer information form and navigation to the overview |
| `CheckoutOverviewPage.java` | Order contents and the Finish action |
| `CheckoutCompletePage.java` | Successful order confirmation |
| `Booking.java` | Booking request and returned booking details |
| `BookingDates.java` | Check-in/check-out dates in the API's ISO date format |
| `BookingResponse.java` | Creation response containing the generated ID and booking |
| `UiConfig.java` | Selenide settings and Allure UI listener |
| `ApiConfig.java` | Fresh REST Assured request specification with JSON mapping and reporting |
| `TestData.java` | Creates a fresh sample booking with future dates |
| `allure.properties` | Stores Allure results under Maven's `target` directory |

The design uses small page objects and direct API requests. Add a new `*Test.java`
class in `ui` or `api` to extend the suite; Maven discovers it automatically.

UI selectors stay inside page objects and use SauceDemo's `data-test` attributes.
Page methods describe actions or assertions; navigation methods return the next page object.
Selenide assertions wait automatically, so no fixed sleeps are needed.
Small private helpers in the UI test class reuse login and cart navigation, while every test
prepares its own state. There are no test dependencies, base page classes, or shared browser sessions.

## References

- [SauceDemo](https://www.saucedemo.com/)
- [Restful Booker API documentation](https://restful-booker.herokuapp.com/apidoc/index.html)
- [Selenide quick start](https://selenide.org/quick-start.html)
- [REST Assured](https://rest-assured.io/)
- [Allure TestNG documentation](https://allurereport.org/docs/testng/)
- [Allure Maven integration](https://allurereport.org/docs/integrations-maven/)
