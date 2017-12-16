package com.icanpay.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icanpay.PaymentSetting;
import com.icanpay.enums.GatewayTradeType;
import com.icanpay.enums.GatewayType;
import com.icanpay.gateways.GatewayBase;
import com.icanpay.gateways.Gateways;
import com.unionpay.acp.sdk.SDKConfig;

@RestController
@RequestMapping("/apppayment")
public class AppPaymentController {

	@Autowired
	Gateways gateways;

	public AppPaymentController(Gateways gateways) {
		this.gateways = gateways;
	}

	@GetMapping("/createorder")
	public Map<String, String> createOrder(Integer type) throws IOException,
			Exception {
		GatewayType gatewayType = GatewayType.Alipay;
		if (type == 0) {
			gatewayType = GatewayType.Alipay;
		}
		if (type == 1) {
			gatewayType = GatewayType.WeChatPay;
		}
		if (type == 2) {
			SDKConfig.getConfig().loadPropertiesFromSrc();
			gatewayType = GatewayType.UnionPay;
		}

		GatewayBase gateway = gateways.get(gatewayType, GatewayTradeType.APP);
		PaymentSetting paymentSetting = new PaymentSetting(gateway);
		paymentSetting.getOrder().setOrderAmount(0.01);
		paymentSetting.getOrder().setOrderNo(
				new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		paymentSetting.getOrder().setPaymentDate(new Date());
		paymentSetting.getOrder().setSubject("apppay");
		return paymentSetting.payment(null);
	}
}
