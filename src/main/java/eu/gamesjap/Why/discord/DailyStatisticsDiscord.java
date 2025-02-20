package eu.gamesjap.Why.discord;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.spicord.api.addon.SimpleAddon;
import org.spicord.bot.DiscordBot;
import org.spicord.bot.command.DiscordBotCommand;
import org.spicord.embed.Embed;

import eu.gamesjap.Why.DailyStatistics;
import net.dv8tion.jda.api.entities.TextChannel;

public class DailyStatisticsDiscord extends SimpleAddon {

    private DiscordBot bot;
    private DailyStatistics ds;

    public DailyStatisticsDiscord(DailyStatistics ds) {
        super("DailyStatistics", "daily_statistics", "Zoiter7");
        this.ds = ds;
    }

    @Override
    public void onLoad(DiscordBot bot) {
        this.bot = bot;

        bot.onCommand("stat", this::statsCmd);
    }

    private void statsCmd(DiscordBotCommand command) {

        String[] arguments = command.getArguments();

        if (arguments.length > 0) {
            if (!arguments[0].isEmpty() && arguments[0].contentEquals("actual")) {
                ds.prepareDiscordMessage(ds.getActualDate(), true,
                        "actual");

            } else if (!arguments[0].isEmpty()) {
                DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    dateFormatter.setLenient(false);
                    dateFormatter.parse(arguments[0]);
                } catch (ParseException e) {
                    this.executeMsg(null,
                            ds.getConfigManager().getConfig().getString("dMessage-incorrectDataFormat"));
                    return;
                }
                String date = arguments[0].toString();

                if (date != null) {
                    ds.prepareDiscordMessage(date, true, "stat");

                }
            }

        } else {
            executeMsg(null, ">>> **Commands:** \n" + " -stat <date>\n" + " -stat actual");
        }

    }

    public void executeMsg(Embed msg, String normalMsg) {

        TextChannel channel = bot.getJda()
                .getTextChannelById(ds.getConfigManager().getConfig().getString("discord-channelID"));

        if (normalMsg != null) {
            channel.sendMessage(normalMsg).queue();
        } else {
            msg.sendToChannel(channel);
        }
    }

}