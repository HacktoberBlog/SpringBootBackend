package com.hacktober.blog.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private String name;
    private String username;   // document id
    private String email;
    private String password;   // will be stored as Base64 encrypted
    private List<String> blogs;
}
