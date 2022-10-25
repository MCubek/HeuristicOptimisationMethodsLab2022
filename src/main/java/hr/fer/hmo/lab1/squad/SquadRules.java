package hr.fer.hmo.lab1.squad;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.player.PlayerPosition;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public class SquadRules {
    private SquadRules() {
    }

    private static final int MAX_BUDGET = 100;

    public static final ISquadRule budgetRule = (activePlayers, reservePlayers) ->
            Stream.concat(activePlayers.stream(), reservePlayers.stream())
                    .mapToDouble(Player::getPrice)
                    .sum() < MAX_BUDGET;

    public static final ISquadRule squadFormationRule = (activePlayers, reservePlayers) -> {
        var rolesMap = Stream.concat(activePlayers.stream(), reservePlayers.stream())
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));

        return rolesMap.entrySet()
                .stream().map(entry -> {
                            var count = entry.getValue();
                            return switch (entry.getKey()) {
                                case GOAL_KEEPER -> count == 2;
                                case DEFENDER, MIDFIELDER -> count == 5;
                                case FORWARD -> count == 3;
                            };
                        }
                )
                .reduce(Boolean::logicalAnd)
                .orElse(false);

    };

    public static final ISquadRule singleClubRule = (activePlayers, reservePlayers) ->
            Stream.concat(activePlayers.stream(), reservePlayers.stream())
                    .collect(Collectors.groupingBy(Player::getClub, Collectors.counting()))
                    .values()
                    .stream()
                    .map(aLong -> aLong <= 3)
                    .reduce(Boolean::logicalAnd).orElse(false);

    public static final ISquadRule singleTeamSizeRule = (activePlayers, reservePlayers) ->
            activePlayers.size() == 11 && reservePlayers.size() == 4;

    public static final ISquadRule firstTeamFormationRule = (activePlayers, reservePlayers) -> {
        var rolesMap = activePlayers.stream()
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));

        return rolesMap.entrySet()
                .stream().map(entry -> {
                            var count = entry.getValue();
                            return switch (entry.getKey()) {
                                case GOAL_KEEPER -> count == 1;
                                case DEFENDER -> count >= 3;
                                case MIDFIELDER -> true;
                                case FORWARD -> count > 1;
                            };
                        }
                )
                .reduce(Boolean::logicalAnd)
                .orElse(false);
    };

    public static final ISquadRule secondTeamFormationRule = (activePlayers, reservePlayers) -> {
        var rolesMap = reservePlayers.stream()
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));

        return rolesMap.entrySet()
                .stream().map(entry -> {
                            var count = entry.getValue();
                            if (entry.getKey() == PlayerPosition.GOAL_KEEPER) {
                                return count == 1;
                            }
                            return true;
                        }
                )
                .reduce(Boolean::logicalAnd)
                .orElse(false);
    };

    public static final ISquadRule allRules = (activePlayers, reservePlayers) ->
            budgetRule.validate(activePlayers, reservePlayers) &&
            squadFormationRule.validate(activePlayers, reservePlayers) &&
            singleClubRule.validate(activePlayers, reservePlayers) &&
            singleTeamSizeRule.validate(activePlayers, reservePlayers) &&
            firstTeamFormationRule.validate(activePlayers, reservePlayers) &&
            secondTeamFormationRule.validate(activePlayers, reservePlayers);
}
