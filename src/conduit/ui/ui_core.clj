(ns conduit.ui.ui-core
    (:use [clojure.core.match :only [match]])
    (:import (javax.swing SwingUtilities)))

(defn invoke-later [f]
    (SwingUtilities/invokeLater f))

(defprotocol IPropSettable
    (setprop [this prop value]))

(defn set-prop! [el k v]
    (setprop el k v))
