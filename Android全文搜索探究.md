# **Android全文搜索探究**

## **前言**

安通+一直存在消息搜索的呼声，但是由于安通+消息加密，即消息是密文存储，如果支持搜索，会一定程度上破坏消息的保密性，所以我们暂未回应此类需求。

然而集成ImSdk的IMUIKIT在提供给OA使用的时候，OA也提出需要进行消息搜索，他们没有加密，所以就对搜索功能进行了一些探究。

通过本次学习，大家能够了解下列相关内容：

* 什么是全文搜索

* 全文搜索原理，涉及到的索引，倒排索引，B-树等

* 在移动端如何实现全文搜索

## **需求分析**

我们的消息是存储在数据库即SQLite中的。既然是搜索，很自然的就想到了使用 LIKE 子句进行匹配查询。

使用下面的查询语句，立马就达到了想要的效果：

~~~ 
SELECT msg FROM message WHERE content LIKE '%?%'; 
~~~

然后进行基准测试(benchmark)，测试结果如下：

**测试手机:** ACE手机

**表:** 随机字符串插入普通表。

**表量级:** 10w,20w,30w,40w,50w,70w,100w,500w

**搜索策略:** 每个量级取1，2，3，4个相同关键字测试，每个关键字测试10遍取耗时平均数

**搜索语句:**

~~~
SELECT * FROM message WHERE msg LIKE '%?%'
~~~
 
 **搜索结果:** 
 
 |-|关键字/个|结果/条|耗时/ms|关键字/个|结果/条|耗时/ms|关键字/个|结果/条|耗时/ms|关键字/个|结果/条|耗时/ms|
 |:--:|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|:--|
 |普通搜索/10w|1|56952|432|2|594|497|3|4|489|4|0|486|
 |全文检索/10w|1|56952|433|2|594|87|3|4|72|4|0|4|
 |普通搜索/20w|1|72018|841|2|978|913|3|19|894|4|1|909|
 |全文检索/20w|1|72018|570|2|978|109|3|19|104|4|1|123|
 |普通搜索/30w|1|110646|1194|2|2019|1371|3|33|1331|4|2|1328|
 |全文检索/30w|1|110646|784|2|2019|169|3|33|145|4|2|151|
 |普通搜索/40w|1|227821|1375|2|5914|1823|3|56|1776|4|2|1763|
 |全文检索/40w|1|227821|1366|2|5914|253|3|56|179|4|2|205|
 |普通搜索/50w|1|183989|1876|2|1556|2215|3|32|2171|4|1|2167|
 |全文检索/50w|1|183989|1086|2|1556|176|3|32|184|4|1|233|
 |普通搜索/70w|1|257055|2629|2|2333|3071|3|52|3014|4|1|3021|
 |全文检索/70w|1|257055|1667|2|2333|231|3|52|238|4|1|227|
 |普通搜索/100w|1|368484|3695|2|8246|4372|3|50|4285|4|1|4271|
 |全文检索/100w|1|368484|2341|2|8246|387|3|50|305|4|1|341|
 

在上面的测试结果中，SQLite的LIKE查询效率随着数据数量级的增加，查询效率显著下降。到百万量级的时候，查询速度降到了秒级。

作为一款即时通讯应用，用户的本地数据是随着时间线性增长的。所以，需要对数据规模进行估计。

在[2018微信数据报告](https://news.china.com/socialgd/10000169/20190110/34955079.html)里，有如下统计：

>2018年，每天有10.1亿用户登陆微信；日发送微信消息450亿条

通过这个报告，可以计算每个用户平均日发送消息45条。如果一个用户有50个相关好友和群的消息，则每年消息数会达到821250条。

对于较长使用时间的用户来说，本地数据数量级到达百万级别轻而易举。

秒级的查询速度无法被接受。

分析SQLite源码，表达式 LIKE 最后是转换为 like() 方法。在 sqlite3.c 中：

~~~
  // 实现SQL like()函数，这个函数实现了内置的LIKE操作符。函数的第一个参数是模式和第二个参数是字符串。  
  // 因此SOL语句为 A LIKE B 被实现为like(B,A).同样的功能(不同的compareInfo结构)计算通配符匹配操作符运算符。
  /*
  ** Implementation of the like() SQL function.  This function implements
  ** the build-in LIKE operator.  The first argument to the function is the
  ** pattern and the second argument is the string.  So, the SQL statements:
  **
  **       A LIKE B
  **
  ** is implemented as like(B,A).
  ** ...
  */
  static void likeFunc(
    sqlite3_context *context, 
    int argc, 
    sqlite3_value **argv
  )
~~~

此函数实现流程如下：

- 给要比较的两个模式zA和zB赋值，限制LIKE模式中zA和zB的长度以避免在patternCompare()里的太复杂

- 保持zB编码没有以改变， 转义字符的字符串zEsc必须包括一个单UTF-8字符。 否则，返回一个错误

- 判断模式zA和模式zB是否存在，若存在，进入compareInfo结构定义中做LIKE模式匹配，如果比较的两个比较两个UTF-8字符串相等， 
  返回true，如果他们两个不相等，返回false

在 patternCompare() 中：

~~~
/*
** ...
** This routine is usually quick, but can be N**2 in the worst case.
*/
static int patternCompare(
  const u8 *zPattern,              /* The glob pattern */
  const u8 *zString,               /* The string to compare against the glob */
  const struct compareInfo *pInfo, /* Information about how to do the compare */
  u32 matchOther                   /* The escape char (LIKE) or '[' (GLOB) */
)
~~~

在这里面，最终用需要查询的字段做了全文匹配。所以，如果数据量小，效率还是可以的。

如果数据量较庞大，那么查询效率就是一个很大的问题。

在 SQLite 中，我们会在经常被搜索的列上建立索引，就像查字典先看目录这样，来加快查询速度。

> 为什么不在 content 列建立索引

同样的，在进行大数据量搜索时，SQLite 提供了SQLite FTS(Full-Text-Search) Extension，它是 SQLite 为全文搜索开发的插件，

内嵌在标准的 SQLite 发布版本当中。 主要有如下特点：

- **搜索速度快：** 使用倒排索引加速查找过程

- **稳定性好：** 目前SQLite 在移动端的稳定性较为良好，FTS Extension 即是在 SQLite 基础上搭建而成的

- **接入简单：**  Android 和 iOS 平台本身就支持 SQLite，且 FTS Extension 的使用与 SQLite 表无异

- **兼容性好：** 得益于 SQLite 本身良好的兼容性，SQLite FTS Extension 也拥有很好的兼容性

在SQLite对[FTS](https://www.sqlite.org/fts3.html)的官方介绍中，有如下内容：

>For example, if each of the 517430 documents in the "Enron E-Mail Dataset" is inserted  
into both an FTS table and an ordinary SQLite table created using the following SQL script: 

~~~
CREATE VIRTUAL TABLE enrondata1 USING fts3(content TEXT);     /* FTS3 table */
CREATE TABLE enrondata2(content TEXT);                        /* Ordinary table */
~~~

>Then either of the two queries below may be executed to find the number of documents in  
the database that contain the word "linux" (351). Using one desktop PC hardware configuration,  
the query on the FTS3 table returns in approximately 0.03 seconds, versus 22.5 for querying the ordinary table. 

~~~
SELECT count(*) FROM enrondata1 WHERE content MATCH 'linux';  /* 0.03 seconds */
SELECT count(*) FROM enrondata2 WHERE content LIKE '%linux%'; /* 22.5 seconds */
~~~

在上面的介绍中，可以看到FTS对查询效率有显著的提升。

SQLite 提供的FTS有5个版本，其中1和2已经废弃。主流的3个版本特点如下：

- **FTS3:** 基础版本，具有完整的 FTS 特性，支持自定义分词器，库函数包括 Offsets、Snippet

- **FTS4:** 在 FTS3 的基础上，性能有较大优化，增加相关性函数计算 MatchInfo

- **FTS5:** 和 FTS4 相比有较大变动，储存格式上有较大改进，最明显就是 Instance-List 的分段存储，能够支持更  
大的 Instance-List 存储；并且开放 ExtensionAPI，支持自定义辅助函数

在Android平台上的对应关系如下：

|Android API|SQLite Version|FTS Version|
|:--:|:--:|:--:|
|API 26|3.18|FTS5|
|API 24|3.9|FTS5|
|API 21|3.8|FTS4|
|API 11|3.7|FTS4|
|API 3|3.5|FTS3|

鉴于需要版本兼容，我们应该考虑使用FTS4。同样进行基准测试，结果如下：

**测试手机:** ACE手机

**表:** 随机字符串插入普通表。

**表量级:** 10w,20w,30w,40w,50w,70w,100w,500w

**搜索策略:** 每个量级取1，2，3，4个相同关键字测试，每个关键字测试10遍取耗时平均数

**搜索语句:**

~~~
SELECT * FROM ftsmessage WHERE ftsmessage MATCH ?
~~~
 
 **搜索结果:** 

> 两种方式搜索结果

在上面的结果中，可以看到两种查询方式的结果存在一些差异。这里，FTS的官方介绍有如下描述：

>Of course, the two queries above are not entirely equivalent. For example the LIKE query  
matches rows that contain terms such as "linuxophobe" or "EnterpriseLinux" (as it happens,  
the Enron E-Mail Dataset does not actually contain any such terms), whereas the MATCH query  
on the FTS3 table selects only those rows that contain "linux" as a discrete token. Both  
searches are case-insensitive. The FTS3 table consumes around 2006 MB on disk compared to  
just 1453 MB for the ordinary table. Using the same hardware configuration used to perform  
the SELECT queries above, the FTS3 table took just under 31 minutes to populate, versus 25  
for the ordinary table. 

这是由于二者的原理不同导致的。LIKE是通过全文匹配查询，FTS是通过索引程序扫描全文，对词建立全文分词索引。

这里的全文分词索引和数据库索引不是一个概念。索引主要是针对表中的一列或者是多列建立的升序或者是降序的排列。

全文索引是索引的升级，他是针对整个文件的字符匹配。而且建立全文索引以后就可以对创建了该索引的表进行全文检索。

FTS工作原理如下：

>原理图

当存储一条文档数据时，解析器与分词器将该文档数据划分成各自独立的词项，并为每个词项建立一个倒排索引。

当查询时，解析器与分词器将查询数据进行词项划分，然后遍历倒排索引，找到其相应的记录号，最后根据与查询条件的相关性等排序规则，返回结果集。

在上图中，**分词器**和**倒排索引**是实现全文搜索的关键。

- **分词器**

FTS3和 FTS4提供四种系统分词器，除了系统分词器外，也可以自定义分词器，需要在C层实现。这里先介绍系统分词器

|类型|中文|特性|注意|
|:--:|:--:|:--:|:--:|
|simple|否|连续的合法字符（unicode大于等于128）和数字组词|全都会转换为小写字母|
|porter|否|同上，支持生成原语义词（如一个语义的动词、名词、形容词会生成统一的原始词汇）|同上|
|icu|是|多语言，需要指明本地化语言，根据语义进行分词（如“北京欢迎你”，可以分为“北”、“北京”、“欢迎”、“欢迎你”等词汇）|可以自定义分词规则|
|unicode61|否|特殊字符分词，（unicode的空格+字符，如“:”、“-”等）|只能处理ASCII字符，需要SQLite 3.7.13及以上|

使用方式

~~~
CREATE VIRTUAL TABLE fts_test USING fts4(message, tokenize=simple);
~~~

- **倒排索引**

倒排索引源于实际应用中需要根据属性的值来查找记录。这种索引表中的每一项都包括一个属性值和具有该属性值的各记录的地址。由于不是由记录来确定属性值，而是由属性值来确定记录的位置，因而称为倒排索引(inverted index)。
它是一种索引方法，被用来存储在全文搜索下某个单词在一个文档或者一组文档中的存储位置的映射。它是文档检索系统中最常用的数据结构。通过倒排索引，可以根据单词快速获取包含这个单词的文档列表。倒排索引主要由两个部分组成：“单词词典”和“倒排文件”。
 
倒排文件
用记录的非主属性值（也叫副键）来查找记录而组织的文件叫倒排文件，即次索引。倒排文件中包括了所有副键值，并列出了与之有关的所有记录主键值，主要用于复杂查询。

单词词典（难点）
是由文档集合中出现过的所有单词构成的字符串集合，单词词典内每条索引项记载单词本身的一些信息以及指向“倒排列表”的指针。（常用的数据结构包含哈希加链表和树形词典结构）

> 引入FTS原理 若只用一句话概括，FTS搜索的原理是将文字信息通过分词器切断为字词组成的数组，并以此建立搜索树。因此，分词器是搜索效率、准确度的关键。
          SQLite内置了simple、porter、unicode61等多个分词器，但其适合场景有限，这里不做深入讨论，开发者可自行搜索。
          这里重点讨论WCDB内置的分词器， WCTTokenizerNameApple 和 WCTTokenizerNameWCDB 。

因为我们主要是中文，所以选择支持中文的ICU分词器和unicode61进行测试，结果如下：

>分词器测试结果

但是测试结果也不理想，无法满足实际项目需求。主要是分词器无法友好的支持中文。

主要存在以下问题，摘自["SQLite FTS3/FTS4与一些使用心得"](https://blog.csdn.net/jessicaiu/article/details/83107634) :

> - 查询语法过于模糊，容易产生歧义，搜索结果不可控
> - 内建函数可定制性不够
> - offsets返回值为字符串，多次realloc和字符串转换，效率太低
> - 一定情况下会更费内存
> - 过时，采用FTS5后未来需要升级数据库

我们使用微信的搜索功能时，可以发现它的搜索功能是很强大的，速度很快，并且搜索结果准确度很高：

>微信搜索结果图片

微信团队开源了微信数据库[WCDB](https://github.com/Tencent/wcdb)

下面是对WCDB的搜索测试结果

>WCDB搜索测试结果

从结果可以看到，WCDB的搜索结果还是比较理想的。如果需要快速实现搜索功能，集成WCDB是一个较好的选择。

同时，WCDB还提供了数据加密，备份，修复，多线程访问等功能。所以，如果比较业务重本地数据，可以直接使用。



## **参考**

[全文搜索使用教程](https://github.com/Tencent/wcdb/wiki/全文搜索使用教程)

[微信全文搜索优化之路](https://mp.weixin.qq.com/s/AhYECT3HVyn1ikB0YQ-UVg)

[移动客户端多音字搜索](https://mp.weixin.qq.com/s/GCznwCtjJ2XUszyMcbNz8Q)

[iOS/Android SQLite 全文检索——FTS (Full Text Search)](https://blog.csdn.net/andanlan/article/details/54237493)

[SQLite-fts3](http://www.sqlite.org/fts3.html)

[SQlite源码分析](http://huili.github.io/index.html)

[trinea](https://www.trinea.cn/)

[SQLite数据库中索引的使用、索引的优缺点](https://blog.csdn.net/zbc415766331/article/details/55053224)

[SQLite 之FTS5全文检索](https://blog.csdn.net/xiaogangwang2012/article/details/82979642)

[SQLite中使用全文搜索FTS](http://blog.sina.com.cn/s/blog_17fbcd7af0102y1e4.html)

[探讨Android全文检索技术](http://www.apkbus.com/blog-942559-78433.html)

---

[Lucence学习-初步使用](https://blog.csdn.net/chenzuyibao/article/details/80627894)

[Lucene系列 - 索引(七) - 对数据库记录建立索引](https://blog.csdn.net/u011389474/article/details/69420731)

[全文检索原理及实现方式](https://blog.csdn.net/qq_16162981/article/details/70142166)

[android+lucene实现全文检索并高亮关键字](https://blog.csdn.net/dengwenwei121/article/details/46823375)

[lucene的android之坑](https://blog.csdn.net/sinat_21423503/article/details/78864502)

[浅谈Lucene全文检索系统](https://blog.csdn.net/qq_41181619/article/details/81412684)

[全文检索](https://blog.csdn.net/carolzhang8406/article/details/81660244)

[全文检索技术与Lucene的使用](全文检索技术与Lucene的使用)

LIKE -- MATCH(FTS) -- WCDB(DB) -- ENCRYPT -- PDF/TXT -- Lucence -- 综合搜索/网络搜索
