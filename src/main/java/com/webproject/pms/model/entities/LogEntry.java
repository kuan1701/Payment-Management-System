package com.webproject.pms.model.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "log_entry")
public class LogEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logEntryId;
	
	private String description;
	
	private String date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	public LogEntry() {
	}
	
	public LogEntry(String description, String date) {
		this.description = description;
		this.date = date;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LogEntry)) return false;
		LogEntry logEntry = (LogEntry) o;
		return logEntryId.equals(logEntry.logEntryId) &&
				description.equals(logEntry.description) &&
				date.equals(logEntry.date);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(logEntryId, description, date);
	}
	
	public Long getLogEntryId() {
		return logEntryId;
	}
	
	public void setLogEntryId(Long logEntryId) {
		this.logEntryId = logEntryId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
