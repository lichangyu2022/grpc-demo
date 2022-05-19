package org.learn.service;

import org.learn.grpc.api.RPCDateRequest;
import org.learn.grpc.api.RPCDateResponse;
import org.learn.grpc.api.RPCDateResponseList;
import org.learn.grpc.api.RPCDateServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author lichangyu
 * 点对点，点对流 流对点 流对流 接口实例
 */
public class RPCDateServiceImpl extends RPCDateServiceGrpc.RPCDateServiceImplBase {


    /**
     * 点对点
     * @param request
     * @param responseObserver
     */
    @Override
    public void getDate(RPCDateRequest request, StreamObserver<RPCDateResponse> responseObserver) {
        // 请求结果,我们定义的
        RPCDateResponse rpcDateResponse = null;
        String userName = request.getUserName();
        String response = String.format("你好: %s, 今天是%s.", userName, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        try {
            // 定义响应,是一个builder构造器.
            rpcDateResponse = RPCDateResponse
                    .newBuilder()
                    .setServerDate(response)
                    .build();
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            // 这种写法是observer,异步写法,老外喜欢用这个框架.
            responseObserver.onNext(rpcDateResponse);
        }
        responseObserver.onCompleted();
    }

    /**
     * 点对流
     * @param request
     * @param responseObserver
     */
    @Override
    public void getDateStream(RPCDateRequest request, StreamObserver<RPCDateResponse> responseObserver) {


        // 请求结果,我们定义的
        RPCDateResponse rpcDateResponse = null;
        String userName = request.getUserName();

        for (int i = 0; i < 100; i++){
            String response = String.format("你好: %s, 这是第%s条数据", userName, i);
            System.out.println(response);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                // 定义响应,是一个builder构造器.
                rpcDateResponse = RPCDateResponse
                        .newBuilder()
                        .setServerDate(response)
                        .build();
            } catch (Exception e) {
                responseObserver.onError(e);
            } finally {
                // 这种写法是observer,异步写法,老外喜欢用这个框架.
                responseObserver.onNext(rpcDateResponse);
            }
        }
        responseObserver.onCompleted();

    }

    /**
     * 流对点
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<RPCDateRequest> stream2Data(StreamObserver<RPCDateResponseList> responseObserver) {

        return new StreamObserver<RPCDateRequest>() {
            @Override
            public void onNext(RPCDateRequest rpcDateRequest) {
                System.out.println("onNext:" + rpcDateRequest.getUserName());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                RPCDateResponseList.Builder builder = RPCDateResponseList.newBuilder();
                for(int i = 0; i < 10; i++){
                    RPCDateResponse response = RPCDateResponse
                            .newBuilder()
                            .setServerDate("stream2Data:"+i)
                            .build();

                    builder.addRPCDateResponse(response);
                }

                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 流对流
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<RPCDateRequest> doubleStream(StreamObserver<RPCDateResponse> responseObserver) {
        return new StreamObserver<RPCDateRequest>() {
            @Override
            public void onNext(RPCDateRequest rpcDateRequest) {
                System.out.println("onNext:" + rpcDateRequest.getUserName());
                RPCDateResponse response = RPCDateResponse
                        .newBuilder()
                        .setServerDate("我接到的name是:"+rpcDateRequest.getUserName())
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError" + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }




}
