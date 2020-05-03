package com.dosth.consumer.rpc;

import org.springframework.cloud.openfeign.FeignClient;

import com.dosth.common.rpc.service.RpcService;
import com.dosth.consumer.rpc.impl.RpcConsumeServiceImpl;

@FeignClient(value = "consul-provider", fallback = RpcConsumeServiceImpl.class)
public interface RpcConsumeService extends RpcService {

}