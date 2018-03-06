# Turf Port to Java

This document tracks the progress being made to port over all of the Turf functionality to Java. This is an on going project and funtions are being added once needed. If you'd like to contribute by adding a Turf function that's missing, please open a GitHub issue still with information relative to why you need this functionality.

Below's an on going list of the Turf functions which currently exist inside the `services-turf` module in this project:
  (Last updated 10/27/17)

## Measurement
- [x] turf-along
- [ ] turf-area
- [x] turf-bbox
- [ ] turf-bbox-polygon
- [x] turf-bearing
- [ ] turf-center
- [ ] turf-center-of-mass
- [ ] turf-centroid
- [x] turf-destination
- [x] turf-distance
- [ ] turf-envelope
- [x] turf-length
- [x] turf-midpoint
- [ ] turf-point-on-feature
- [ ] turf-polygon-tangents
- [ ] turf-point-to-line-distance
- [ ] turf-rhumb-bearing
- [ ] turf-rhumb-destination
- [ ] turf-rhumb-distance
- [ ] turf-square
- [ ] turf-great-circle

## Coordinate Mutation
- [ ] turf-flip
- [ ] turf-rewind
- [ ] turf-truncate
- ~~round~~ use Math.round() instead

## Transformation
- [ ] turf-bbox-clip
- [ ] turf-bezier
- [ ] turf-buffer
- [x] turf-circle
- [ ] turf-clone
- [ ] turf-concave
- [ ] turf-convex
- [ ] turf-difference
- [ ] turf-dissolve
- [ ] turf-intersect
- [ ] turf-line-offset
- [ ] turf-simplify
- [ ] turf-tesselate
- [ ] turf-transform-rotate
- [ ] turf-transform-scale
- [ ] turf-transform-translate
- [ ] turf-union

## Feature Conversion
- [ ] turf-combine
- [ ] turf-explode
- [ ] turf-flatten
- [ ] turf-linestring-to-polygon
- [ ] turf-polygonize
- [ ] turf-polygon-to-linestring

## Misc
- [ ] turf-kinks
- [ ] turf-line-arc
- [ ] turf-line-chunk
- [ ] turf-line-intersect
- [ ] turf-line-overlap
- [ ] turf-line-segment
- [x] turf-line-slice
- [ ] turf-line-slice-along
- [ ] turf-line-split
- [ ] turf-mask
- [x] turf-nearest-point-on-line
- [ ] turf-sector
- [ ] turf-shortest-parth
- [ ] turf-unkink-polygon

## Helper
The helper functions are all part of the GeoJson module; this class contains unit conversion and additional functionality to help other calculations.

## Data
- [ ] turf-random
- [ ] turf-sample

## Interpolation
- [ ] turf-interpolate
- [ ] turf-isobands
- [ ] turf-isolines
- [ ] turf-tin
- [ ] turf-planepoint
- [ ] turf-idw

## Joins
- [x] turf-inside // not in Turf
- [ ] turf-tag
- [x] turf-points-within-polygon

## Grids
- [ ] turf-hex-grid
- [ ] turf-point-grid
- [ ] turf-square-grid
- [ ] turf-triangle-grid

## Classification
- [x] turf-nearest-point

## Aggregation
- [ ] turf-collect
- [ ] turf-clusters-dbscan
- [ ] turf-clusters-kmeans

## Meta
- [x] coordAll
- [ ] coordEach
- [ ] coordReduce
- [ ] featureEach
- [ ] featureReduce
- [ ] flattenEach
- [ ] flattenReduce
- [ ] getCoord
- [ ] getCoords
- [ ] getGeom
- [ ] getType
- [ ] geomEach
- [ ] geomReduce
- [ ] propEach
- [ ] propReduce
- [ ] segmentEach
- [ ] segmentReduce
- [ ] getCluster
- [ ] clusterEach
- [ ] clusterReduce

## Assertions
All of these are already enforced in the GeoJson module
- [x] collectionOf
- [x] containsNumber
- [x] geojsonType
- [x] featureOf 

## Booleans
- [ ] turf-boolean-point-on-line
- [ ] turf-boolean-clockwise
- [ ] turf-boolean-contains
- [ ] turf-boolean-crosses
- [ ] turf-boolean-disjoint
- [ ] turf-boolean-overlap
- [ ] turf-boolean-equal
- [ ] turf-boolean-parallel

## Unit Conversion 
- [ ] bearingToAzimuth
- [ ] convertArea
- [x] convertLength
- [ ] degreesToRadians
- [x] lengthToRadians
- [x] lengthToDegrees
- [x] radiansToLength
- [x] radiansToDegrees
- [ ] toMercator
- [ ] toWgs84
- [ ] clusterEachCallback
- [ ] clusterReduceCallback
- [ ] removeEmptyPolygon
- [ ] ab
- [ ] coordEachCallback
- [ ] coordReduceCallback
- [ ] propEachCallback
- [ ] propReduceCallback
- [ ] featureEachCallback
- [ ] featureReduceCallback
- [ ] geomEachCallback
- [ ] geomReduceCallback
- [ ] flattenEachCallback
- [ ] flattenReduceCallback
- [ ] segmentEachCallback
- [ ] segmentReduceCallback

## Others not documented?
- [ ] turf-point-to-line-distance
- [ ] turf-nearest-point-to-line
- [ ] turf-shortest-path
- [ ] turf-voronoi
- [ ] turf-invariant
- [ ] turf-helpers
- [ ] turf-projection
- [ ] turf-clean-coords
