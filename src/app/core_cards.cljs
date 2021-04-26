(ns app.core-cards
  (:require [reagent.core :as r]
            [devcards.core :as dc :refer [defcard defcard-doc]]
            [app.core :refer [app player-x player-o game-over app-state game-cell game-current-player]]))

(defcard-doc
  "# Tic Tac Toe - Game Components")

(defcard player-x-card
  "Player X"
  (dc/reagent (player-x nil)))

(defcard player-o-card
  "Player O"
  (dc/reagent (player-o nil)))

(defcard game-cell-card
  "Empty Game Cell"
  (dc/reagent (game-cell 0 0 nil)))

(defcard game-current-player-card
  "Current Player"
  (dc/reagent (game-current-player :x)))

(defcard game-over-card
  "Game Over"
  (dc/reagent (game-over :x)))

(defcard core-card
  (dc/reagent app)
  app-state
  {:inspect-data true
   :history true})
