(ns conduit.ui.frame
    (:require [conduit.core :as c]
              [conduit.ui.ui-core :as uic])
    (:import (javax.swing JFrame)
             (conduit.ui.ui_core IPropSettable)))

(def setters 
    {:title #(.setTitle %1 %2)
     :show #(.setVisible %1 %2)})                               
     
(def slots
    setters)


(extend javax.swing.JFrame
    uic/IPropSettable
    {:setprop (fn [this prop value]
                 (uic/invoke-later #((get setters prop) this value)))})

(defn frame [& opts]
    (let [opts (apply hash-map opts)
          a (atom {:state nil :listeners nil})]
          (proxy [javax.swing.JFrame conduit.ui.ui_core.IPropSettable]
              []
              (setprop [prop value]
                  (uic/invoke-later #((get setters prop) this value)))
              (attach [cfn to]
                  (swap! a assoc-in [:listeners to] cfn))
              (accept [k v]
                  (uic/set-prop! this k v)))))
                                                      

