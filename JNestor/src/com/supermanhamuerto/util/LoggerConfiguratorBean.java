package com.supermanhamuerto.util;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class LoggerConfiguratorBean
{

	public void setConfiguration( Properties configuration )
	{
		PropertyConfigurator.configure( configuration ); 
	}
}
