(ns magnars.pages
  (:require [clojure.string :as str]
            [datomic-type-extensions.api :as d]
            [magnars.blog-posts :as blog-posts]
            [magnars.elements :as e]
            [magnars.rss :as rss]
            [powerpack.markdown :as md])
  (:import (java.time LocalDateTime)
           (java.time.format DateTimeFormatter)
           (java.util Locale)))

(def lang (Locale/forLanguageTag "en"))

(defn ymd [^LocalDateTime ldt]
  (.format ldt (DateTimeFormatter/ofPattern "MMMM d. y" lang)))

(defn comma-separated [coll]
  (drop 1 (interleave (into (list " og " "")
                            (repeat (dec (count coll)) ", "))
                      coll)))

(defn prepare-blog-post-teaser [{:blog-post/keys [description published series]
                                 :page/keys [title uri]}]
  (cond-> {:title title
           :kicker (some-> (:series/name series) (str ": "))
           :kicker-url (:page/uri series)
           :url uri
           :description (md/render-html description)
           :kind :teaser-article}
    published (assoc :published (ymd published))))

(defn layout [{:keys [title]} & forms]
  [:html {:lang "en"}
   [:head
    [:title title]
    [:meta {:name "theme-color" :content "#222"}]
    [:link {:href "/atom.xml" :rel "alternate" :title "magnars.com" :type "application/atom+xml"}]]
   [:body
    forms]])

(defn render-frontpage [page]
  (layout
   {:title "magnars"}
   (e/header-section
    {:title "magnars"})
   [:div.section
    [:div.content.info-section
     [:div.section-content.text-content
      [:p "Greetings! I am Magnar Sveen and this is my technical blog on topics such as Clojure, Datomic, functional programming, frontend development, architecture, and more."]
      [:p "You might also know me from the video series "
       [:a {:href "https://emacsrocks.com"} "Emacs Rocks!"] " or "
       [:a {:href "https://www.parens-of-the-dead.com"} "Parens of the Dead"] ", "
       " or maybe one of my "
       [:a {:href "https://github.com/magnars?tab=repositories&q=&type=source&language=&sort=stargazers"}
        "open source projects"] ". In either case, welcome to my blog!"]]]]

   (e/teaser-section
    {:teasers (->> (blog-posts/get-blog-posts (d/entity-db page))
                   (map prepare-blog-post-teaser))})))

(defn get-series-blurb [series]
  (str/replace (:series/blurb series)
               #"\[(.+)\]"
               (fn [[_ content]]
                 (str"<a href='" (:page/uri series) "'>" content "</a>"))))

(defn get-relevant-post [blog-post series]
  (let [other-posts (->> (:blog-post/_series series)
                         (remove #{blog-post})
                         (sort-by :blog-post/published))]
    (if (:series/sequential? series)
      (if-let [next-post (first (drop-while #(.isBefore (:blog-post/published %)
                                                        (:blog-post/published blog-post))
                                            other-posts))]
        {:prelude "Want to read more? Here's the next entry in the series:"
         :post next-post}
        (when (seq other-posts)
          {:prelude "Did you miss out on the start? Here's the first entry in the series:"
           :post (first other-posts)}))
      (when-let [newest-post (last other-posts)]
        {:prelude "Here's the last entry in the series:"
         :post newest-post}))))

(defn render-series-conclusion [blog-post series]
  (let [blurb (get-series-blurb series)
        {:keys [prelude post]} (get-relevant-post blog-post series)]
    (when post
      [:div.section.slim
       [:div.content.info-section
        [:div.section-content.text-content
         (list
          [:p.mbl blurb " " prelude]
          (e/teaser (-> (prepare-blog-post-teaser post)
                        (dissoc :kicker))))]]])))

(defn render-blog-post [blog-post]
  (let [series (:blog-post/series blog-post)]
    (layout
     {:title (str (:page/title blog-post) " | magnars.com")}
     (e/header-section
      {:title "magnars" :href "/"})
     [:div.section
      [:div.content.text-content
       [:h1.h1
        (when series
          [:div.h4.mbxs [:a {:href (:page/uri series)} (:series/name series)] ": "])
        [:span (:page/title blog-post)]]
       (when-let [{:keys [blog url]} (:blog-post/original blog-post)]
         [:div.original-post
          [:a {:href url}
           [:div.norsk-flagg]
           [:div "På norsk på " blog "-bloggen" "."]]])
       (md/render-html (:blog-post/body blog-post))]]
     (when series
       (render-series-conclusion blog-post series)))))

(defn render-404 [_page]
  (layout {:title "Fant ikke siden!"} [:h1 "404 WAT"]))

(defn prepare-sequential-kicker [index teaser]
  (-> teaser
      (assoc :kicker (str "Part " (inc index) ":"))
      (dissoc :kicker-url)))

(defn render-series-page [series]
  (layout
   {:title (str (:series/name series) " | magnars.com")}
   (e/header-section
    {:title "magnars"
     :href "/"})
   [:div.section
    [:div.content.text-content.pbn
     [:h1.h1.mbm (:series/name series)]
     (when-let [description (:series/description series)]
       (md/render-html description))]]
   (e/teaser-section
    {:teasers (if (:series/sequential? series)
                (->> (:blog-post/_series series)
                     (sort-by :blog-post/published)
                     (map prepare-blog-post-teaser)
                     (map-indexed prepare-sequential-kicker))
                (->> (:blog-post/_series series)
                     (sort-by :blog-post/published)
                     (reverse)
                     (map prepare-blog-post-teaser)
                     (map #(dissoc % :kicker))))})))

(defn render-page [req page]
  (if-let [f (case (:page/kind page)
               :page.kind/frontpage render-frontpage
               :page.kind/blog-post render-blog-post
               :page.kind/series render-series-page
               :page.kind/rss-feed (fn [_] (rss/blog-post-feed (:app/db req)))
               nil)]
    (f page)
    (render-404 page)))

(comment

  (def system integrant.repl.state/system)

  (->> (d/db (:datomic/conn system))
       blog-posts/get-blog-posts
       (map #(into {:db/id (:db/id %)} %)))

  
)
