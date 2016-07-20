package ru.finnetrolle.telebot.service.processing.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.telebot.model.Pilot
import ru.finnetrolle.telebot.service.internal.UserService
import ru.finnetrolle.telebot.service.processing.MessageBuilder
import ru.finnetrolle.telebot.service.processing.engine.CommandExecutor
import ru.finnetrolle.telebot.service.telegram.TelegramBotService
import ru.finnetrolle.telebot.util.MessageLocalization

/**
 * Telegram bot
 * Licence: MIT
 * Author: Finne Trolle
 */

@Component
class GroupBroadcastCommand: CommandExecutor {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var telegram: TelegramBotService

    @Autowired
    private lateinit var loc: MessageLocalization

    override fun name() = "/GC"

    override fun secured() = true

    override fun description() = "telebot.command.description.gc"

    override fun execute(pilot: Pilot, data: String): String {
        val text = data.substringAfter(" ")
        val users = userService.getLegalUsers(data.substringBefore(" "))
        telegram.broadcast(users.map { u -> MessageBuilder.build(u.id.toString(), text) }.toList())
        return loc.getMessage("messages.broadcast.result", users.size)
    }
}