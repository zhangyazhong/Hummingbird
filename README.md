# Hummingbird

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/cc52bf9d3a9448a58eed051ddff57220)](https://app.codacy.com/app/zhangyazhong/Hummingbird?utm_source=github.com&utm_medium=referral&utm_content=zhangyazhong/Hummingbird&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/zhangyazhong/Hummingbird.svg?branch=master)](https://travis-ci.org/zhangyazhong/Hummingbird)
[![codecov](https://codecov.io/gh/zhangyazhong/Hummingbird/branch/master/graph/badge.svg)](https://codecov.io/gh/zhangyazhong/Hummingbird)
[![Language](https://img.shields.io/badge/language-java8-red.svg)](https://github.com/zhangyazhong/Hummingbird)

Hummingbird provides lots of easy-to-use tools for research experiment and contains parts shown as follows:
- a series of collections for table data handling, persistence and loading;
- multiple solutions for various configuration formats; 
- a useful tool to record time cost of code block running;
- a name manager to generate and manage names;
- execution report generation with regex supported.
- an auto-configure logging utility based on Log4j 2 

## Link with Hummingbird

Hummingbird artifacts are hosted in Maven Central. You can add a Maven dependency with the following coordinates:

```xml
<dependency>
    <groupId>cn.sissors</groupId>
    <artifactId>hummingbird</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Collections

`cn.sissors.hummingbird.collect`

### Table Container

For table data, the base class is `TableContainer<R, C, V>`. It supports 

- `push`: push a cell into container;
- `get`: get a cell from container based on row and column;
- `merge`: combine two containers into single one;
- `cut`: filter out some rows or columns based on customized rules;
- `clean`: clear container;
- `sort`: sort container based on the dictionary order of row and column keys;
- `print`: print content on to screen or customized print stream.

Besides, there is a `CSVTableContainer<R, C, V>` that extends from `TableContainer<R, C, V>` which supports to persist and load in csv format

- `persist`: write data into a csv format file;
- `load`: load data from a csv format file.

### Examples

There is a `CSVTableContainer<String, String, ResultUnit>` container, where `ResultUnit` is a simple class that only contains 2 attributes:

```java
public class ResultUnit implements Parsable<ResultUnit> {
    private double result;
    private double error;

    public ResultUnit() {
    }

    public ResultUnit(double result, double error) {
        this.result = result;
        this.error = error;
    }
}
```

The steps to create a container object is very simple:

```java
CSVTableContainer<String, String, ResultUnit> csvTableContainer =
        new CSVTableContainer<>("time", String.class, String.class, ResultUnit.class);
// or
CSVTableContainer<String, String, ResultUnit> csvTableContainer =
        new CSVTableContainer<String, String, ResultUnit>("time") {};
```

Notice, you need to specify generic types explicitly to make parser work. Then fill up with some data cells:

```java
csvTableContainer.push("2:00", "result_1", new ResultUnit(2.5, 0.3));
csvTableContainer.push("2:00", "result_0", new ResultUnit(6, 1.1));
csvTableContainer.push("1:00", "result_1", new ResultUnit(3.2, 0.4));
csvTableContainer.push("1:00", "result_0", new ResultUnit(0.8, 0.1));
csvTableContainer.push("3:00", "result_0", new ResultUnit(9, 1.5));
```

Then, call `csvTableContainer.print()` and you will get an output at console:

```
┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
│time                      │result_1                 │result_0                 │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│2:00                      │r=2.5, e=0.3             │r=6.0, e=1.1             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│1:00                      │r=3.2, e=0.4             │r=0.8, e=0.1             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│3:00                      │-                        │r=9.0, e=1.5             │
└──────────────────────────┴─────────────────────────┴─────────────────────────┘
```

Next, there is an example for combination with another container through `merge()`. Another table is shown as below:

```
┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
│time                      │result_1                 │result_0                 │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│4:00                      │r=11.0, e=2.4            │r=7.5, e=1.8             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│0:00                      │r=4.8, e=1.0             │r=8.0, e=0.9             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│5:00                      │r=2.0, e=0.6             │-                        │
└──────────────────────────┴─────────────────────────┴─────────────────────────┘
```

And after combination, we get this:

```
┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
│time                      │result_1                 │result_0                 │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│2:00                      │r=2.5, e=0.3             │r=6.0, e=1.1             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│1:00                      │r=3.2, e=0.4             │r=0.8, e=0.1             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│3:00                      │-                        │r=9.0, e=1.5             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│4:00                      │r=11.0, e=2.4            │r=7.5, e=1.8             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│0:00                      │r=4.8, e=1.0             │r=8.0, e=0.9             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│5:00                      │r=2.0, e=0.6             │-                        │
└──────────────────────────┴─────────────────────────┴─────────────────────────┘
```

For pretty showing, we can use `sort()` to make rows and columns in order:

```
┌──────────────────────────┬─────────────────────────┬─────────────────────────┐
│time                      │result_0                 │result_1                 │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│0:00                      │r=8.0, e=0.9             │r=4.8, e=1.0             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│1:00                      │r=0.8, e=0.1             │r=3.2, e=0.4             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│2:00                      │r=6.0, e=1.1             │r=2.5, e=0.3             │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│3:00                      │r=9.0, e=1.5             │-                        │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│4:00                      │r=7.5, e=1.8             │r=11.0, e=2.4            │
├──────────────────────────┼─────────────────────────┼─────────────────────────┤
│5:00                      │-                        │r=2.0, e=0.6             │
└──────────────────────────┴─────────────────────────┴─────────────────────────┘
```

<b>TODO:</b>support more authentication methods for ssh.

## Configuration 

`cn.sissors.hummingbird.runtime.config`

In brief, `Configuration` is a set of pairs in *key-value* format. After initialization, user can use `get(key)` and `set(key, value)` to fetch and update the pair respectively. Besides, `Configuration` provides a series of functions to convert value into the specified type automatically, though it's stored as a `String` inside of `Configuration` object. And currently, it's strongly recommended to use common data types such as `String`, `Integer`, `Long` and `Double` to avoid weird error.

`Configuration` is an abstract class. That's because different configuration methods have different configuration formats. For example:

`.properties` file is in `key = value` format:

```properties
system.core = 4
system.memory = 8g
system.name = ConfigTest
```

`.xml` file is in closed tag:

```xml
<system>
	<core>4</core>
	<memory>8g</memory>
	<name>ConfigTest</name>
</system>

```

`.json` file is a JSON object:

```json
{
    "system": {
        "name": "ConfigTest",
        "core": 4,
        "memory": "8g"
    }
}

```

In this toolkit, we wrote a sub-class called `PropertiesConfiguration`, which implemented `load` method to load `.properties` file into memory. If you need to use it, just create a class to extend `PropertiesConfiguration` and implement `locations()` method to return the path list of your configurations.

There is an example:

```java
public class HummingbirdConfiguration extends PropertiesConfiguration {
    @Override
    public List<String> locations() {
        return ImmutableList.of(
            "classpath: default.properties", "classpath: user-defined.properties"
        );
    }
}
```

The keyword `classpath` is refer to the root path of the project. e.g. If the project is built by maven, then `classpath` would refer to the directory of `/src/main/resources/`.

Besides, we implemented `JSONConfiguration` to support `.json` files. As shown above, keys in JSON object will be flatted with dot, such as `{"system": {"name": "ConfigTest"}}` will be loaded as `{"system.name": "ConfigTest"}`. The way to use `JSONConfiguration` is the same as `PropertiesConfiguration`.

<b>TODO: </b>

- support more configuration formats;
- support to export JSON object in `JSONConfiguration`.

## Timer

`cn.sissors.hummingbird.runtime.timer`

Timer is very common in experiment coding. But built-in method in java, that is to get timestamp through `System.currentTimeMillis()` and calculate time cost through substraction, is too complicated. So `Timer` is in order to optimize this process. By involving `Timer`, time cost calculation would change to:

```java
// old
long startTime = System.currentTimeMillis();
/*
 some code...
 */
long finishTime = System.currentTimeMillis();
long costTime = finishTime - startTime;

// new
TimerManager.create("experiment");
/*
 some code...
 */
long costTime = TimerManager.stop("experiment").time();
```

For easy to read, we implement a `format(long time)` function to convert a long number into time format such as `5232 -> 5.232, 8285232 -> 2:18:05.232`. You can use this tool by `TimerManager.format(String name)` also.

Another way to use timer is through Java annotation which is `@TimerRecord(name)`. This is only used for methods and after running, there would be a new timer called `name` stored in `Timer`. It's shown below:

```java
@TimerRecord("sleep_period")
private void sleep(long time) throws InterruptedException {
    Thread.sleep(time);
}

@Test
public void testTimerReport() throws InterruptedException {
    sleep(2000L);
    System.out.println(TimerManager.format("sleep_period"));
}
```

Besides, when using annotation way to record time, add AspectJ plugin to `pom.xml`.

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.10</version>
    <configuration>
        <complianceLevel>1.8</complianceLevel>
        <source>1.8</source>
        <aspectLibraries>
            <aspectLibrary>
                <groupId>cn.sissors</groupId>
                <artifactId>hummingbird</artifactId>
            </aspectLibrary>
        </aspectLibraries>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Another new feature of `Timer` is that it now supports for aggregation functions on time cost. 

Suppose you executed a method many times and after that you want to know the average or maximum time cost of the method. 
The older way is store time cost of each round of executions, and calculate average or maximum result.
To achieve a simpler approach, we implemented a built-in interface in `TimerManager.agg()`. Here's how to use it.

```java
@TimerRecord("sleep_period")
private void sleep(long time) throws InterruptedException {
    Thread.sleep(time);
}

@Test
public void testTimerReport() throws InterruptedException {
    for (int i = 0; i < 20; i++) {
        sleep(RandomUtils.nextInt() % 100);
    }
    System.out.println(TimerManager.agg("sleep_period", TimerAggregation.avg));
}
```

<b>TODO: </b>record time based on time point, not timer.

## NameManager

`cn.sissors.hummingbird.runtime.namespace`

`NameManager` is a global tool to distribute distinct names for application under the specified namespace. 

### How to use

```java
// applying for default namespace
String name = NameManager.uniqueName();

// applying for custom namespace
String name = NameManager.uniqueName("main");
```

When the process involving that name has finished, you can use `release(String name)` to release. If there are only a limited number of times for name distribution, release is not that necessary.

```java
// release for default namespace
NameManager.release(name);

// release for custom namespace
NameManager.release("main", name);
```

Besides, use `UNIQUE_NAME_LENGTH(int UNIQUE_NAME_LENGTH)` and `MAX_LOOP_ROUND(long MAX_CHECKING_ROUND)` to set manager.

<b>TODO: </b>

- use other ways to ensure 100% no-duplicate;
- support customize name format.

## ExecutionReport

`cn.sissors.hummingbird.runtime.report`

`ExecutionReport` is used to store running status of program. It's in key-value format and supports operations like regex search, lambda function `forEach(BiConsumer)`.

### How to use

```java
// creation
ExecutionReport report = ExecutionReport.create();
report.put("round0.partA.result", new ResultUnit(5, 3));
report.put("round0.partB.result", new ResultUnit(4, 0.5));
report.put("round1.partA.result", new ResultUnit(2, 0));
report.put("round1.partB.result", new ResultUnit(1, 1));


// print to console
report.print();
/*
┌───────────────────┬────────────┐
│key                │value       │
├───────────────────┼────────────┤
│round1.partB.result│r=1.0, e=1.0│
├───────────────────┼────────────┤
│round1.partA.result│r=2.0, e=0.0│
├───────────────────┼────────────┤
│round0.partA.result│r=5.0, e=3.0│
├───────────────────┼────────────┤
│round0.partB.result│r=4.0, e=0.5│
└───────────────────┴────────────┘
*/


// search specified keys
report.search(".*\\.partA\\..*").print();
/*
┌───────────────────┬────────────┐
│key                │value       │
├───────────────────┼────────────┤
│round1.partA.result│r=2.0, e=0.0│
├───────────────────┼────────────┤
│round0.partA.result│r=5.0, e=3.0│
└───────────────────┴────────────┘
*/


report.search("round1\\..*").print();
/*
┌───────────────────┬────────────┐
│key                │value       │
├───────────────────┼────────────┤
│round1.partB.result│r=1.0, e=1.0│
├───────────────────┼────────────┤
│round1.partA.result│r=2.0, e=0.0│
└───────────────────┴────────────┘
*/


// lambda foreach
report.forEach((key, value) -> System.out.println(String.format("%s -> %s", key, value.toString())));
/*
round1.partB.result -> r=1.0, e=1.0
round1.partA.result -> r=2.0, e=0.0
round0.partA.result -> r=5.0, e=3.0
round0.partB.result -> r=4.0, e=0.5
*/


// merge with another one
ExecutionReport report2 = ExecutionReport.create(ImmutableMap.of(
    "round2.partA.result", new ResultUnit(4, 2),
    "round2.partB.result", new ResultUnit(9, 4.5),
    "round1.partB.result", new ResultUnit(3, 1)
));
report.merge(report2).print();
/*
┌───────────────────┬────────────┐
│key                │value       │
├───────────────────┼────────────┤
│round1.partB.result│r=3.0, e=1.0│
├───────────────────┼────────────┤
│round2.partA.result│r=4.0, e=2.0│
├───────────────────┼────────────┤
│round2.partB.result│r=9.0, e=4.5│
├───────────────────┼────────────┤
│round1.partA.result│r=2.0, e=0.0│
├───────────────────┼────────────┤
│round0.partA.result│r=5.0, e=3.0│
├───────────────────┼────────────┤
│round0.partB.result│r=4.0, e=0.5│
└───────────────────┴────────────┘
*/
```

## Logger

`cn.sissors.hummingbird.runtime.logger`

Logger configuration is often a troublesome task, especially writen in XML format. `Logger` is an auto-configure logging utility based on Log4j 2 and helps you to build your own logging module within few code statements. 

Pre-configured logger support 4 logging levels:

- debug,
- info,
- warn,
- error. 

Logger contains 3 appenders, which are console, single runtime file and rolling file. Besides, single runtime file and rolling file are writen asynchronously to improve performace. 

### How to use

```java
// with customized XML configuration at classpath
Logger.log(Logger.Level.DEBUG, "test debug");
Logger.debug("test debug");

// without XML configuration, invoke Logger.build() first
Logger.build("/tmp/hummingbird/log/", "hummingbird");
Logger.log(Logger.Level.DEBUG, "test debug");
Logger.debug("test debug");
```

Output log is in this format ([%d{yyyy-MM-dd HH:mm:ss.SSS}][%p] - [class.method:line-number] %m%n)

```shell
[2018-11-29 15:57:28.803] [DEBUG] - [App.main:12] debug: Hello world!
[2018-11-29 15:57:28.808] [INFO] - [App.main:13] info: Hello world!
[2018-11-29 15:57:28.808] [WARN] - [App.main:14] warn: Hello world!
[2018-11-29 15:57:28.808] [ERROR] - [App.main:15] error: Hello world!
```

Furthermore, if log string contains `\n` or `\r\n`, logger would split it to multiple lines automatically.

```java
Logger.build();
Logger.debug("Hello\r\nWorld!");
```

Output is

```shell
[2018-11-29 16:01:00.676] [DEBUG] - [App.main:16] Hello
[2018-11-29 16:01:00.676] [DEBUG] - [App.main:16] World!
```

<b>TODO:</b>

- provide more customized options for logging;
- support for logger rebuilding.