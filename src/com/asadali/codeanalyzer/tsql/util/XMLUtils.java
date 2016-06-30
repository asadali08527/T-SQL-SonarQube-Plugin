package com.asadali.codeanalyzer.tsql.util;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


public class XMLUtils {
	
	//private static Logger theLogger =Logger.getLogger(JAXBUtils.class.getName());
	
	public static <T>T unmarshall(InputStream fileStream, Class<?>... clazz) throws Exception {
	
		JAXBContext context = null;
		context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(fileStream);
		
	}

}