# 从html转为docx文件

# 使用包 e-iceblue

下载
`https://www.e-iceblue.com/downloads/java/Spire.Doc_5.4.0.zip`

放到特定目录下，执行mvn命令安装到本地repo中
`mvn install:install-file -Dfile=E:/workspace/libs/Spire.Doc.jar -DgroupId=e-iceblue -DartifactId=spire.doc -Dversion=5.4.0 -Dpackaging=Jar -DgeneratePom=true`

在pom中引入

```xml

<dependency>
    <groupId>e-iceblue</groupId>
    <artifactId>spire.doc</artifactId>
    <version>5.4.0</version>
</dependency>

```

# db配置说明

使用时，将application-db-example.yml 改名为 application-db.yml 修改数据库配置