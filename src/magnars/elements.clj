(ns magnars.elements)

(defn teaser [{:keys [url title kicker kicker-url description kind published]}]
  [:div.teaser {:class kind}
   [:article.teaser-content
    [:h4.h4 {}
     (when kicker
       [:div.h6
        (if kicker-url
          [:a.unlink {:href kicker-url} kicker]
          kicker)])
     [:a {:href url} title]]
    [:div.teaser-body
     description]
    [:p [:span.byline.text-s published]]]])

(defn teaser-section [{:keys [teasers]}]
  [:div.section.teasers
   [:div.content
    [:div.section-content
     [:div.teaser-list (map teaser teasers)]]]])

(defn header-section [{:keys [title href]}]
  [:div.section.header
   [:div.content
    [:header.h2
     [:div.headshot]
     (if href
       [:a {:href href} title]
       title)]]])
