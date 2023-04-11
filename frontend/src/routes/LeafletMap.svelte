<script lang="ts">
	import { onDestroy, onMount } from 'svelte';
	import { browser } from '$app/environment';
	import L from 'leaflet';
	import 'l.movemarker';
	import type { Actor, Customer } from './+page';

	export let actors: Actor[];
	export let customers: Customer[];

	export let ownPosition: number[];

	let mapElement: HTMLDivElement;
	let map: L.Map;
	onMount(async () => {
		if (browser) {
			const actorIcon = L.divIcon({
				html: `<svg xmlns="http://www.w3.org/2000/svg" height="48" width="48"><path d="M11.2 39.95q-2.45 0-4.175-1.725Q5.3 36.5 5.3 34.05H2V11q0-1.2.9-2.1Q3.8 8 5 8h28.95v8.35h5.25L46 25.4v8.65h-3.55q0 2.45-1.725 4.175Q39 39.95 36.55 39.95q-2.45 0-4.175-1.725Q30.65 36.5 30.65 34.05H17.1q0 2.45-1.725 4.175Q13.65 39.95 11.2 39.95Zm0-3q1.2 0 2.05-.85.85-.85.85-2.05 0-1.2-.85-2.05-.85-.85-2.05-.85-1.2 0-2.05.85-.85.85-.85 2.05 0 1.2.85 2.05.85.85 2.05.85Zm25.35 0q1.2 0 2.05-.85.85-.85.85-2.05 0-1.2-.85-2.05-.85-.85-2.05-.85-1.2 0-2.05.85-.85.85-.85 2.05 0 1.2.85 2.05.85.85 2.05.85Zm-2.6-10.2h9.3l-5.55-7.4h-3.75Z"/></svg>`,
				className: '',
				iconSize: [16, 32],
				iconAnchor: [8, 32]
			});

			const selfIcon = L.divIcon({
				html: `
<svg xmlns="http://www.w3.org/2000/svg" height="48" width="48"><path d="M24 44q-8.05-6.85-12.025-12.725Q8 25.4 8 20.4q0-7.5 4.825-11.95Q17.65 4 24 4q6.35 0 11.175 4.45Q40 12.9 40 20.4q0 5-3.975 10.875T24 44Zm-5.5-18h3v-6h5v6h3v-9.25L24 13.1l-5.5 3.65Z"/></svg>`,
				className: '',
				iconSize: [16, 32],
				iconAnchor: [8, 32]
			});

			map = L.map(mapElement).setView([49.01, 12.08], 13);

			L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
				attribution:
					'&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
			}).addTo(map);

			actors.map(({ latitude, longitude, id }) =>
				L.marker([latitude, longitude], { icon: actorIcon }).bindPopup(id).addTo(map)
			);
			customers.map(({ latitude, longitude, name }) =>
				L.marker([latitude, longitude]).bindPopup(name).addTo(map)
			);

			ownPosition;

			if (ownPosition) {
				L.marker([ownPosition[1], ownPosition[0]], { icon: selfIcon }).bindPopup('You').addTo(map);
			}
		}

		// for (let a of actors) {
		//   L.moveMarker(
		//     [
		//       [a.latitude - 0.005, a.longitude - 0.005],
		//       [a.latitude, a.longitude]
		//     ],
		//     5000
		//   ).addTo(map);
		// }
	});

	onDestroy(async () => {
		if (map) {
			console.log('Unloading Leaflet map.');
			map.remove();
		}
	});
</script>

<section>
	<p class="text-xs text-slate-500">
		The map shows the current location of all actors and customers.
	</p>
	<div bind:this={mapElement} class="h-[500px] mt-3" />
</section>

<style>
	@import 'leaflet/dist/leaflet.css';
</style>
