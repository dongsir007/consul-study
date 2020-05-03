package com.dosth.consul.es.kibana.serializer;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 
 * @description joda时间反序列化
 *
 * @author guozhidong
 */
public class JodaDateTimeDeserializer extends JsonDeserializer<DateTime> {

	@Override
	public DateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
		String s = jsonParser.getValueAsString();
		if (s != null) {
			try {
				return formatter.parseDateTime(s);
			} catch (Exception e) {
				formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
				return formatter.parseDateTime(s);
			}
		}
		return null;
	}
}