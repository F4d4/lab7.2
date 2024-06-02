package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;

import java.sql.SQLException;

/**
 * Интерфейс для всех комманд
 */
public interface Executable {
    Response apply(String[] arguments, Ticket ticket, String login, String password) throws SQLException;
}
