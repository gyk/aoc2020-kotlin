(ns make
  (:require [babashka.cli :as cli]
            [babashka.fs :as fs]
            [babashka.process :refer [shell]]
            [clojure.string :as str]))

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

(def out-dir "./out")

(defn parse-opts
  []
  (cli/parse-opts
    *command-line-args*
    {:spec     opt-spec
     ; Do not allow other options
     :restrict true}))

(defn build
  []
  (let [opts (parse-opts)
        _ (when (:help opts)
            (println (cli/format-opts {:spec opt-spec}))
            (System/exit 0))]

    (fs/create-dirs out-dir)

    (let [{:keys [day part]} opts
          src (format "day%02d_%d.kt" day part)]
      (println "Building Kotlin solution ...")
      (sh* ["kotlinc"
            "-cp"
            out-dir
            (fs/path "./src/main/kotlin" src)
            "-d"
            out-dir]))))

(defn run
  []
  (let [opts  (parse-opts)
        {:keys [day part]} opts
        class (format "Day%02d_%dKt" day part)]
    (sh* ["kotlin"
          "-cp"
          out-dir
          class])))
