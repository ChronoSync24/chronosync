package com.sinergy.chronosync.model;

import com.sinergy.chronosync.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Base entity abstract class.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_by", updatable = false)
	private User createdBy;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "modified_by")
	private User modifiedBy;

	@Column(name = "modified_at")
	private LocalDateTime modified;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime created;

	@PreUpdate
	protected void onUpdate() {
		this.modified = LocalDateTime.now(ZoneOffset.UTC);
	}
}
