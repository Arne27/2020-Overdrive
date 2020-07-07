package za.co.entelect.challenge.game.contracts.game

import java.util

import za.co.entelect.challenge.game.contracts.Config.Config

import scala.collection.JavaConverters._
import za.co.entelect.challenge.game.contracts.command.RawCommand
import za.co.entelect.challenge.game.contracts.map.{BlockPosition, CarGameMap, GameMap}
import za.co.entelect.challenge.game.contracts.commands.CommandFactory

class CarGameRoundProcessor extends GameRoundProcessor{


  override def processRound(gameMap: GameMap, commandsToProcess: util.Map[GamePlayer, util.List[RawCommand]]): Boolean = {
    val carGameMap = gameMap.asInstanceOf[CarGameMap]
    val gamePlayers = carGameMap.getGamePlayers()
    val commandFactory = new CommandFactory

    if (carGameMap.getCurrentRound >= Config.MAX_ROUNDS){
      for (i <- gamePlayers.indices){
        val player = gamePlayers(i).asInstanceOf[CarGamePlayer]
        player.finish()
      }
      return true
    }

    val player1StartPositions = carGameMap.getPlayerBlockPosition(1)
    val player2StartPositions = carGameMap.getPlayerBlockPosition(2)

    var carStartPositions = Array[BlockPosition]()
    carStartPositions = carStartPositions.appended(player1StartPositions)
    carStartPositions = carStartPositions.appended(player2StartPositions)
    carGameMap.setStartRound(carStartPositions)

    for ( i <- gamePlayers.indices) {
      val gamePlayer = gamePlayers(i)
      val commandText = commandsToProcess.get(gamePlayer).get(0).getCommand
      var playerCommand: RawCommand = commandFactory.makeCommand(commandText)
      playerCommand.performCommand(gameMap, gamePlayer)
    }

    carGameMap.resolveCyberTruckCollisions() //needs to happen first because projected path of player is used in player collisions
    carGameMap.resolvePlayerCollisions()
    carGameMap.calculateEffectsOfAndApplyStagedPositionsToPlayers()

    carGameMap.placeRequestedCyberTrucks()
    return true
  }

  override def getErrorList(gameMap: GameMap): util.List[String] = {
    return new Array[String](0).toList.asJava
    //TODO: Fix error reporting
  }

  override def getErrorList(gameMap: GameMap, player: GamePlayer): util.List[String] = {
    throw new NotImplementedError("Car game round processor get error list => game map and player")
  }
}
