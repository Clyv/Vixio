package me.iblitzkriegi.vixio.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.bukkit.event.Event;

public class CondHasRole extends Condition {
    static {
        Vixio.getInstance().registerCondition(CondHasRole.class, "%member% has %role%")
                .setName("Member has role")
                .setDesc("Check if a member has a specific role")
                .setExample("if event-member has role with id \"216516161651\"");
    }

    private Expression<Member> member;
    private Expression<Role> role;

    @Override
    public boolean check(Event e) {
        Member member = this.member.getSingle(e);
        Role role = this.role.getSingle(e);
        if (role == null || member == null) {
            return false;
        }
        return member.getRoles().contains(role);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return member.toString(e, debug) + " has " + role.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        member = (Expression<Member>) exprs[0];
        role = (Expression<Role>) exprs[1];
        return true;
    }
}
