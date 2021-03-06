package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ArgumentScoreboardCriteria implements ArgumentType<IScoreboardCriteria> {

    private static final Collection<String> b = Arrays.asList(new String[] { "foo", "foo.bar.baz", "minecraft:foo"});
    public static final DynamicCommandExceptionType a = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.criteria.invalid", new Object[] { object});
    });

    private ArgumentScoreboardCriteria() {}

    public static ArgumentScoreboardCriteria a() {
        return new ArgumentScoreboardCriteria();
    }

    public static IScoreboardCriteria a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
        return (IScoreboardCriteria) commandcontext.getArgument(s, IScoreboardCriteria.class);
    }

    public IScoreboardCriteria a(StringReader stringreader) throws CommandSyntaxException {
        int i = stringreader.getCursor();

        while (stringreader.canRead() && stringreader.peek() != 32) {
            stringreader.skip();
        }

        String s = stringreader.getString().substring(i, stringreader.getCursor());
        IScoreboardCriteria iscoreboardcriteria = IScoreboardCriteria.a(s);

        if (iscoreboardcriteria == null) {
            stringreader.setCursor(i);
            throw ArgumentScoreboardCriteria.a.create(s);
        } else {
            return iscoreboardcriteria;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
        ArrayList arraylist = Lists.newArrayList(IScoreboardCriteria.criteria.keySet());
        Iterator iterator = StatisticList.REGISTRY.iterator();

        while (iterator.hasNext()) {
            StatisticWrapper statisticwrapper = (StatisticWrapper) iterator.next();
            Iterator iterator1 = statisticwrapper.a().iterator();

            while (iterator1.hasNext()) {
                Object object = iterator1.next();
                String s = this.a(statisticwrapper, object);

                arraylist.add(s);
            }
        }

        return ICompletionProvider.b(arraylist, suggestionsbuilder);
    }

    public <T> String a(StatisticWrapper<T> statisticwrapper, Object object) {
        return Statistic.a(statisticwrapper, object);
    }

    public Collection<String> getExamples() {
        return ArgumentScoreboardCriteria.b;
    }

    public Object parse(StringReader stringreader) throws CommandSyntaxException {
        return this.a(stringreader);
    }
}
