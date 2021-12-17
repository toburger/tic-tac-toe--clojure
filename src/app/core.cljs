(ns app.core
  (:require [reagent.core :as r]
            [app.game-logic :as game-logic]))

(enable-console-print!)

(def initial-state
  {:board          [[nil nil nil]
                    [nil nil nil]
                    [nil nil nil]]
   :current-player :x
   :winner         nil})

(defonce app-state (r/atom initial-state))

(defonce board-cursor
  (r/cursor app-state [:board]))

(defonce current-player-cursor
  (r/cursor app-state [:current-player]))

(defonce winner-cursor
  (r/cursor app-state [:winner]))

(defn move! [x y]
  (let [{:keys [board current-player]} @app-state]
    (when (game-logic/can-update-cell? board x y)
      (let [new-board          (game-logic/update-board board x y current-player)
            new-winner         (game-logic/get-winner new-board)
            new-current-player (game-logic/switch-player current-player)]
        (r/rswap! app-state merge {:board          new-board
                                   :current-player new-current-player
                                   :winner         new-winner})))))

(defn restart-game! []
  (reset! app-state initial-state))

(defn player-x [props]
  [:img.PlayerX
   (r/merge-props
    {:alt "PlayerX"
     :src "./img/PlayerX.svg"}
    props)])

(defn player-o [props]
  [:img.PlayerO
   (r/merge-props
    {:alt "PlayerO"
     :src "./img/PlayerO.svg"}
    props)])

(defn no-player []
  [:span.NoPlayer])

(defn dispatch-player [player]
  [:span
   (case player
     :x [player-x {:class "PlayerX--Small"}]
     :o [player-o {:class "PlayerO--Small"}]
     [no-player])])

(defn game-cell [x y cell]
  [:button.Cell
   {:disabled (some? cell)
    :on-click #(move! x y)}
   (case cell
     :x [player-x]
     :o [player-o]
     [no-player])])

(defn game-board [board]
  [:div.Board
   (map-indexed
    (fn [rowidx row]
      [:div.Board__Row
       {:key rowidx}
       (map-indexed
        (fn [colidx cell]
          ^{:key (str rowidx "-" colidx)}
          [game-cell rowidx colidx cell]) row)])
    board)])

(defn game-current-player [current-player]
  [:div.CurrentPlayer
   [:span.CurrentPlayer__Text
    "Player:"
    [dispatch-player current-player]]])

(defn game-field [board current-player]
  [:div
   [game-board @board]
   [game-current-player @current-player]])

(def restart-image "./img/restart.png")

(defn game-over [winner]
  [:div.GameOver
   [:img.GameOver__Image
    {:on-click #(restart-game!)
     :src      restart-image
     :alt      "Restart"}]
   [:p.GameOver__Text
    (case winner
      :draw "It's a draw!"
      [:span
       "Player"
       [dispatch-player winner]
       "wins!"])]])

(defn app []
  [:div.App
   (if-let [winner @winner-cursor]
     [game-over winner]
     [game-field board-cursor current-player-cursor])])

;; (defn on-js-reload [])
;; optionally touch your app-state to force rerendering depending on
;; your application
;; (swap! app-state update-in [:__figwheel_counter] inc)

(defn ^:dev/after-load render []
  (r/render [:div.Content [app]] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  (render))
