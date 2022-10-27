package hr.fer.hmo.lab1.algorithm;

import hr.fer.hmo.lab1.player.Player;
import hr.fer.hmo.lab1.player.PlayerPosition;
import hr.fer.hmo.lab1.squad.Squad;
import hr.fer.hmo.lab1.squad.SquadRules;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 26.10.2022.
 */

@RequiredArgsConstructor
public class GreedyConstructionAlgorithm implements ISearchAlgorithm {
    private final double alpha;
    private final Random random;

    private Function<Player, Double> playerValueFunction = p -> Double.valueOf(p.getPoints());
    private Function<Player, Double> playerInversePriceFunction = p -> 1 / p.getPrice();

    @Override
    public Squad search(List<Player> players, Squad startingSquad) {

        Squad generatedSquad;
        do {
            generatedSquad = new Squad(11, 4, players);
            // MANDATORY PLAYERS

            // 1 Goalkeeper for active team
            generatedSquad.addActivePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.GOAL_KEEPER,
                    playerValueFunction));
            // 1 Goalkeeper for reserve team
            generatedSquad.addReservePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.GOAL_KEEPER,
                    playerInversePriceFunction));

            // 3 Defenders
            for (int i = 0; i < 3; i++) {
                generatedSquad.addActivePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                        generatedSquad,
                        p -> p.getPosition() == PlayerPosition.DEFENDER,
                        playerValueFunction));
            }

            // 1 Forward
            generatedSquad.addActivePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                    generatedSquad,
                    p -> p.getPosition() == PlayerPosition.FORWARD,
                    playerValueFunction));


            // Rest of main team

            for (int i = 0; i < 6; i++) {
                generatedSquad.addActivePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                        generatedSquad,
                        p -> p.getPosition() != PlayerPosition.GOAL_KEEPER,
                        playerValueFunction));
            }

            // Rest of reserve team
            for (int i = 0; i < 3; i++) {
                generatedSquad.addReservePlayer(pickPlayerFromRandomisedRCLThatIsNotInSquad(players,
                        generatedSquad,
                        p -> p.getPosition() != PlayerPosition.GOAL_KEEPER,
                        playerInversePriceFunction));
            }

        } while (! generatedSquad.checkRule(SquadRules.allRules));

        return generatedSquad;
    }

    private List<Player> generateRandomisedRCL(List<Player> players,
                                               Predicate<Player> playerPredicate,
                                               Function<Player, Double> costFunction) {

        List<Player> rcl = generateRCL(players, playerPredicate, costFunction);

        Collections.shuffle(rcl);

        return rcl;
    }

    private List<Player> generateRCL(List<Player> players,
                                     Predicate<Player> playerPredicate,
                                     Function<Player, Double> costFunction) {
        double max = players.stream()
                .filter(playerPredicate)
                .mapToDouble(costFunction::apply)
                .max().orElse(0);

        double min = players.stream()
                .filter(playerPredicate)
                .mapToDouble(costFunction::apply)
                .min().orElse(0);

        double limit = min + alpha * (max - min);

        return players.stream()
                .filter(playerPredicate)
                .filter(p -> costFunction.apply(p) > limit)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Player pickPlayerFromRandomisedRCLThatIsNotInSquad(List<Player> players, Squad pickedSquad, Predicate<Player> playerPredicate, Function<Player, Double> costFunction) {
        var rcl = generateRandomisedRCL(players, playerPredicate, costFunction);

        Player player;
        do {
            player = rcl.get(random.nextInt(0, rcl.size() - 1));

        } while (pickedSquad.isPlayerInSquad(player));

        return player;
    }
}
