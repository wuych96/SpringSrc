package com.wuych.test;

import lombok.Data;

/**
 * @Author Wu Yanchen
 * @Date 2020-05-11 16:10
 */
@Data
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Integer integer;
}
