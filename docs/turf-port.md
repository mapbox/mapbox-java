# Turf Port to Java

This document tracks the progress being made to port over all of the Turf functionality to Java. This is an on going project and funtions are being added once needed. If you'd like to contribute by adding a Turf function that's missing, please open a GitHub issue still with information relative to why you need this functionality.

Below's an on going list of the Turf functions which currently exist inside the `services-turf` module in this project:
  (Last updated 10/27/17)

## Measurement
- [x] turf-along
- [x] turf-bearing
- [x] turf-destination
- [x] turf-distance
- [x] turf-length
- [x] turf-midpoint
- [x] turf-bbox
- [ ] turf-rhumb-bearing
- [ ] turf-rhumb-destination
- [ ] turf-rhumb-distance
- [ ] turf-area
- [ ] turf-bbox-polygon
- [ ] turf-square
- [ ] turf-center-of-mass
- [ ] turf-center
- [ ] turf-centroid
- [ ] turf-envelope
- [ ] turf-point-on-surface
- [ ] turf-polygon-tangents
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
- [ ] turf-line-slice-along
- [x] turf-line-slice
- [ ] turf-line-split
- [ ] turf-mask
- [ ] turf-boolean-point-on-line
- [ ] turf-sector
- [ ] turf-unkink-polygon
- [x] turf-point-on-line

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
- [x] turf-inside
- [ ] turf-tag
- [x] turf-within
- [ ] turf-boolean-within

## Grids
- [ ] turf-hex-grid
- [ ] turf-point-grid
- [ ] turf-square-grid
- [ ] turf-triangle-grid

## Classification
- [x] turf-nearest

## Aggregation
- [ ] turf-collect
- [ ] turf-clusters-dbscan
- [ ] turf-clusters-kmeans

## Meta
- [x] coordAll
- [x] coordEach
- [ ] coordReduce

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
- [ ] bearingToAngle
- [ ] convertArea
- [x] convertDistance
- [ ] degrees2radians
- [x] distanceToRadians
- [x] distanceToDegrees
- [x] radiansToDistance
- [x] radians2degrees
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
