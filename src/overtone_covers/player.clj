(ns overtone-covers.player
  (:use [overtone.live]))

(defn play-step [step idx start chord step-ctrl synth-fn]
  (dorun (for [n chord]
    (apply-at (+ (* step idx) start) #(apply (synth-fn step-ctrl) [:note (note n)]) []))))

(defn player [pattern pattern-ctrl nome beat synth-fn beats resolution]
  (let [t (nome beat)
        next-beat (+ beats beat)
        next-beat-at (nome next-beat)
        step (/ (- next-beat-at t) resolution)]
    (dorun (for [[idx chord] pattern]
      (play-step step idx t chord (get pattern-ctrl idx) synth-fn)))
    (apply-by next-beat-at player [pattern pattern-ctrl nome next-beat synth-fn beats resolution])))

(defn sequencer [nome patterns scale idx beat]
  (doseq [[sound pattern] patterns]
     (when (= 1 (nth pattern (mod idx (count pattern))))
       (at (nome beat) (sound))))
  (let [next-beat (+ scale beat)]
    (apply-by (nome next-beat) sequencer [nome patterns scale (inc idx) next-beat])))

(defn simple-player [{:keys [:pattern :pattern-ctrl :nome :beat :synth-fn :beats :resolution]
                      :or {:beats 16 :resolution 64 :pattern-ctrl {}}}]
  (player pattern pattern-ctrl nome beat synth-fn beats resolution))
