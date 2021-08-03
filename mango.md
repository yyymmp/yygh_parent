

##### 安装

```dockerfile
docker pull mongo
```

```dockerfile
docker run -p 27017:27017 --name mongo \
-v /mydata/mongo/db:/data/db \
-d mongo:4.2.5 --auth
```

进入客户端:

```dockerfile
docker exec -it mongo mongo
```



##### 对应概念:

| SQL概念     | MongoDB概念 | 解释/说明                           |
| ----------- | ----------- | ----------------------------------- |
| database    | database    | 数据库                              |
| table       | collection  | 数据库表/集合                       |
| row         | document    | 数据记录行/文档                     |
| column      | field       | 数据字段/域                         |
| index       | index       | 索引                                |
| primary key | primary key | 主键,MongoDB自动将_id字段设置为主键 |

##### 基本操作:

- 对database的操作:

  创建与删除:

  ```sql
  use article  //存在即选中  不存在则创建 
  db.dropDatabase()  /删除当前数据库
  ```
我们刚创建的数据库并不在数据库的列表中，要显示它，我们需要向该数据库插入一些数据,使用show dbs才可以显示

- 对table/collection的操作

  创建集合,删除集合

  ```sql
  db.createCollection("article")
  
  db.article.drop()
  ```

- 对row/document(数据行的操作 

  增: 对于任意新增的field(name,title),都会在数据库新增该field列

  ```sql
  db.article.insert({name:"j",title:"jj"})
  ```

  - 更新:

  ```
  db.collection.update(
     <query>,
     <update>,
     {
       multi: <boolean>
     }
  )
  # query：修改的查询条件，类似于SQL中的WHERE部分
  # update：更新属性的操作符，类似与SQL中的SET部分
  # multi：设置为true时会更新所有符合条件的文档，默认为false只更新找到的第一条
  ```

  ```sql
  db.article.update({name:"j"},{$set:{"name":"l"}},{multi:false})
  ```

  也可以使用save保存替换掉原文档	

  - 删除

  ```sql
  db.collection.remove(
     <query>,
     {
       justOne: <boolean>
     }
  )
  # query：删除的查询条件，类似于SQL中的WHERE部分
  # justOne：设置为true只删除一条记录，默认为false删除所有记录
  ```

  ```sql
  db.article.remove({name:"l"},{justOne:true})
  ```

  - 查询

    查询全部

    ```sql
    db.article.find()
    ```

    and查询,传入多个键,逗号隔开即可

    ```sql
    db.article.find({name:"j",by:"jlz"})
    ```

    or查询:  使用`$or`操作符实现，例如查询title为`java教程`或`MongoDB 教程`的所有文档；

    ```sql
     db.article.find({$or:[{"title":"java"},{"title": "MongoDB 教程"}]})
    ```

    and与or连用: 查询titile是java 并且   by是jlz或者by是MongoDB的文档

    ```sql
    db.article.find({"title": "java", $or:[{"by": "jlz"},{"by": "MongoDB"}]})
    ```

    skip与limit: 限制一条 跳过三条记录开始取,取limit条

    ```
    db.article.find().limit(1).skip(3)
    ```

    

##### 索引

<u>索引通常能够极大的提高查询的效率</u>，如果没有索引，MongoDB在读取数据时必须扫描集合中的每个文件并选取那些符合查询条件的记录

创建索引:

```sql
db.collection.createIndex(keys, options)
# background：建索引过程会阻塞其它数据库操作，设置为true表示后台创建，默认为false
# unique：设置为true表示创建唯一索引
# name：指定索引名称，如果没有指定会自动生成
```

给tile和description创建索引,1表示升序索引，-1表示降序索引,指定以后台方式创建

```sql
db.article.createIndex({"title":1,"description":-1}, {background: true})
```

查看索引:

```sql
db.article.getIndexes()
```









