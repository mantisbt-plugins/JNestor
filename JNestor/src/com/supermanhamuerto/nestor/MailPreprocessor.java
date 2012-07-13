package com.supermanhamuerto.nestor;


/**
 * <p>
 * This preprocessor converts one MailMessage into another</p>
 * @author rluna
 *
 */
public interface MailPreprocessor
{
	public MailMessage applyCustomRules( MailMessage mailMessage );
}
