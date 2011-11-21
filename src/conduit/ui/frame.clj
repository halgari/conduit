(ns conduit.ui.frame
    (:require [conduit.core :as c]
              [conduit.ui.ui-core :as uic]
              [conduit.ui.component :as com])
    (:import (javax.swing JFrame)
             (conduit.ui.ui_core IPropSettable)))

(def setters 
    {:title #(.setTitle %1 %2)
     :show #(.setVisible %1 %2)
     :size (fn [o [w h]] (.setSize o w h))
     :text #(.setText %1 %2)
     :children #(doseq [c %2] (.add %1 c))})                               
     
(def slots
    setters)


(extend javax.swing.JFrame
    uic/IPropSettable
    {:setprop (fn [this prop value]
                 (uic/invoke-later #((get setters prop) this value)))})

(defmacro proxy-ui [el props sets listeners]
    `(let [a# (atom {:state nil :listeners nil})
           p# ~props
           o# (proxy [~el conduit.ui.ui_core.IPropSettable]
              []
              (setprop [~'prop ~'value]
                  (uic/invoke-later #((get p# ~'prop) ~'this ~'value)))
              (attach [~'cfn ~'to]
                  (swap! a# assoc-in [:listeners ~'to] ~'cfn))
              (accept [~'k ~'v]
                  (uic/set-prop! ~'this ~'k ~'v)))]
          (doseq [s# ~sets]
                 (uic/set-prop! o# (first s#) (second s#)))
          (doseq [l# ~listeners]
                 (l# o# a#))
          o#))

(defn frame [& opts]
    (let [options (apply hash-map opts)]
         (proxy-ui javax.swing.JFrame
                   setters
                   (:sets options)
                   [com/addListeners])))

(defn button [& opts]
    (let [options (apply hash-map opts)]
         (proxy-ui javax.swing.JButton
                   setters
                   (:sets options)
                   [])))

(frame :sets {:size [300 100] 
              :title "Hey" 
              :show true 
              :children [(button :sets{ :text "Click Me!"})]})
                                                      

