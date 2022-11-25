package hr.fer.hmo.algorithm;

import hr.fer.hmo.player.Player;
import hr.fer.hmo.player.PlayerPosition;
import hr.fer.hmo.squad.Squad;
import hr.fer.hmo.squad.SquadRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 26.10.2022.
 */

public class GreedyConstructionAlgorithm implements ISearchAlgorithm {
    private final double alpha;
    private final Random random;

    private static final double DOUBLE_ERROR = 1E-5;

    public GreedyConstructionAlgorithm(double alpha, double beta, Random random) {
        this.alpha = 1 - alpha;
        this.random = random;

        this.playerValueFunction = p -> Math.pow(p.getPoints(), beta) / p.getPrice();
    }

    private final Function<Player, Double> playerValueFunction;
    private final Function<Player, Double> playerInversePriceFunction = p -> 1 / p.getPrice();

    private final BiPredicate<Squad, Player> playerValidInSquadRule = (s, p) -> ! s.isPlayerInSquad(p)
                                                                                && s.clubCounts().getOrDefault(p.getClub(), 0L) <= 2;

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {

        Squad generatedSquad;
        do {
            generatedSquad = new Squad(11, 4, players);
            // MANDATORY PLAYERS

            // 1 Goalkeeper for active team
            generatedSquad.addActivePlayer(pickPlayerFromRCL(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.GOAL_KEEPER,
                    playerValueFunction));
            // 1 Goalkeeper for reserve team
            generatedSquad.addReservePlayer(pickPlayerFromRCL(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.GOAL_KEEPER,
                    playerInversePriceFunction));

            // 3 Defenders
            for (int i = 0; i < 3; i++) {
                generatedSquad.addActivePlayer(pickPlayerFromRCL(players,
                        generatedSquad,
                        p -> p.getPosition() == PlayerPosition.DEFENDER,
                        playerValueFunction));
            }

            // 1 Forward
            generatedSquad.addActivePlayer(pickPlayerFromRCL(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.FORWARD,
                    playerValueFunction));


            // Rest of team
            List<Integer> restOfSquadPrimaryOrReserveId = new ArrayList<>(List.of(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    1,
                    1,
                    1));
            List<PlayerPosition> restOfSquadPosition = new ArrayList<>(List.of(
                    PlayerPosition.DEFENDER,
                    PlayerPosition.DEFENDER,
                    PlayerPosition.MIDFIELDER,
                    PlayerPosition.MIDFIELDER,
                    PlayerPosition.MIDFIELDER,
                    PlayerPosition.MIDFIELDER,
                    PlayerPosition.MIDFIELDER,
                    PlayerPosition.FORWARD,
                    PlayerPosition.FORWARD));

            while (! restOfSquadPrimaryOrReserveId.isEmpty()) {
                int idIndex = random.nextInt(restOfSquadPrimaryOrReserveId.size());
                int positionIndex = random.nextInt(restOfSquadPosition.size());

                int id = restOfSquadPrimaryOrReserveId.get(idIndex);
                PlayerPosition position = restOfSquadPosition.get(positionIndex);

                if (id == 0) {
                    generatedSquad.addActivePlayer(pickPlayerFromRCL(players,
                            generatedSquad,
                            p -> p.getPosition() == position,
                            playerValueFunction));
                } else {
                    generatedSquad.addReservePlayer(pickPlayerFromRCL(players,
                            generatedSquad,
                            p -> p.getPosition() == position,
                            playerInversePriceFunction));
                }

                restOfSquadPrimaryOrReserveId.remove(idIndex);
                restOfSquadPosition.remove(positionIndex);
            }


        } while (! generatedSquad.checkRule(SquadRules.allRules));

        return generatedSquad;
    }

    private List<Player> generateRCL(List<Player> players,
                                     Squad squad,
                                     Predicate<Player> playerPredicate,
                                     Function<Player, Double> costFunction) {
        double max = players.stream()
                .filter(playerPredicate)
                .filter(p -> playerValidInSquadRule.test(squad, p))
                .mapToDouble(costFunction::apply)
                .max().orElse(0);

        double min = players.stream()
                .filter(playerPredicate)
                .filter(p -> playerValidInSquadRule.test(squad, p))
                .mapToDouble(costFunction::apply)
                .min().orElse(0);

        double limit = min + alpha * (max - min);

        return players.stream()
                .filter(playerPredicate)
                .filter(p -> playerValidInSquadRule.test(squad, p))
                .filter(p -> costFunction.apply(p) >= limit - DOUBLE_ERROR)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Player pickPlayerFromRCL(List<Player> players,
                                     Squad pickedSquad,
                                     Predicate<Player> playerPredicate,
                                     Function<Player, Double> costFunction) {

        var rcl = generateRCL(players, pickedSquad, playerPredicate, costFunction);

        return rcl.get(random.nextInt(rcl.size()));
    }
}
