package demoblaze.api;

import io.karatelabs.junit6.Karate;

/**
 * Runs every feature under {@code demoblaze/api} and reports each scenario as a JUnit test.
 * The Karate HTML report is written to {@code build/karate-reports}.
 */
class DemoblazeApiTest {

    @Karate.Test
    Karate demoblazeApi() {
        return Karate.run("classpath:demoblaze/api")
                .tags("~@ignore")
                .outputDir("build/karate-reports")
                .outputHtmlReport(true);
    }
}
