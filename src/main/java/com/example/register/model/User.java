package com.example.register.model;

import com.example.register.model.converter.RoleConventer;
import com.example.register.model.validator.ValidName;
import com.example.register.model.validator.ValidPassword;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NamedNativeQuery(
name = "User.countCorrectAnswers",
query = "SELECT Count(correct_answer) "
	 + " FROM users LEFT JOIN  users_answers on users.user_id = users_answers.user_id"
	 + " LEFT JOIN questions on users_answers.question_id = questions.question_id"
	 + " WHERE username = :username AND answer = correct_answer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;
	
	@NotBlank
	@ValidName
	@Column(unique = true, nullable = false)
	private String username;
	
	@ValidPassword
	@Column(nullable = false)
	private String password;
	
	@NotBlank
	@Email
	@Column(unique = true, nullable = false)
	private String email;
	
	@ValidName
	private String firstName;
	
	@ValidName
	private String lastName;	

	private LocalDateTime lastActive;

	@Column(name = "pic_url")
	private String picUrl;
	
	@Convert(converter = RoleConventer.class)
	@Column(length = 4, nullable = false)
	private Role role;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "users_answers", joinColumns = @JoinColumn(name = "user_id"))
	@MapKeyJoinColumn(name = "question_id")
	@Column(name = "answer")
	private Map<Question, String> answers = new HashMap<>();

	
	public void storeAnsweredQuestion(Question question, String answer) {
		answers.put(question, answer);
	}

	@Override
	public String toString() {
		return username;
	}
	
	@Override
	public boolean equals(Object other) {
		if ( ! (other instanceof User))
			return false;
		User otherU = (User) other;
		return this.username.equals(otherU.username);
	}
	
	@Override
	public int hashCode() {
		return this.username.hashCode();
	}
}
