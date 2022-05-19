# 简介
对grpc的四种通信方式进行实现，GRPCClient和GRPCServer分别为客户端和服务端,
GRPCClient主要包括四个对服务端调用的接口，先后启动客户端和服务端可完成grpc调用


#接口 
| 方法名      | 说明  |
|----------|-----|
| getDate | 点对点 |
| getDateStream | 点对流 |
| stream2Data | 流对点 |
| doubleStream | 流对流 |


#依赖
```xml
<dependencies>
 <dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-all</artifactId>
    <version>1.12.0</version>
 </dependency>
</dependencies>
<build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.4.1.Final</version>
            </extension>
        </extensions>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.5.0</version>
                <configuration>
                    <pluginId>grpc-java</pluginId>
                    <protocArtifact>com.google.protobuf:protoc:3.0.2:exe:${os.detected.classifier}</protocArtifact>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.2.0:exe:${os.detected.classifier}</pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

# protobuf文件编写
**proto文件目录为src/main/proto**
```protobuf
syntax = "proto3"; // 协议版本

// 选项配置
option java_package = "com.chuangqi.grpc.api";
option java_outer_classname = "RPCDateServiceApi";
option java_multiple_files = true;

// 定义包名
package com.chuangqi.grpc.api;

// 服务接口.定义请求参数和相应结果
service RPCDateService {
  rpc getDate (RPCDateRequest) returns (RPCDateResponse) {}

  rpc getDateStream (RPCDateRequest) returns (stream RPCDateResponse){}

  rpc doubleStream (stream RPCDateRequest) returns (stream RPCDateResponse){}

  rpc stream2Data (stream RPCDateRequest) returns (stream RPCDateResponseList){}
  
}
// 定义请求体
message RPCDateRequest {
  string userName = 1;
}

// 定义相应内容
message RPCDateResponse {
  string serverDate = 1;
}
// 定义相应内容
message RPCDateResponseList {
  repeated RPCDateResponse rPCDateResponse = 1;
}
```

1. 安装idea/pluging选项下载protobuf插件
2. 使用maven/plugins/protobuf/compile编译
3. 将生成的target/generated-sources/protobuf/java中文件复制到对应目录下
4. 使用maven/plugins/protobuf/custom编译
5. 将生成的target/generated-sources/protobuf/grpc-java中文件复制到对应目录下

#编写实现类
**src/main/java/com/chuangqi/service/RPCDateServiceImpl.java**

#定义服务端
**src/main/java/com/chuangqi/GRPCServer.java**

#定义客户端
**src/main/java/com/chuangqi/GRPCClient.java**

