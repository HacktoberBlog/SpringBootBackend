package com.hacktober.blog.user.dto;

import java.util.List;

public record UserDto(String name, String username, String email, String password, List<String> blogs) {
}
