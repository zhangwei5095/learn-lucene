lucene_learn
============

lucene学习

day_01:
索引创建的步骤:

1. 创建directory
2. 创建IndexWriter
3. 创建Document
4. 为Document添加Field
5. 通过IdexUriter添加文档到索引中

搜索的步骤：

1. 创建directory
2. 创建IndexReader
3. 根据IndexReader创建IndexSearcher
4. 创建Query
5. 根据searcher搜索并返回对象TopDocs
6. 根据TopDocs获取ScoreDoc
7. 根据searcher和ScoreDoc获取具体的document
8. 根据document获取需要的值


day_02:
索引创建的一些特性

- Field.Store.*  [存储域选项]
- YES,表示会把这个域中的内容完全存储在索引文件中，方便进行文本的还原
- NO,表示这个域的内容不存储在索引文件中，但是可以被索引，此时内容无法进行还原(doc.get方法)

Field.Index.*  [索引域选项]

- ANALYZED,进行分词和索引，使用于标题、内容等
- NOT_ANALYZED,进行索引，但是不进行分词，如身份证号、姓名、ID等，适用精准搜索
- ANALYZED_NOT_NORMS,进行分词但是不存储norms信息，这个norms中包含了创建索引的时间和权值等信息
- NOT_ANALYZED_NOT_NORMS,既不分词也不索引
- NO,完全不索引，不存储

```java

doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

```

day_03:
索引的删除、更新、优化

- 索引的删除

```java
writer.deleteDocuments(new Term("id", "1"));
```

- 恢复删除

```java
reader.undeleteAll();
```

- 删除回收站

```java
writer.forceMergeDeletes();
```

- 优化合并

```java
writer.forceMerge(2);
```

> 会将索引强制合并为两段，这两段被删除的数据会被清空，此处lunene在3.5之后不建议使用，因为会消耗大量的内存开销，lunene会自动处理

- 更新

```java
writer.updateDocument(new Term("id", "1"), doc);
```

> lunene其实并没有提供更新，在这里更新操作其实是如下两个操作:先删除，再添加



