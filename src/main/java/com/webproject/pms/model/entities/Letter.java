package com.webproject.pms.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "letter")
public class Letter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long letterId;
	
	private Integer typeQuestion;
	
	@NotEmpty
	@Size(min = 2, max = 2048)
	private String description;
	
	private String date;
	
	private Boolean isProcessed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	public Letter() {
	}
	
	public Letter(Integer typeQuestion, @NotEmpty @Size(min = 2, max = 2048) String description, String date) {
		this.typeQuestion = typeQuestion;
		this.description = description;
		this.date = date;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Letter)) return false;
		Letter letter = (Letter) o;
		return letterId.equals(letter.letterId) &&
				typeQuestion.equals(letter.typeQuestion) &&
				description.equals(letter.description) &&
				date.equals(letter.date) &&
				isProcessed.equals(letter.isProcessed);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(letterId, typeQuestion, description, date, isProcessed);
	}
	
	public Long getLetterId() {
		return letterId;
	}
	
	public void setLetterId(Long letterId) {
		this.letterId = letterId;
	}
	
	public Integer getTypeQuestion() {
		return typeQuestion;
	}
	
	public void setTypeQuestion(Integer typeQuestion) {
		this.typeQuestion = typeQuestion;
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
	
	public Boolean getProcessed() {
		return isProcessed;
	}
	
	public void setProcessed(Boolean processed) {
		isProcessed = processed;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
