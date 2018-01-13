package me.iblitzkriegi.vixio.condition;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import net.dv8tion.jda.core.entities.Member;
import org.bukkit.event.Event;

public class CondMemberIsDeaf extends Condition {
    static {
        Vixio.getInstance().registerCondition(CondMemberIsDeaf.class, "%member% is deafened", "%member% is[n[']t] [not] deafened")
                .setName("Member is deafened")
                .setDesc("Check if a member is currently deafened by their own will, meaning they deafened themself.")
                .setExample(
                        "command /checkDeaf <text> <text>:",
                        "\ttrigger:",
                        "\t\tset {member} to member of user with id arg-1 in guild with id arg-2",
                        "\t\tif {member} is set:",
                        "\t\t\tbroadcast \"%{member} is deafened%\""
                );
    }
    private Expression<Member> member;
    private boolean not;
    @Override
    public boolean check(Event e) {
        Member member = this.member.getSingle(e);
        if (member == null) {
            return false;
        }
        return not == member.getVoiceState().isDeafened();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return member.toString(e, debug) + (not ? " is not" : " is ") + "deafened";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        member = (Expression<Member>) exprs[0];
        not = matchedPattern == 0;
        return true;
    }
}