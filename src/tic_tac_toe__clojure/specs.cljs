(ns tic-tac-toe--clojure.specs
  (:require [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [tic-tac-toe--clojure.game-logic :as game-logic]))

(spec/def ::player #{:x :o})

(spec/def ::board-pos #{0 1 2})

(spec/def ::cell
  (spec/or :empty nil?
           :player ::player))

(spec/def ::row
  (spec/coll-of ::cell :kind vector? :count 3))

(spec/def ::board
  (spec/coll-of ::row :kind vector? :count 3))

(spec/def ::current-player ::player)

(spec/def ::winner ::cell)

(spec/def ::game
  (spec/keys :req-un [::board
                      ::current-player
                      ::winner]))

(spec/fdef game-logic/switch-player
           :args (spec/cat :player ::player)
           :ret ::player
           :fn #(not= (:ret %) (-> % :args :player)))

(spec/fdef game-logic/update-board
           :args (spec/cat :board ::board
                           :x ::board-pos
                           :y ::board-pos
                           :value ::player)
           :ret ::board)

(spec/fdef game-logic/get-cell
           :args (spec/cat :board ::board
                           :x ::board-pos
                           :y ::board-pos)
           :ret ::cell)

(spec/fdef game-logic/can-update-cell?
           :args (spec/cat :board ::board
                           :x ::board-pos
                           :y ::board-pos)
           :ret boolean?)

(spec/fdef game-logic/winner-in-rows?
           :args (spec/cat :board ::board
                           :player ::player)
           :ret boolean?)

(spec/fdef game-logic/winner-in-cols?
           :args (spec/cat :board ::board
                           :player ::player)
           :ret boolean?)

(spec/fdef game-logic/winner-in-diagonals?
           :args (spec/cat :board ::board
                           :player ::player)
           :ret boolean?)

(spec/fdef game-logic/winner?
           :args (spec/cat :board ::board
                           :player ::player)
           :ret boolean?)

(spec/fdef game-logic/draw?
           :args (spec/cat :board ::board)
           :ret boolean?)

(spec/fdef game-logic/get-winner
           :args (spec/cat :board ::board)
           :ret ::winner)

(comment
  (spec/valid? ::game {:board          [[nil nil nil] [nil :x nil] [nil nil :o]]
                       :current-player :x
                       :winner         nil})

  (gen/generate (spec/gen ::game))

  (stest/check `game-logic/switch-player)

  (stest/check `game-logic/update-board)

  (stest/check `game-logic/get-cell)

  (stest/check `game-logic/can-update-cell?)

  (stest/check `game-logic/winner-in-rows?)

  (stest/check `game-logic/winner-in-cols?)

  (stest/check `game-logic/winner-in-diagonals?)

  (stest/check `game-logic/winner?)

  (stest/check `game-logic/draw?)

  (stest/check `game-logic/get-winner))
;

