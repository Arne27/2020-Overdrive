package za.co.entelect.challenge.game.contracts.commands

import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.game.GamePlayer
import za.co.entelect.challenge.game.contracts.map.GameMap
import za.co.entelect.challenge.game.contracts.map.CarGameMap
import za.co.entelect.challenge.game.contracts.game.CarGamePlayer
import za.co.entelect.challenge.game.contracts.map.BlockPosition
import za.co.entelect.challenge.game.contracts.Config.Config

class ChangeLaneCommand(isLeft: Boolean) extends BaseCarGameCommand {

    override def getFuturePositionAfterAdditionalProcessingOfCommand(carGameMap: CarGameMap, carGamePlayer: CarGamePlayer, currentPlayerPosition: BlockPosition): BlockPosition = {
        val futureBlockNumber = currentPlayerPosition.getBlockNumber() + carGamePlayer.getSpeed() - Config.CHANGE_LANE_PENALTY;
        
        var futureLane = currentPlayerPosition.getLane();
        if (isLeft) 
        {
            futureLane -= 1;
            carGamePlayer.turnLeft();
        }
        else 
        {
            futureLane += 1;
            carGamePlayer.turnRight();
        }

        val futurePosition = new BlockPosition(futureLane, futureBlockNumber);
        return futurePosition;
    }

}