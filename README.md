# Scalding Examples 

This is repository of code examples shown in my [blog
posts](http://morazow.com/archive.html) about
[Scalding](https://github.com/twitter/scalding).

## Running Locally
```
> sbt run
```

## Running on Hadoop
First create 'fat jar' then run on Hadoop environment.
```
> sbt assembly
```
It will create fat jar with name **ScaldingExamples-1.0.jar** under
**target/scala-2.10/ScaldingExamples-1.0.jar**.

Run on Hadoop cluster giving proper input/output paths,
```
> hadoop/yarn jar ScaldingExamples-1.0.jar JobClassName \
      --hdfs \ 
      --input /hdfs/input/path/to/file \
      --output /hdfs/output/path/
```
