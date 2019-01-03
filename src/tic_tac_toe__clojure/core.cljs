(ns tic-tac-toe--clojure.core
    (:require [reagent.core :as reagent :refer [atom class-names]]
              [tic-tac-toe--clojure.game-logic :as game-logic]))

(enable-console-print!)

(def initial-state
  {:board [[nil nil nil]
           [nil nil nil]
           [nil nil nil]]
   :current-player :x
   :winner nil})

(defonce app-state (atom initial-state))

(defn move! [x y]
  (let [{:keys [board current-player]} @app-state]
    (if (game-logic/can-update-board board x y)
      (let [new-board (game-logic/update-board board x y current-player)
            winner (game-logic/get-winner new-board)
            current-player (game-logic/swap-player current-player)]
        (swap! app-state merge {:board new-board
                                :current-player current-player
                                :winner winner})))))

(defn restart-game! []
  (reset! app-state initial-state))

(defn player-x [{:keys [className]}]
  [:img
   {:alt "PlayerX"
    :src "https://toburger.github.io/tic-tac-toe/static/media/PlayerX.6a80d7b3.svg"
    :className (class-names "PlayerX" className)}])

(defn player-o [{:keys [className]}]
  [:img
   {:alt "PlayerO"
    :src "https://toburger.github.io/tic-tac-toe/static/media/PlayerO.b936ed0a.svg"
    :className (class-names "PlayerO" className)}])

(defn no-player []
  [:span {:className "NoPlayer"}])

(defn dispatch-player [player]
  [:span
   (case player
     :x [player-x {:className "PlayerX--Small"}]
     :o [player-o {:className "PlayerO--Small"}]
     [no-player])])

(defn game-cell [x y cell]
  [:button
   {:key (str x "-" y)
    :disabled (some? cell)
    :onClick (fn [] (move! x y))
    :className "Cell"}
   (case cell
     :x [player-x]
     :o [player-o]
     [no-player])])

(defn game-board [board]
  [:div
   {:className "Board"}
   (map-indexed
    (fn [idx row]
      [:div {:key idx :className "Board__Row"}
       (map-indexed (partial game-cell idx) row)])
    board)])

(defn game-current-player [current-player]
  [:div
   {:className "CurrentPlayer"}
   [:span
    {:className "CurrentPlayer__Text"}
    "Player:"
    [dispatch-player current-player]]])

(defn game-field [{:keys [board current-player]}]
  [:div
   [game-board board]
   [game-current-player current-player]])

(def restart-image "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADEAAAAxCAMAAABEQrEuAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAMAUExURQAAAJqtsJutsZqusZuuspyusZyusp2vs52ws56wtJ+xtZ+ytJ+ytaCxtKCxtaCytaGytqOzt6K0t6S0t6O0uKS0uKS1uaW2uKW2uae3uqe4uqe4u6i4u6m5vKm6vKq6vay8vqy8v669wK6+wK6+wbHAwrLAw7PBxLPCxLTCxLXDxbXDxrXExrbEx7jFyLnGyLnGybvIyrzJy77KzL7KzcDMzsHNz8LOz8LO0MPP0cTP0cTQ0sbR08fS08jS1MjT1cvU1svU18zV183W2M7X2c/Y2c/Y2tDY2tHZ29Ha29La3NPb3dPc3dTc3dbe39ff4Nff4dje4Njf4dng4Njg4dvh49vi49zj5N3j5d3k5d7k5uDl5uHm5+Hn6OHo6OPo6ePp6uTp6eTp6uXq6uXq6+fs7Ojs7ent7uru7uzv8Ozv8erx8uvx8+ry8uvy8+zw8O3x8uzx8+3y8uzy8+zy9O3z9e309O709e/19vDz8/Dz9PHz9fH09PH09fD19vH19/D29vH29/T29vT39/L2+PT3+PT3+fP4+PX4+PX4+fb5+vb6+vf6+/j5+fj5+vj6+vj6+/v8/P///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMPhzfUAAAEAdFJOU////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wBT9wclAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAGXRFWHRTb2Z0d2FyZQBwYWludC5uZXQgNC4wLjE2RGmv9QAABBRJREFUSEt9Vv9DGzUUryJpMsU7Y6utFh0i+AXQjZVNHTIU57Rz4hc2YG6iMueXjjq9q7FIb7153bhjOuQfju/lkrtrC35+uHz7fPLyXl6Sy8lDEHUD3/OD8KFu92BAEXVcxxWe53tt4ThuoLtT9CkCx/EiXUeEouGGuq7Ro/Abzb5hQOD0ajKKPcfd09VeBA2ha4hU4dd9XdtePz8/Vz29UNv8S/eIW+lciUI09lXp1aZ4nlDGGCWsfGpNdcp79WRlRiFcVXTPPUMYLxrYlIx9pgb2tkzUtEI4qlh5ntiabGCRV37CoQf1rqJoRaehijPDluZlwexlHIzqsS9KEdXxG04PaU4fbLKE48Et/MaKhnJrimjGAPjQeSQIFWRUeKpWHdbjh+BpsoKULUwHVGxh65MjlhTD5reBE2BAQfGHB2WL9wXJttIgA+g0TuuAEVCoOFXzekiDT06VyBO6gSCrwApg/TkZNKH6s53MyCjD73J7Z+1VktphEzgxzJ6TzXtQmzcmrOrG1eUyEHnljS92L42ka2XfAE8EoMDd/ueFZMRaPdivQKvARx59qXXt8cQKfRuIXSFzEfp/g+ruYsHe3Nkd1Xpa2LmUbJI1BsR9V+bQGXkh8Zt8cNtaOm4ssvH9SZM43PoFmI7MeXgq5tBZ1V3yZrPJOHRlPZmMXgGmG+UEOj4zonvZyXYaNcCxE17JtMlHwBS7OXWyJ4xpsvRt4hLCHv1z0pjMLwKzHeTUsR/XimdJbaUnHzlvzZjZ6AIwvaDfxoWvejafl1qvJTbej23EfhjP2Rknc2ghpK+3RxM/asAUYRyrt8zi7Re7k8d0HVo2+XgzjdU6MCFWaj9qSXd+7VqS9tbMRCE8aSbjNib8r7DnmCU3zarAyN9nH9H1/Dvt71aTEWsciA9hB1VeyTHjepGe+Hd+OPaFzh0cTCfBpu8CD1YEuYsX0XtpTPNvhtenbGaXYDu3d75PM+Ym8JpdUOxiKrZ4GiFa+bS9fUOHr/KcKkCgDqE6H7KBD8vZzMbZtDy7qDKQc16I+4psA1i+OoPyDkbrbjmTf0XOCKwqCzKrTMAlh3eJuqMvH3lbIaxyGzi+vktkR93S8/9z/TzFriOljutHhXTu4/fUkRJOPkeC28GvUjxQYnl6KJtSKSz2JQ7HS4kVMlBXtVxiyQ5nkK98jYNhzNEK6f2mio2XH0s2X4PS6l0cCuv6DdYK2Y6fHHlxjOZtswfcIk9OK59BYJ41o5AdM8d6ddSmjAIYH1/4Ie7s6OcGkChgFryxVe3Hyx+eW1i8eDVeKsB14mcVkSqk/D1+eQbgJXMhsgoZuof8h3gNkRoA9ChQ0xDdlBD5rtPLH1AAAuG4blMI4eJP0OA6BxWIKAz8btg3uYKU/wG8TKU6ZqxftgAAAABJRU5ErkJggg==")

(defn game-over [{:keys [winner]}]
  [:div
   {:className "GameOver"}
   [:img
    {:className "GameOver__Image"
     :onClick #(restart-game!)
     :src restart-image
     :alt "Restart"}]
   [:p
    {:className "GameOver__Text"}
    (case winner
      :draw "It's a draw!"
      [:span
       "Player"
       [dispatch-player winner]
       "wins!"])]])

(defn app []
  [:div
   {:className "App"}
   (if (nil? (:winner @app-state))
     [game-field @app-state]
     [game-over @app-state])])

(reagent/render-component [app]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
