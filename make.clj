(ns make
  (:require [babashka.cli :as cli]
            [babashka.fs :as fs]
            [babashka.process :refer [shell]]
            [clojure.string :as str]))

(defn env [s] (System/getenv s))

(def windows?
  (-> (System/getProperty "os.name")
      (str/lower-case)
      (str/starts-with? "win")))

(defn sh*
  ([args] (sh* nil args))
  ([opts args]
   (apply shell
          opts
          (concat
           (if windows? ["cmd" "/c"] [])
           (if (sequential? args) args [args])))))
(when (not= (.getEncoding *out*) "UTF8")
  (alter-var-root (var *out*)
                  (constantly (java.io.OutputStreamWriter.
                               (System/out)
                               "UTF8"))))

(def opt-spec
  {:day     {:ref   "<day>"
             :alias :d}
   :part    {:ref   "<part>"
             :alias :p}
   :help    {:desc "Print this message"}
   :dry-run {}})

(def thinking-cloud "\uD83D\uDCAD")

(defn build
  [kotlinc-root]
  (let [kotlinc (str (fs/path kotlinc-root "bin/kotlinc"))
        kotlin-stdlib
        (str (fs/path kotlinc-root "lib/kotlin-stdlib.jar"))
        opts (cli/parse-opts
              *command-line-args*
              {:spec     opt-spec
                ; Do not allow other options
               :restrict true})
        _ (when (:help opts)
            (println (cli/format-opts {:spec opt-spec}))
            (System/exit 0))
        sh* (if (contains? opts :dry-run)
                (fn [& args]
                  (apply println thinking-cloud args))
                sh*)]

    (println "Opts:" opts)

    (let [src1 (format "day%02d_1.kt" (:day opts))
          src2 (format "day%02d_2.kt" (:day opts))
          jar1 (format "day%02d_1.jar" (:day opts))
          jar2 (format "day%02d_2.jar" (:day opts))
          class1 (format "Day%02d_1Kt" (:day opts))
          class2 (format "Day%02d_2Kt" (:day opts))]
      ; kotlinc -cp day09_1.jar  .\day09_2.kt  -d day09_2.jar
     (println "Building Kotlin solution ...")
     (sh* {:dir "./src/main/kotlin"}
      [kotlinc
       src1
       "-d"
       jar1])

    ; java -cp 'C:\Users\gyk\bin\kotlinc\lib\kotlin-stdlib.jar;.\day09_1.jar;.\day09_2.jar'
    ; Day09_2Kt
     (sh* {:dir "./src/main/kotlin"}
          ["java"
           "-cp"
           (format "'%s;%s;%s'"
                   kotlin-stdlib
                   jar1
                   jar2)
           class2])
     #_(shell kotlinc (format "day%02d.jar" (:day opts))))))




