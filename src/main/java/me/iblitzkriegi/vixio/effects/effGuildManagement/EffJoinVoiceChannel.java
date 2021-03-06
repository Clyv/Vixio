package me.iblitzkriegi.vixio.effects.effGuildManagement;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.effects.EffLogin;
import me.iblitzkriegi.vixio.registration.EffectAnnotation;
import me.iblitzkriegi.vixio.util.AudioPlayerSendHandler;
import me.iblitzkriegi.vixio.util.GuildMusicManager;
import me.iblitzkriegi.vixio.util.TrackScheduler;
import me.iblitzkriegi.vixio.util.VixioAudioHandlers;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.bukkit.event.Event;

import java.util.Map;

import static me.iblitzkriegi.vixio.effects.EffLogin.bots;
import static me.iblitzkriegi.vixio.effects.EffLogin.trackSchedulers;
import static me.iblitzkriegi.vixio.util.VixioAudioHandlers.getGuildAudioPlayer;

/**
 * Created by Blitz on 12/17/2016.
 */
@EffectAnnotation.Effect(
        name = "JoinVoiceChannel",
        title = "Join Voice Channel",
        desc = "Join a Voice Channel",
        syntax = "[discord] join voice channel [with id] %string% with [bot] %string%",
        example = "SOONLOL"
)
public class EffJoinVoiceChannel extends Effect {
    private Expression<String> vID;
    private Expression<String> vBot;
    @Override
    protected void execute(Event e) {
        JDA jda = EffLogin.bots.get(vBot.getSingle(e));
        if(jda!=null){
            VoiceChannel vc = jda.getVoiceChannelById(vID.getSingle(e));
            Guild g = vc.getGuild();
            if(g!=null||vc!=null){
                g.getAudioManager().openAudioConnection(vc);
                if(trackSchedulers.get(jda.getSelfUser().getId())==null) {
                    GuildMusicManager musicManager = getGuildAudioPlayer(g);
                    TrackScheduler  scheduler = new TrackScheduler(musicManager.scheduler.getPlayer());
                    trackSchedulers.put(jda.getSelfUser().getId(), scheduler);
                    EffLogin.audioPlayers.put(scheduler.getPlayer(), jda.getSelfUser());
                    Vixio.reverseGuilds.put(musicManager.player, g);
                }
            }
        }

    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        vID = (Expression<String>) expr[0];
        vBot = (Expression<String>) expr[1];
        return true;
    }
}
