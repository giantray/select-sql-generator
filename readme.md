#select-sql-generator
A smart tool help you to generate **select** sql statement for Java programmer. Hope you will enjoy it.

This tool can generate **select** sql by some simple annotation on your query model.In  the past,generate a select sql will be hard when the query conditions are not fixed and composite.

For example,assume that you system should search in the database table,and the search condition are multiple which rest with system user input:

You will search in the table call student,and system user can search by composite condition,such as student name,score,birth,class and so on.In the past,you hava to traverse all condition and tell if it is not null which mean user has select it as one of the condition, finaly you will get ugly code ,the code has so many "xxx!=null then sql+=xxx"statement.

Here is a more good choice for you to do such thing,it is base on annotation.You should add annotation on the query condition model.Then the tool will generate the sql for you.

##Usage
###1. Let us look at the example code first.
This is a query model,it means that system user can search data by some condition like name,sexuality……
```
//model
@SqlTable("example")
public class ExampleQuery {
	@SqlOper(Oper.LIKE)
	private String name;
	@SqlOper(Oper.EQUAL)
	private String sexuality;
	@SqlOper(Oper.GREATER)
	private Integer score;
	@SqlOper(Oper.LESSEQUAL)
	private Date createAt;

	@SqlLimit(Limit.START)
	private int start;
	@SqlLimit(Limit.SIZE)
	private int size;
	@SqlOrderType()
	private Order order;
	@SqlOrderCol()
	private String orderCol;
	//leave out import and set\get statement
}

//get sql 
ExampleQuery query = new ExampleQuery();
query.setSexuality("male");
SelectSql sql = SqlGenerator.getSelectWhere(query);
System.out.println(sql.getSelectAll());

```
###2. annotation instruction
- @SqlTable("example")

	Add on the class name.it's value should be the corresponding database table name.@SqlTable("example") means the sql will be "select xx from **example**"

- @SqlOper(xxx)

	Add on the field.It's value could be Oper.EQUAL,Oper.LIKE,Oper.GREATER,GREATEREQUAL.EQUAL,Oper.LESS,Oper.LESSEQUAL.
```
	@SqlOper(Oper.EQUAL)
	private String sexuality;
```
above example means that if the field value(sexuality) is not null(in other words,if the field value is null,it will not be add to the sql),the sql will be "select * from xxx where sexuality = 'abc'(abc is the field value you set)

- @SqlLimit

	Add on the field. `@SqlLimit(Limit.START)` , `@SqlLimit(Limit.SIZE)` corresponding to sql "limt start,size"

- @SqlOrderType()

	Add on the field.it determine the order is desc or asc. value could be Order.DESC,Order.ASC

- @SqlOrderCol()

	Add on the field.if value is not null.the sql will has "order by xxx",xxx is the field value here.

###3.get the sql.
```
		ExampleQuery query = new ExampleQuery();
		query.setName("li");
		query.setCreateAt(new Date(System.currentTimeMillis()));
		query.setOrder(Order.DESC);
		query.setOrderCol("name");
		query.setScore(60);
		query.setSexuality("male ' or 1=1");
		query.setSize(0);
		query.setStart(5);

		SelectSql sql = SqlGenerator.getSelectWhere(query);
		System.out.println(sql.getSelectAll());
		System.out.println(sql.getSelectCount());
		System.out.println(sql.getSelectAllForPreparedstatement());
		System.out.println(sql.getSelectCountForPreparedstatement());

....
		Connection connection = JDBCTools.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql.getSelectAllForPreparedstatement());
		SqlGenerator.setPreparedstatementPara(preparedStatement, sql.getWhereParaListForPreparedstatement());

```

`SqlGenerator.getSelectWhere(query)` help you gen the sql eaily,say hello to annotation and say goodbye to ugly assemble code which is not in common use

finally,get the sql with two way:
1. `sql.getSelectAll()` get the sql with para.
2. `sql.getSelectAllForPreparedstatement()`,`SqlGenerator.setPreparedstatementPara`help you get sql by preparedStatement.

##Download
Add this to your project's pom.xml
```xml
	<dependency>
		  <groupId>com.github.giantray</groupId>
		  <artifactId>select-sql-generator</artifactId>
		  <version>1.0</version>
	</dependency>
```

You get get this jar by [Sonatype's snapshots](https://oss.sonatype.org) repository 


##License
Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

 [snap]: https://oss.sonatype.org/content/repositories/snapshots/