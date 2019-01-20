(ns tic-tac-toe--clojure.game-logic)

(defn switch-player
  "Switches a player to the next player."
  [player]
  (case player
    :x :o
    :o :x))

(defn update-board
  "Updates a single `board` value by `x` and `y` coordinates and returns the modified board.
  Leaves the original board unchanged."
  [board x y value]
  (assoc-in board [x y] value))

(defn get-cell
  "Gets a single cell value in a `board` by providing `x` and `y` coordinates"
  [board x y]
  (get-in board [x y]))

(defn can-update-cell
  "Checks if a cell value can be updated.
  (simply checks if the `board` contains nil)"
  [board x y]
  (nil? (get-cell board x y)))

(defn- check [row v] (every? (partial = v) row))

(defn check-rows-for-winner
  "Checks if all elements a row contain the same value `v`.
  Checks all three rows."
  [board v]
  (or (check (nth board 0) v)
      (check (nth board 1) v)
      (check (nth board 2) v)))

(defn- transpose [m]
  (apply mapv vector m))

(defn check-cols-for-winner
  "Checks if all elements of a column contain the same value `v`.
  Checks all three columns.

  Uses a simple trick by transposing the board so that the whole board
  gets rotated and the columns become rows. Then the rows can be passed
  to checkRowsForWinner"
  [board v]
  (check-rows-for-winner (transpose board) v))

(defn check-diagonals-for-winner
  "Checks if all elements of a diagonal contain the same value `v`.
  Checks the two diagonals."
  [board v]
  (let [get #(get-in board %)
        diag1 [(get [0 0]) (get [1 1]) (get [2 2])]
        diag2 [(get [2 0]) (get [1 1]) (get [0 2])]]
    (or (check diag1 v)
        (check diag2 v))))

(defn check-for-winner
  "Checks if any one of the
  * rows
  * columns
  * diagonals
  of the `board` contain the same value `v`."
  [board v]
  (or (check-rows-for-winner board v)
      (check-cols-for-winner board v)
      (check-diagonals-for-winner board v)))

(defn check-for-draw
  "Checks if the game is a draw
  It does check if all of the boards aren't nil."
  [board]
  (every? some? (flatten board)))

(defn get-winner
  "Determines either the winner or a draw.
  If null is returned the game continues."
  [board]
  (cond
    (check-for-winner board :x)
    :x
    (check-for-winner board :o)
    :o
    (check-for-draw board)
    :draw
    :else
    nil))

(comment
  (def board [[:x nil :o] [nil nil nil] [:x :x :x]])

  (prn board)

  (update-board board 1 1 :x)

  (can-update-cell board 0 0)
  (can-update-cell board 0 1)

  (check-rows-for-winner board :x)
  (check-cols-for-winner board :x)

  (check-for-winner board :x)
  (check-for-winner board :o)

  (check-for-draw board)
  (check-for-draw [[nil nil nil] [nil nil nil] [nil nil nil]])

  (get-winner board)
  (get-winner [[nil nil :o] [nil nil nil] [nil nil nil]])
  (get-winner [[nil nil nil] [nil nil nil] [nil nil nil]])
  )