package com.drybro.userinfo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drybro.userinfo.model.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {

	Optional<UserInfo> findUserInfoByEmail(String email);

}
