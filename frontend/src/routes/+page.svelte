<script lang="ts">
  import type { PageData } from "./$types";
  import LeafletMap from "./LeafletMap.svelte";
  import TaskTable from "./TaskTable.svelte";
  import { onMount } from "svelte";
  import StartProcess from "./StartProcess.svelte";
  import Notifications from "svelte-notifications";
  import CarbonNotification from "./CarbonNotification.svelte";

  export let data: PageData;

  let customers = data.tasks.map((task) => {
    return {
      name: task.variables.customerName,
      address: task.variables.customerAddress,
      latitude: parseFloat(task.variables.latitude),
      longitude: parseFloat(task.variables.longitude)
    };
  });

  let ownPosition: [number, number];
  onMount(async () => {
    ownPosition = await getCoords();
  });
  const getCoords = async () => {
    const pos = (await new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resolve, reject);
    })) as GeolocationPosition;

    return [pos.coords.longitude, pos.coords.latitude] as [number, number];
  };
</script>

<svelte:head>
  <title>Task overview</title>
  <meta content="Location Use Case" name="description" />
</svelte:head>
<Notifications item={CarbonNotification}>
  <section class="container mx-auto p-8 space-y-8">
    <h3>Start Process</h3>
    <StartProcess />
    <h3>Task overview</h3>
    <TaskTable {ownPosition} tasks={data.tasks} />
    <h3>Map</h3>
    <LeafletMap actors={data.actors} {customers} {ownPosition} />
  </section>
</Notifications>