#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# WindowSpec.R - WindowSpec class and methods implemented in S4 OO classes

#' @include generics.R jobj.R column.R
NULL

#' S4 class that represents a WindowSpec
#'
#' WindowSpec can be created by using windowPartitionBy() or windowOrderBy()
#'
#' @rdname WindowSpec
#' @seealso \link{windowPartitionBy}, \link{windowOrderBy}
#'
#' @param sws A Java object reference to the backing Scala WindowSpec
#' @note WindowSpec since 2.0.0
setClass("WindowSpec",
         slots = list(sws = "jobj"))

setMethod("initialize", "WindowSpec", function(.Object, sws) {
  .Object@sws <- sws
  .Object
})

windowSpec <- function(sws) {
  stopifnot(class(sws) == "jobj")
  new("WindowSpec", sws)
}

#' @rdname show
#' @note show(WindowSpec) since 2.0.0
setMethod("show", "WindowSpec",
          function(object) {
            cat("WindowSpec", callJMethod(object@sws, "toString"), "\n")
          })

#' partitionBy
#'
#' Defines the partitioning columns in a WindowSpec.
#'
#' @param x a WindowSpec.
#' @param col a column to partition on (desribed by the name or Column).
#' @param ... additional column(s) to partition on.
#' @return A WindowSpec.
#' @rdname partitionBy
#' @name partitionBy
#' @aliases partitionBy,WindowSpec-method
#' @family windowspec_method
#' @examples
#' \dontrun{
#'   partitionBy(ws, "col1", "col2")
#'   partitionBy(ws, df$col1, df$col2)
#' }
#' @note partitionBy(WindowSpec) since 2.0.0
setMethod("partitionBy",
          signature(x = "WindowSpec"),
          function(x, col, ...) {
            stopifnot(class(col) %in% c("character", "Column"))

            if (class(col) == "character") {
              windowSpec(callJMethod(x@sws, "partitionBy", col, list(...)))
            } else {
              jcols <- lapply(list(col, ...), function(c) {
                c@jc
              })
              windowSpec(callJMethod(x@sws, "partitionBy", jcols))
            }
          })

#' Ordering Columns in a WindowSpec
#'
#' Defines the ordering columns in a WindowSpec.
#' @param x a WindowSpec
#' @param col a character or Column indicating an ordering column
#' @param ... additional sorting fields
#' @return A WindowSpec.
#' @name orderBy
#' @rdname orderBy
#' @aliases orderBy,WindowSpec,character-method
#' @family windowspec_method
#' @seealso See \link{arrange} for use in sorting a SparkDataFrame
#' @examples
#' \dontrun{
#'   orderBy(ws, "col1", "col2")
#'   orderBy(ws, df$col1, df$col2)
#' }
#' @note orderBy(WindowSpec, character) since 2.0.0
setMethod("orderBy",
          signature(x = "WindowSpec", col = "character"),
          function(x, col, ...) {
            windowSpec(callJMethod(x@sws, "orderBy", col, list(...)))
          })

#' @rdname orderBy
#' @name orderBy
#' @aliases orderBy,WindowSpec,Column-method
#' @note orderBy(WindowSpec, Column) since 2.0.0
setMethod("orderBy",
          signature(x = "WindowSpec", col = "Column"),
          function(x, col, ...) {
            jcols <- lapply(list(col, ...), function(c) {
              c@jc
            })
            windowSpec(callJMethod(x@sws, "orderBy", jcols))
          })

#' rowsBetween
#'
#' Defines the frame boundaries, from \code{start} (inclusive) to \code{end} (inclusive).
#'
#' Both \code{start} and \code{end} are relative positions from the current row. For example,
#' "0" means "current row", while "-1" means the row before the current row, and "5" means the
#' fifth row after the current row.
#'
#' @param x a WindowSpec
#' @param start boundary start, inclusive.
#'              The frame is unbounded if this is the minimum long value.
#' @param end boundary end, inclusive.
#'            The frame is unbounded if this is the maximum long value.
#' @return a WindowSpec
#' @rdname rowsBetween
#' @aliases rowsBetween,WindowSpec,numeric,numeric-method
#' @name rowsBetween
#' @family windowspec_method
#' @examples
#' \dontrun{
#'   rowsBetween(ws, 0, 3)
#' }
#' @note rowsBetween since 2.0.0
setMethod("rowsBetween",
          signature(x = "WindowSpec", start = "numeric", end = "numeric"),
          function(x, start, end) {
            # "start" and "end" should be long, due to serde limitation,
            # limit "start" and "end" as integer now
            windowSpec(callJMethod(x@sws, "rowsBetween", as.integer(start), as.integer(end)))
          })

#' rangeBetween
#'
#' Defines the frame boundaries, from \code{start} (inclusive) to \code{end} (inclusive).
#'
#' Both \code{start} and \code{end} are relative from the current row. For example, "0" means
#' "current row", while "-1" means one off before the current row, and "5" means the five off
#' after the current row.
#'
#' @param x a WindowSpec
#' @param start boundary start, inclusive.
#'              The frame is unbounded if this is the minimum long value.
#' @param end boundary end, inclusive.
#'            The frame is unbounded if this is the maximum long value.
#' @return a WindowSpec
#' @rdname rangeBetween
#' @aliases rangeBetween,WindowSpec,numeric,numeric-method
#' @name rangeBetween
#' @family windowspec_method
#' @examples
#' \dontrun{
#'   rangeBetween(ws, 0, 3)
#' }
#' @note rangeBetween since 2.0.0
setMethod("rangeBetween",
          signature(x = "WindowSpec", start = "numeric", end = "numeric"),
          function(x, start, end) {
            # "start" and "end" should be long, due to serde limitation,
            # limit "start" and "end" as integer now
            windowSpec(callJMethod(x@sws, "rangeBetween", as.integer(start), as.integer(end)))
          })

# Note that over is a method of Column class, but it is placed here to
# avoid Roxygen circular-dependency between class Column and WindowSpec.

#' over
#'
#' Define a windowing column.
#'
#' @param x a Column, usually one returned by window function(s).
#' @param window a WindowSpec object. Can be created by \code{windowPartitionBy} or
#'        \code{windowOrderBy} and configured by other WindowSpec methods.
#' @rdname over
#' @name over
#' @aliases over,Column,WindowSpec-method
#' @family colum_func
#' @examples
#' \dontrun{
#'   df <- createDataFrame(mtcars)
#'
#'   # Partition by am (transmission) and order by hp (horsepower)
#'   ws <- orderBy(windowPartitionBy("am"), "hp")
#'
#'   # Rank on hp within each partition
#'   out <- select(df, over(rank(), ws), df$hp, df$am)
#'
#'   # Lag mpg values by 1 row on the partition-and-ordered table
#'   out <- select(df, over(lead(df$mpg), ws), df$mpg, df$hp, df$am)
#' }
#' @note over since 2.0.0
setMethod("over",
          signature(x = "Column", window = "WindowSpec"),
          function(x, window) {
            column(callJMethod(x@jc, "over", window@sws))
          })
