package com.supermanhamuerto.nestor.mantis;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.MailMessage;

public interface MantisProcessorAction
{
	public void doAction( long issueId, MailMessage msg, boolean exists ) throws JNException;
}
