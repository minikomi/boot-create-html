(ns example.core
  (:require
   [clojure.string :as s]
   [hiccup.page :as hp]
   [hiccup.core :as h]))

(defn weather-table [weather-data]
  (h/html
   [:table
    [:thead
     [:th "Country"]
     [:th "City"]
     [:th "Min (℃)"]
     [:th "Max (℃)"]]
    [:tbody
     (for [{:keys [country city min max]} (sort-by :max weather-data)]
       [:tr
        [:td.country
         (s/upper-case country)]
        [:td city]
        [:td.min min]
        [:td.max max]])]]))

(defn base-template [data]
  (hp/html5
   {}
   [:head
    [:meta {:charset "UTF-8"}]
    (hp/include-css "/css/styles.css")
    [:title (:title data "DEFAULT TITLE")]]
   [:body {:class (:body-class data "default-class")}
    [:h1 (:title data "DEFAULT TITLE")]
    (when (:weather data)
      (weather-table (:weather data)))]))
