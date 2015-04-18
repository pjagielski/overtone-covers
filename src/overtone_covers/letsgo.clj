(ns overtone-covers.letsgo
  (:use [overtone.live])
  (:require [overtone-covers.player :refer :all]
            [overtone-covers.patterns :refer :all]))

(definst lead [note 60 release 0.30 amp 0.3 sub-gate 0.3]
  (let [freq  (midicps note)
        freq2 (midicps (- note 0.08))
        freq3 (midicps (+ note 0.20))
        freq4 (midicps (+ note 0.48))
        osc   (saw [freq freq2 freq3 freq4])
        sub   (lpf (pulse (* freq 0.5) 0.3) 500)
        osc   (+ (* sub-gate sub) (* amp osc))
        mix   (mix osc)
        env   (env-gen (env-lin 0.015 0.20 release) :action FREE)]
    (pan2 (* mix env))))

(comment (lead))

(definst bass [note 60 amp 0.3 osc-mix 0.0 cutoff 0.35 sustain 0.2 release 0.15 fil-dec 0.85 fil-amt 1500]
  (let [freq (midicps note)
        sub-freq (midicps (- note 12))
        osc1 (saw:ar freq)
        osc2 (pulse sub-freq 0.5)
        osc (+ (* osc-mix osc2) (* (- 1 osc-mix) osc1))
        snd [osc osc]
        fil-env (env-gen (adsr 0.0 fil-dec 0.1 fil-dec))
        snd (lpf snd (+ (* fil-env fil-amt) (lin-exp cutoff 0.0 1.0 20.0 20000.0)))
        env (env-gen (env-lin 0.01 sustain release) 1 1 0 1 FREE)]
    (out 0 (* amp env snd))))

(comment (bass))

(definst bend-noise [amp 0.7 decay 0.85 cutoff 0.65]
  (let [osc (white-noise)
        snd [osc osc]
        snd (bpf snd (lin-exp cutoff 0.0 1.0 20.0 20000.0) 0.5)
        env (env-gen (env-adsr 0.0 decay 0.0 0.7) :action FREE)]
  (out 0 (* amp env snd))))

(comment (bend-noise))

(defn noise-player [nome beat]
  (let [next-beat (+ 4 beat)]
    (at (nome (+ 3 beat)) (bend-noise))
    (apply-by (nome next-beat) noise-player [nome next-beat])))

(def kick (sample "resources/letsgo/kick.wav"))
(def clap (sample "resources/letsgo/clap.wav"))
(def tambo (sample "resources/letsgo/tambo.wav"))

(defn beat-player [nome beat]
  (let [next-beat (+ 2 beat)]
    (at (nome beat) (kick))
    (at (nome (+ 0.5 beat)) (tambo))
    (at (nome (+ 1 beat)) (do (kick) (clap)))
    (at (nome (+ 1.5 beat)) (tambo))
    (apply-by (nome next-beat) beat-player [nome next-beat])))

(defn play-lead [step-ctl]
  (partial lead))

(defn play-bass [step-ctl]
  (if-let [sustain (get step-ctl :sustain)]
    (partial bass :sustain sustain)
    (partial bass)))

(def nome (metronome 128))

(defn play-all [nome]
  (let [beat (nome)]
      (beat-player nome beat)
      (noise-player nome beat)
      (player letsgo {} nome beat play-lead 16 64)
      (player letsgo-bass letsgo-bass-ctrl nome beat play-bass 16 64)))

(comment
  (play-all nome)
  (stop))