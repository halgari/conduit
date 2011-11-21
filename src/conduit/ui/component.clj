(ns conduit.ui.component    
    (:require [conduit.core :as c]
              [conduit.ui.ui-core :as uic])
    (:import (javax.swing JFrame)
             (conduit.ui.ui_core IPropSettable)))

(def sets {:enabled #(.setEnabled %1 %2)
           :visible #(.setVisible %1 %2)})

(defn- focus-listener [this state]
    (reify java.awt.event.FocusListener
           (focusGained [this e]
               (c/emit-all @state :focus :gained))
           (focusLost [this e]
               (c/emit-all @state :focus :lost))))

(defn addListeners [this state]
    (do (.addFocusListener this (focus-listener this state))))
    
