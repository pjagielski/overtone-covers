(ns overtone-covers.patterns
  (:use [overtone.live]))

(def shrt {:sustain 0.1})

(def letsgo {0  [:B6 :G6 :B5 :G5 :B4]
             3  [:B6 :G6 :B5 :G5 :B4]
             6  [:B6 :G6 :B5 :G5 :B4]
             9  [:B6 :G6 :B5 :G5 :B4]
             16 [:E7 :B6 :G6 :E6 :B5 :G5 :B4]
             19 [:E7 :B6 :G6 :E6 :B5 :G5 :B4]
             22 [:E7 :B6 :G6 :E6 :B5 :G5 :B4]
             25 [:E7 :B6 :G6 :E6 :B5 :G5 :B4]
             32 [:C7 :E6 :C6 :G5 :C5]
             35 [:C7 :E6 :C6 :G5 :C5]
             38 [:C7 :E6 :C6 :G5 :C5]
             41 [:C7 :E6 :C6 :G5 :C5]
             48 [:F#7 :D7 :F#6 :D6 :A5 :D5]
             51 [:F#7 :D7 :F#6 :D6 :A5 :D5]
             54 [:G7 :E7 :G6 :E6 :B5 :E5]
             57 [:G7 :E7 :G6 :E6 :B5 :E5]})

(def letsgo-bass
  {0  [:E2] 3  [:E2] 6  [:E2] 9  [:E2] 11 [:E3] 12 [:E2] 14 [:E3]
   16 [:G2] 19 [:G2] 22 [:G2] 25 [:G2] 27 [:G2] 30 [:G2]
   32 [:A2] 35 [:A2] 38 [:A2] 41 [:A2] 43 [:A2] 46 [:A2]
   48 [:B2] 51 [:B2] 54 [:C3] 57 [:C3] 59 [:C3] 62 [:C3]})

(def letsgo-bass-ctrl
  {11 shrt 12 shrt 14 shrt 27 shrt 30 shrt 43 shrt 46 shrt 57 shrt 59 shrt 62 shrt})
