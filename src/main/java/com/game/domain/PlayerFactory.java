package com.game.domain;

import com.game.domain.value.PlayerEnumType;

import java.util.EnumMap;

/**
 * @author VIRIYA
 * @create 2020/10/24 7:27
 */
public class PlayerFactory {


    private static EnumMap<PlayerEnumType, AbstractPlayer> players;

    /**
     * When all the keys of a Map are values from the same enum,
     * the Map can be replaced with an EnumMap,
     * which can be much more efficient than other sets
     * because the underlying data structure is a simple array.
     */
    static {
        players = new EnumMap<>(PlayerEnumType.class);
    }

    /**
     * Utility classes, which are collections of static members,
     * are not meant to be instantiated.
     * Even abstract utility classes, which can be extended,
     * should not have public constructors.
     * Java adds an implicit public constructor to every class
     * which does not define at least one explicitly.
     * Hence, at least one non-public constructor should be defined.
     */
    private PlayerFactory() {

    }

    public static AbstractPlayer createAiPlayer() {
        if (players.containsKey(PlayerEnumType.AI)) {
            return players.get(PlayerEnumType.AI);
        }
        AbstractPlayer aiChessPlayer = new AiChessPlayer();
        players.put(PlayerEnumType.AI,aiChessPlayer);
        return aiChessPlayer;
    }


    public static AbstractPlayer createRemotePlayer() {
        if (players.containsKey(PlayerEnumType.NET)) {
            return players.get(PlayerEnumType.NET);
        }
        AbstractPlayer aiChessPlayer = new RemoteChessPlayer();
        players.put(PlayerEnumType.NET,aiChessPlayer);
        return aiChessPlayer;
    }
}
