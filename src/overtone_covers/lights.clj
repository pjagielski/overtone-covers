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
        fad  (x-fade2 src del (- (* mix 2) 1) 1)
        src  (+ src fad)
        env  (env-gen (perc :release release) :action FREE)]
   (pan2 (* env amp src))))

(defsynth fx-echo-amp [bus 0 max-delay 1.0 delay-time 0.4 decay-time 2.0 amp 0.5]
  (let [source (in bus)
        echo (comb-n source max-delay delay-time decay-time)]
    (replace-out bus (pan2 (+ (* amp echo) source) 0))))

(comment
  (def echo (inst-fx! bass fx-echo-amp))
  (ctl echo :delay-time 0.07 :decay-time 0.2 :amp 0.35))

(def brvb (inst-fx! bouncy fx-freeverb))
(ctl brvb :room-size 0.7)

(comment
  (clear-fx bouncy)
  (clear-fx bass))

(defn play-bass [step-ctl]
  (if-let [sustain (get step-ctl :sustain)]
    (partial bass :sustain sustain)
    (partial bass)))

(defn play-bouncy [_] (partial bouncy))

(def kick   (sample "resources/lights/kick.wav"))
(def snare  (sample "resources/lights/snare.wav"))
(def shaker (sample "resources/lights/shaker.wav"))
(def tom    (sample "resources/lights/tom_flat.wav"))
(def crash  (sample "resources/lights/crash.wav"))

(def _ 0)
(def none (repeat 16 _))

(def pattern1
  {kick   [1 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
   snare  [_ _ _ _ _ _ _ _ 1 _ _ _ _ _ _ _]
   shaker [_ _ _ _ 1 _ _ _ _ _ _ _ 1 _ _ _]
   crash  [1 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
   tom none})

(def pattern2
  {kick   [1 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
   snare  [_ _ _ _ _ _ _ _ 1 _ _ _ _ _ _ _]
   shaker [_ _ _ _ 1 _ _ _ _ _ _ _ 1 _ _ _]
   tom    [_ _ _ _ _ _ _ _ _ _ _ _ 1 _ _ _]
   crash  none})

(def pattern3
  {kick   [1 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
   snare  [_ _ _ _ _ _ _ _ 1 _ _ _ _ _ _ _]
   shaker [_ _ _ _ 1 _ _ _ _ _ _ _ 1 _ _ _]
   tom none crash none})

(def pattern4
  {kick   [1 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _]
   snare  [_ _ _ _ _ _ _ _ 1 _ _ _ _ _ 1 _]
   shaker [_ _ _ _ 1 _ _ _ _ _ _ _ 1 _ _ _]
   tom none crash none})

(def patterns
  (apply (partial merge-with concat)
         (flatten (vector pattern1 pattern2
                          (repeat 2 [pattern3 pattern2])
                          pattern3 pattern4))))

(defn play-all []
  (let [nome (metronome 120) beat (nome)]
      (sequencer nome patterns 1/8 0 beat)
      (player lights {} nome beat play-bouncy 16 64)
      (player lights-bass lights-bass-control nome beat play-bass 16 64)))

(comment
  (play-all)
  (stop))

