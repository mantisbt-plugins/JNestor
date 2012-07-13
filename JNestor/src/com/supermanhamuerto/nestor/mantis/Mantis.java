package com.supermanhamuerto.nestor.mantis;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.mantis.SoapMantis.RelationshipType;

public interface Mantis
{

	public String getCategory();

	public void setCategory(String category);

	public IssueDataFactory getIdf();

	public void setIdf(IssueDataFactory idf);

	public void init() throws JNException;

	public void openSession() throws JNException;

	public boolean issueNumberExists(long issueid) throws JNException; // issueNumberExists

	public IssueDataAttachment newIssueDataAttachment();

	public long addIssueAttachment(long issueId, IssueDataAttachment attachment)
	    throws JNException;

	public IssueDataDto newIssue();

	public long addIssue(IssueDataDto issue) throws JNException; // addIssue

	public NoteData newNote();

	public long addNote(long issue, NoteData noteData) throws JNException; // addNote

	public void addCustomField(long issue, String field, String value)
	    throws JNException;

	public long addRelationship(long origIssue, long destIssue,
	    RelationshipType relation) throws JNException; // addRelationship

}