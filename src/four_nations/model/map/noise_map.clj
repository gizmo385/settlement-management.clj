(ns four-nations.model.map.noise-map
  (:require [four-nations.model.map.utils :as utils]))

(defrecord NoiseMap [noise height width])

(defn generate-2d-noise
  "Given a height and a width, generates a two-dimensional array of noise, where each cell is
   occupied by a number between 1 and 255 (inclusive on both sides)."
  [height width]
  (letfn [(make-cell [] (inc (rand-int 255)))
          (make-row [] (repeatedly width make-cell))]
    (to-array-2d (repeatedly height make-row))))

(defn average-of-neighbors
  "Given a 2-dimensional array of noise, a height, width, and a cell coordinate, calculates the
   average of the values of its neighbor cells"
  [noise height width x y]
  (let [neighbors (utils/coordinates->neighbors height width noise [x y])]
    (quot (apply + neighbors)
          (count neighbors))))

(defn average-2d-noise
  "Given a 2-dimensional array of noise, returns a 2-dimensional array of averaged noise, where the
   value of each cell is the average of the values of its neighbors."
  [noise height width]
  (for [y (range height)]
    (for [x (range width)]
      (average-of-neighbors noise height width x y))))

(defn generate-noisemap
  "Given a height and width, generates a 2-dimensional map of averaged noise, which can be used for
   procedural terrain generation."
  [height width]
  (let [random-noise (generate-2d-noise height width)
        averaged-noise (average-2d-noise random-noise height width)]
    (->NoiseMap averaged-noise height width)))
