package com.horyu1234.kkutugame.dao;

import com.horyu1234.kkutugame.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by horyu on 2017-04-13.
 */
@Repository
public class LoginTypeDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginTypeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LoginType> getLoginTypes() {
        return jdbcTemplate.query("SELECT * FROM `login_types`",
                new BeanPropertyRowMapper<>(LoginType.class));
    }
}
