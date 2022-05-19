package org.learn;

import org.learn.grpc.api.RPCDateRequest;
import org.learn.grpc.api.RPCDateResponse;
import org.learn.grpc.api.RPCDateResponseList;
import org.learn.grpc.api.RPCDateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;

public class GRPCClient {

    private static final String host = "localhost";
    private static final int serverPort = 9999;

    public static void main(String[] args) throws Exception {



        //getDate();

        //getDateStream();
        //doubleStream();
        stream2Data();
    }

    private static void stream2Data() {
        // 1. 拿到一个通信的channel
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
        // 2.拿到道理对象
        //，客户端如果是流式请求的话，那么客户端必须使⽤异步的stub
        RPCDateServiceGrpc.RPCDateServiceStub rpcDateServiceStub = RPCDateServiceGrpc.newStub(managedChannel);
        StreamObserver<RPCDateRequest> result = rpcDateServiceStub.stream2Data(new StreamObserver<RPCDateResponseList>() {

            @Override
            public void onNext(RPCDateResponseList rpcDateResponseList) {
                rpcDateResponseList.getRPCDateResponseList().forEach(rpcDateResponse -> {
                    System.out.println(rpcDateResponse.getServerDate());
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });


        for (int i = 0; i < 10; i++){
            RPCDateRequest rpcDateRequest = RPCDateRequest
                    .newBuilder()
                    .setUserName("lichangyu"+i)
                    .build();
            result.onNext(rpcDateRequest);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        result.onCompleted();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        managedChannel.shutdown();


    }

    private static void doubleStream() {

        // 1. 拿到一个通信的channel
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
        // 2.拿到道理对象
        RPCDateServiceGrpc.RPCDateServiceStub rpcDateServiceStub = RPCDateServiceGrpc.newStub(managedChannel);
        StreamObserver<RPCDateRequest> result = rpcDateServiceStub.doubleStream(new StreamObserver<RPCDateResponse>() {
            @Override
            public void onNext(RPCDateResponse rpcDateResponse) {
                System.out.println("返回结果：" + rpcDateResponse.getServerDate());

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
            }
        });


        for(int i = 0; i < 100; i++){
            RPCDateRequest rpcDateRequest = RPCDateRequest
                    .newBuilder()
                    .setUserName("lichangyu"+i)
                    .build();
            result.onNext(rpcDateRequest);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        result.onCompleted();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        managedChannel.shutdown();



    }

    private static void getDateStream() {
        // 1. 拿到一个通信的channel
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
        try {
            // 2.拿到道理对象
            RPCDateServiceGrpc.RPCDateServiceBlockingStub rpcDateService = RPCDateServiceGrpc.newBlockingStub(managedChannel);
            RPCDateRequest rpcDateRequest = RPCDateRequest
                    .newBuilder()
                    .setUserName("lichangyu")
                    .build();
            // 3. 请求
            Iterator<RPCDateResponse> dateStream = rpcDateService.getDateStream(rpcDateRequest);
            // 4. 输出结果
            while (dateStream.hasNext()){
                System.out.println(dateStream.next().getServerDate());
            }

        } finally {
            // 5.关闭channel, 释放资源.
            managedChannel.shutdown();
        }
    }

    private static void getDate() {
        // 1. 拿到一个通信的channel
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext().build();
        try {
            // 2.拿到道理对象
            RPCDateServiceGrpc.RPCDateServiceBlockingStub rpcDateService = RPCDateServiceGrpc.newBlockingStub(managedChannel);
            RPCDateRequest rpcDateRequest = RPCDateRequest
                    .newBuilder()
                    .setUserName("lichangyu")
                    .build();
            // 3. 请求
            RPCDateResponse rpcDateResponse = rpcDateService.getDate(rpcDateRequest);
            // 4. 输出结果
            System.out.println(rpcDateResponse.getServerDate());
        } finally {
            // 5.关闭channel, 释放资源.
            managedChannel.shutdown();
        }
    }

}
