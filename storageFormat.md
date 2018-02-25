Storage Format
============

{
	"distance-stats": {
		"on-foot": 16.32,
		"in-car": 123.45,
		"on-bus": 22.22,
		"on-bike": 1.2
	},
	"legs": {
		"<date>_<associated trip number>_<>": {
			"start": <unix timestamp, as long>,
			"means": "bike" <or "car", "bus", "foot">,
			"distance": <distance in miles>
		}
	},
	"trips": {
		"trip-<date>-<sequential number from zero for multiple trips in the same day>": <number of legs in that trip>
	}
}
