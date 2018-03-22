package com.demo.elasticsearch.pojo;

import java.util.Collection;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;

public class MyClient extends TransportClient {
	
	public MyClient(Settings settings,
			Collection<Class<? extends Plugin>> plugins) {
		super(settings, plugins);
	}
	
	
}
