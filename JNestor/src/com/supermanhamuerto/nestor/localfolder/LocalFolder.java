package com.supermanhamuerto.nestor.localfolder;

import com.supermanhamuerto.nestor.MailMessage;

public interface LocalFolder
{

	public boolean messageExist(MailMessage msg);

	public boolean messageExist(String id);

	public void saveMessage(MailMessage msg);

	public void deleteMessage( MailMessage msg );
}