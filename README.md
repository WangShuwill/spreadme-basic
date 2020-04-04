<h1 align="center">spreadme commons</h1>

<p align="center">
    <a href="#Apahce"><img src="https://img.shields.io/badge/License-Apache-brightgreen.svg" alt="Apache"></a>
    <a href="#Maven"><img src="https://img.shields.io/badge/Build-Maven-blue.svg" alt="Maven"></a>
    <a href="#Java"><img src="https://img.shields.io/badge/Programma-Java8+-important.svg" alt="Java8+"></a>
</p>

spreadme-frame是一套用于Java开发的基础工具框架,集成了常用的工具类,让Java开发
变得更加简单高效.

Liense
------
This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).

See the `NOTICE.txt` file for required notices and attributions.

Install
-------
- #### Maven
- #### java1.8+
- #### spread-commons
``` xml
<dependency>
    <groupId>org.spreadme</groupId>
    <artifactId>spreadme-commons</artifactId>
    <version>1.5.2</version>
</dependency>
```

Example
-------
- Date Util
```java
// 线程安全的时间解析工具
String formatter = "HH:mm:ss dd.MM.yyyy";
String text = "19:30:55 03.05.2015";
Date data = Dates.parse(text, formatter);
// Sun May 03 19:30:55 CST 2015

// 格式化日期
Dates.format(new Date(), formatter)
// 11:00:40 03.04.2020

// 其他用法
Console.info("今天开始时间: %s", Dates.getStartOfDate(new Date()));
Console.info("今天结束时间: %s", Dates.getEndOfDate(new Date()));
Console.info("100天前的时间: %s", Dates.getDate(new Date(), ChronoUnit.DAYS, -100));
Console.info("时间戳: %s", Dates.getTimestamp());
Date oldDate = Dates.parse("19:30:55 03.05.2015", formatter);
Duration duration = Dates.getDuration(new Date(), oldDate);
Console.info("相差天数: %d", duration.toDays());
```

- IO Util
```java
IOUtil.copy(InputStream, OutputStream);

RepeatableInputStream in = IOUtil.toRepeatable(InputStream inputStream);

IOUtil.zipFiles(List<File> files, OutputStream out);

IOUtil.zipResouces(final List<Resource> entries, OutputStream out)
...
```

- 验证码工具 Captcha
```java
CaptchaCode code = LineCaptcha.of(200, 50).create();
```
<img src="https://spreadme.oss-cn-shanghai.aliyuncs.com/static/img/captcha.png" alt="Apache"></a>

```java
CaptchaCode code = CurvesCaptcha.of(200, 50).create();
```
<img src="https://spreadme.oss-cn-shanghai.aliyuncs.com/static/img/CurvesCaptcha.png" alt="Apache"></a>