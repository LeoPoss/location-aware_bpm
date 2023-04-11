<script lang="ts">
	import {
		Button,
		Form,
		FormGroup,
		NumberInput,
		RadioButton,
		RadioButtonGroup,
		TextInput,
		Tile
	} from 'carbon-components-svelte';

	import { getNotificationsContext } from 'svelte-notifications';

	const { addNotification } = getNotificationsContext();

	async function geocodeAddress(address: string) {
		const response = await fetch(
			'https://nominatim.openstreetmap.org/search?q=' +
				address +
				'&format=json&polygon=1&addressdetails=1'
		);
		const data = await response.json();
		return [data[0].lat, data[0].lon];
	}

	async function reverseGeocode(lat: string, lon: string) {
		const response = await fetch(
			'https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + lat + '&lon=' + lon
		);
		const data = await response.json();
		return data.display_name;
	}

	async function onFormSubmit() {
		switch (true) {
			case customerId ||
				customerName === '' ||
				taskType === '' ||
				(lat === '' && lon === '' && address === ''):
				break;
			case address !== '' && (lat === '' || lon === ''): {
				let pos = await geocodeAddress(address);
				lat = pos[0];
				lon = pos[1];
				console.log('Customer Location: ' + lat + ', ' + lon);
				break;
			}
			case address === '' && lat !== '' && lon !== '':
				address = await reverseGeocode(lat, lon);
				console.log('Customer Address: ' + address);
				break;
			default:
				break;
		}

		fetch('http://localhost:8080/engine-rest/process-definition/key/location/start', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				variables: {
					customerId: {
						value: customerId,
						type: 'String'
					},
					customerName: {
						value: customerName,
						type: 'String'
					},
					contractType: {
						value: taskType,
						type: 'String'
					},
					latitude: {
						value: lat,
						type: 'String'
					},
					longitude: {
						value: lon,
						type: 'String'
					},
					customerAddress: {
						value: address,
						type: 'String'
					}
				}
			})
		})
			.then((response) => {
				if (response.ok) {
					return response.json();
				} else {
					throw new Error('Something went wrong');
				}
			})
			.then((data) => {
				console.log('Success:', data);
				addNotification({
					heading: 'Sucessfully created process instance',
					type: 'success',
				});
			})
			.catch((error) => {
				console.error('Error:', error);
				alert('Error: ' + error);
			});
	}

	let customerId = 1;
	let customerName = 'Tina Tester';
	let taskType = 'normal';
	let lat = '48.999189';
	let lon = '12.093240';
	let address = 'Universitätsstraße 31, 93053 Regensburg';
</script>

<section>
	<span class="text-slate-500 text-xs"
		>You can provide either an address or both, latitude and longitude &rarr; if both are provided,
		it will used as single source of truth (no cross-checking if lat, long match the given address)</span
	>
	<Tile class="mt-3">
		<Form class="grid grid-cols-2 gap-3" on:submit={onFormSubmit}>
			<NumberInput
				bind:value={customerId}
				helperText="Must match known customerId from NodeRED"
				label="CustomerId"
				min={1}
				placeholder="Enter customerId..."
			/>
			<TextInput
				bind:value={customerName}
				labelText="Customer Name"
				placeholder="Enter user name..."
			/>
			<FormGroup class="col-span-2">
				<RadioButtonGroup bind:selected={taskType} legendText="Task type">
					<RadioButton labelText="Normal" value="normal" />
					<RadioButton labelText="Emergency" value="emergency" />
				</RadioButtonGroup>
			</FormGroup>
			<TextInput bind:value={lat} labelText="Latitude" placeholder="Enter customer latitude..." />
			<TextInput bind:value={lon} labelText="Longitude" placeholder="Enter customer longitude..." />
			<span class="col-span-2">or</span>
			<TextInput
				bind:value={address}
				class="col-span-2"
				labelText="Address"
				placeholder="Enter customer address..."
			/>
			<Button class="col-span-2" type="submit">Create Process</Button>
		</Form>
	</Tile>
</section>
