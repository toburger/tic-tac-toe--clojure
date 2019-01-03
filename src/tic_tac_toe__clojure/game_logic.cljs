(ns tic-tac-toe--clojure.game-logic)

(enable-console-print!)

(defn swap-player [player]
  (case player
    :x :o
    :o :x))

(defn update-board [board x y value]
  (assoc-in board [x y] value))

(defn get-board [board x y]
  (get-in board [x y]))

(defn can-update-board [board x y]
  (nil? (get-board board x y)))

(defn- check [row v] (every? (partial = v) row))

(defn check-rows-for-winner [board v]
  (or (check (nth board 0) v)
      (check (nth board 1) v)
      (check (nth board 2) v)))

(defn- transpose [m]
  (apply mapv vector m))

(defn check-cols-for-winner [board v]
  (check-rows-for-winner (transpose board) v))

(defn check-diagonals-for-winner [board v]
  (let [get #(get-in board %)
        diag1 [(get [0 0]) (get [1 1]) (get [2 2])]
        diag2 [(get [2 0]) (get [1 1]) (get [0 2])]]
    (or (check diag1 v)
        (check diag2 v))))

(defn check-for-winner [board v]
  (or (check-rows-for-winner board v)
      (check-cols-for-winner board v)
      (check-diagonals-for-winner board v)))

(defn check-for-draw [board]
  (every? some? (flatten board)))

(defn get-winner [board]
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

  (can-update-board board 0 0)
  (can-update-board board 0 1)

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