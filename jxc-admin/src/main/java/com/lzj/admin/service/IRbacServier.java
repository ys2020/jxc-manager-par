package com.lzj.admin.service;

import java.util.List;

public interface IRbacServier {
    List<String> findroleByUserName(String userName);

    List<String> findAuthoritiesByroleName(List<String> roleName);
}
