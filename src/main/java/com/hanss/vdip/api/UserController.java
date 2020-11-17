package com.hanss.vdip.api;

import com.hanss.vdip.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "Сервис пользователей", description = "Работа с пользователями")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RepositoryRestResource(path = "users", collectionResourceRel = "users")
public interface UserController extends PagingAndSortingRepository<User, Integer> {
}
