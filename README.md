lucene_learn
============

lucene学习

day_01:
索引创建的步骤:


搜索的步骤：


day_02:
索引创建的一些特性
Field.Store.*  [存储域选项]
YES,表示会把这个域中的内容完全存储在索引文件中，方便进行文本的还原
NO,表示这个域的内容不存储在索引文件中，但是可以被索引，此时内容无法进行还原(doc.get方法)
Field.Index.*  [索引域选项]
ANALYZED,进行分词和索引，使用于标题、内容等
NOT_ANALYZED,进行索引，但是不进行分词，如身份证号、姓名、ID等，适用精准搜索
ANALYZED_NOT_NORMS,进行分词但是不存储norms信息，这个norms中包含了创建索引的时间和权值等信息
NOT_ANALYZED_NOT_NORMS,既不分词也不索引
No,完全不索引，不存储

```java

doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

```

