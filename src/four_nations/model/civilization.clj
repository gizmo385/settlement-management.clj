(ns four-nations.model.civilization
  (:require
    [four-nations.model.units.core :as units]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Record Definitions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; The civilization metadata is information that is useful to keep track of but is not immediately
;;; relevant to the state of the civilization. This includes information that has been loaded from
;;; static configuration files, such as the available unit attributes and unit types.
(defrecord CivilizationMetadata
  [unit-attributes unit-types])

(defrecord Civilization
  [units unit-locations tribe metadata])

(defn load-civilization-metadata
  [unit-attributes-filename unit-types-filename]
  (let [unit-attributes (units/file->unit-attributes unit-attributes-filename)
        unit-types (units/file->unit-types unit-types-filename unit-attributes)]
    (->CivilizationMetadata unit-attributes unit-types)))

(defn start-civilization
  [metadata tribe]
  (->Civilization [] {} tribe metadata))

(defn add-unit
  [civilization unit x y]
  (let [unit-id (:id unit)]
    (-> civilization
        (update :units conj unit)
        (update :unit-locations assoc unit-id [x y]))))

(defn spawn-unit
  [civilization unit-type x y]
  (let [new-unit (units/unit-type->unit unit-type (:tribe civilization))]
    (add-unit civilization new-unit x y)))

(comment
  (use 'clojure.pprint)
  (let [metadata (load-civilization-metadata "unit-attributes.edn" "unit-types.edn")]
    (pprint (start-civilization metadata :water)))
  )
