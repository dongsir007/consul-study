package com.dosth.consul.es.kibana.serializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * 
 * @description Instant时间格式化
 *
 * @author guozhidong
 */
@SuppressWarnings("serial")
public class InstantDateDeserializer extends StdDeserializer<Instant> {

	protected InstantDateDeserializer() {
		super(Instant.class);
	}

	private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS Z").withZone(ZoneOffset.UTC);
	
	@Override
	public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String s = p.getText();
		if (s != null) {
			return Instant.from(fmt.parse(s));
		}
		return null;
	}

}
