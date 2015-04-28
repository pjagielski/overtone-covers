(ns overtone-covers.lights
  (:use [overtone.live])
  (:require [overtone-covers.player :refer :all]
            [overtone-covers.patterns :refer :all]))

(definst bass [note 60 fine 0.12 cutoff 0.33 contour 0.25 sub-amp 0.5 amp 0.8 sustain 0.4 release 0.15]
  (let [freq (midicps note)
        osc1 (saw freq)
        osc2 (saw (midicps (+ note fine)))
        sub  (* sub-amp (pulse (/ freq 2)))
        snd  (mix [osc1 osc2 sub])
        fil-env (env-gen (adsr 0.1 0.5 0.1 0.2))
        snd  (rlpf snd (+ (* fil-env (* contour 20000)) (lin-exp cutoff 0.0 1.0 20.0 20000.0)) 0.65)
        env  (env-gen (env-lin 0.01 sustain release) 1 1 0 1 FREE)]
    (pan2 (* snd env amp))))

(definst bouncy [note 60 fine 0.12 del 0.4 amp 0.3 cutoff 0.50 release 2 mix 0.7]
  (let [freq (midicps (+ note fine))
        osc  (pulse freq 0.25)
        src  (rlpf osc (lin-exp cutoff 0.0 1.0 20.0 20000.0) 0.15)
        src  (* src (env-gen (perc) :action FREE))
        del  (comb-n src 2 0.25 8)
        src  (+ src del)
        fad  (x-fade2 src del (- (* mix 2) 1) 1)
        env  (env-gen (perc :release release) :action FREE)]
   (pan2 (* env amp src))))

(defn play-bass [step-ctl]
  (if-let [sustain (get step-ctl :sustain)]
    (partial bass :sustain sustain)
    (partial bass)))

(defn play-bouncy [_] (partial bouncy))

(def kick (sample "resources/lights/kick.wav"))
(def snare (sample "resources/lights/snare.wav"))

(defn beat-player [nome beat]
  (let [next-beat (+ 2 beat)]
    (at (nome beat) (kick))
    (at (nome (inc beat)) (do (kick) (snare)))
    (apply-by (nome next-beat) beat-player [nome next-beat])))

(comment
  (let [nome (metronome 120) beat (nome)]
    (beat-player nome beat)
    (player lights {} nome beat play-bouncy 16 64)
    (player lights-bass lights-bass-control nome beat play-bass 16 64))
  (stop))
