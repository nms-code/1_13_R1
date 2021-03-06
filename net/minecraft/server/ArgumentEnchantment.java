package net.minecraft.server;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ArgumentEnchantment implements ArgumentType<Enchantment> {

    private static final Collection<String> b = Arrays.asList(new String[] { "unbreaking", "silk_touch"});
    public static final DynamicCommandExceptionType a = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("enchantment.unknown", new Object[] { object});
    });

    public ArgumentEnchantment() {}

    public static ArgumentEnchantment a() {
        return new ArgumentEnchantment();
    }

    public static Enchantment a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
        return (Enchantment) commandcontext.getArgument(s, Enchantment.class);
    }

    public Enchantment a(StringReader stringreader) throws CommandSyntaxException {
        MinecraftKey minecraftkey = MinecraftKey.a(stringreader);
        Enchantment enchantment = (Enchantment) Enchantment.enchantments.get(minecraftkey);

        if (enchantment == null) {
            throw ArgumentEnchantment.a.create(minecraftkey);
        } else {
            return enchantment;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
        return ICompletionProvider.a((Iterable) Enchantment.enchantments.keySet(), suggestionsbuilder);
    }

    public Collection<String> getExamples() {
        return ArgumentEnchantment.b;
    }

    public Object parse(StringReader stringreader) throws CommandSyntaxException {
        return this.a(stringreader);
    }
}
