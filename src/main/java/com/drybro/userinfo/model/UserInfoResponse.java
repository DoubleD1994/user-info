package com.drybro.userinfo.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

	boolean isSuccess;
	UserInfo userInfo;
	Set<UserInfo> userInfoSet;
	List<String> errors;
	Object requestDetails;

}
