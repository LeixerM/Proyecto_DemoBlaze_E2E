# Regression baseline

This repository replaces two earlier projects, [`Demoblaze`](https://github.com/LeixerM/Demoblaze) (UI) and
[`APIDemoblaze`](https://github.com/LeixerM/APIDemoblaze) (API). Before anything was upgraded, both original
suites were run unchanged so that no previously working behaviour is lost in the move.

- Date: 2026-10-03
- Machine: Windows 11, JDK 21.0.9 (Temurin), Google Chrome (stable)
- Original sources: `Demoblaze@eea753d`, `APIDemoblaze@6a1cd5c` (default branches)

## 1. Baseline: original suites, unchanged

| Project | Command | Original stack |
|---|---|---|
| `Demoblaze` | `./gradlew clean test` (its own Gradle 9.0.0 wrapper) | Serenity BDD 4.2.1, Cucumber 7.16.1, JUnit 5.11.0 |
| `APIDemoblaze` | `./gradlew clean test` (its own Gradle 9.0.0 wrapper) | Karate 1.4.1 (`com.intuit.karate:karate-junit5`) |

Both projects run on Java 21 without changes.

| Project | Scenario | Result | Reason |
|---|---|---|---|
| Demoblaze | Make a successful purchase | **FAIL** | `Expected: is <true> but: was <false>` from the combined boolean question. All navigation, cart and checkout steps passed. Only the final check failed. The expected receipt date is built from the hard-coded `"month": "2"` and `"year": "2026"` in `dataPurchase.json`, so this check could only pass during February 2026. The single boolean hides which field differed. |
| APIDemoblaze | login password failed (`@loginFailed`) | PASS | |
| APIDemoblaze | login successfully | not run | The original runner hard-codes `.tags("@loginFailed")` |
| APIDemoblaze | Create a new account successfully | not run | Same reason |
| APIDemoblaze | Attempt to create existing user | not run | Same reason |

The tag filter left three API scenarios unexecuted by default. To capture their behaviour as well, they were run
once from a throw-away copy with only that `.tags(...)` call removed (no other change):

| Scenario | Result |
|---|---|
| login successfully | PASS (status 200 only; static user `user_51776892`) |
| login password failed | PASS |
| Create a new account successfully | PASS |
| Attempt to create existing user | PASS (static user `admin`) |

## 2. Version changes

| Dependency | Old | New |
|---|---|---|
| Build | two separate Gradle projects | one Gradle multi-module build (`api-tests`, `ui-tests`) |
| Gradle wrapper | 9.0.0 | 8.14.5 (same wrapper as the other upgraded Serenity projects) |
| Java | 21 (toolchain not pinned) | 21 (Gradle toolchain) |
| Serenity BDD | 4.2.1 | 5.3.11 |
| Serenity Gradle plugin | 4.2.1 | 5.3.9 |
| Cucumber (`cucumber-junit-platform-engine`) | 7.16.1 | 7.34.2 |
| JUnit (UI module) | Jupiter 5.11.0 / Platform 1.11.0 | 6.0.3 (BOM) |
| Cucumber reporter | `io.cucumber.core.plugin.SerenityReporter` | `net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel` |
| Karate | `com.intuit.karate:karate-junit5:1.4.1` | `io.karatelabs:karate-junit6:2.1.3` |
| JUnit (API module) | 5.x (from Karate 1.4.1) | 6.1.3 (BOM, matches Karate 2.1.3) |
| AssertJ, jxl (UI) | 3.26.3, unused import | removed (assertions use Serenity `Ensure`) |

Versions were checked against Maven Central `maven-metadata.xml` on 2026-10-03. Karate 2.x publishes its JUnit
integration as `io.karatelabs:karate-junit6`. The last `io.karatelabs:karate-junit5` release is 1.5.2.

Step-by-step upgrading did not apply to this repository: it is a new project built on the target stack, not an
in-place bump. Instead, every baseline behaviour is mapped to a scenario that runs on the new stack (below).

## 3. Scenario mapping (old → new)

| Old scenario | New scenario | Baseline | Upgraded (2026-10-03) |
|---|---|---|---|
| Demoblaze: Make a successful purchase | `purchase.feature`: A guest buys two products and receives a matching receipt | FAIL (date check tied to Feb 2026) | PASS. Each receipt field is checked separately. The date is computed from the current day. |
| APIDemoblaze: login password failed | `login.feature`: Logging in with a wrong password is rejected | PASS | PASS |
| APIDemoblaze: login successfully | `login.feature`: A registered user logs in and receives an auth token | PASS (not in default run) | PASS. Now also checks the `Auth_token` body and that the token decodes to the username. |
| APIDemoblaze: Create a new account successfully | `signup.feature`: A new user signs up with a unique username | PASS (not in default run) | PASS |
| APIDemoblaze: Attempt to create existing user | `signup.feature`: Signing up twice with the same username is rejected | PASS (not in default run) | PASS. Uses a user created in the same scenario, not the shared `admin` account. |

New coverage with no predecessor: an unknown-user login, the `/entries`, `/view` and `/bycat` catalog endpoints
with schema checks, a product that does not exist, cart removal and totals, order-form validation, a login
through the UI with an account created through the API (plus its wrong-password case), and 7 unit tests.

No baseline-passing behaviour was dropped or regressed.

Upgraded run: `./gradlew clean test aggregate` passed 23 tests with 0 failures (11 Karate scenarios, 5 Cucumber
scenarios and 7 unit tests).
