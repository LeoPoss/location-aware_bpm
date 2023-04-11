<script lang="ts">
  import { invalidateAll } from "$app/navigation";
  import haversine from "haversine";
  import { onMount } from "svelte";
  import type { CamundaTask } from "./+page";
  import { Button, Form, InlineLoading, NumberInput, Tag, Tooltip } from "carbon-components-svelte";
  import { getNotificationsContext } from "svelte-notifications";

  export let tasks: CamundaTask[];
  export let ownPosition: [number, number];

  const { addNotification } = getNotificationsContext();

  // Because the API key has a usage quota, it will not be published, but you can get your own key here: https://openrouteservice.org/dev/#/home
  const openrouteserviceAPI = import.meta.env.VITE_OPENROUTESERVICE_API;

  async function getDrivingDuration(from: [number, number], to: [number, number]) {
    if (!openrouteserviceAPI) return -1;
    const res = await fetch(
      `https://api.openrouteservice.org/v2/directions/driving-car?api_key=${openrouteserviceAPI}&start=${from[0]},${from[1]}&end=${to[0]},${to[1]}`
    );
    const durationInSeconds = (await res.json()).features[0].properties.summary.duration;
    return Math.round(durationInSeconds / 60);
  }

  async function mergeAndSortTasks() {
    if (ownPosition) {
      tasks = await Promise.all(
        tasks.map(async (task) => {
          const distance = Number(
            haversine(
              ownPosition,
              [parseFloat(task.variables.longitude), parseFloat(task.variables.latitude)],
              { format: "[lon,lat]" }
            )
              .valueOf()
              .toFixed(2)
          );
          const duration = await getDrivingDuration(ownPosition, [
            parseFloat(task.variables.longitude),
            parseFloat(task.variables.latitude)
          ]);
          return { ...task, distance, duration };
        })
      );
      tasks.sort((a, b) => a.distance - b.distance);
      tasks.sort((a, b) => a.duration - b.duration);
    }
  }

  onMount(async () => {
    while (!ownPosition) {
      await new Promise((resolve) => setTimeout(resolve, 1000));
    }
    await mergeAndSortTasks();
  });

  async function claimTask(taskId: string) {
    await fetch(`http://localhost:8080/engine-rest/task/${taskId}/claim`, {
      method: "POST",
      body: JSON.stringify({
        userId: "c1"
      }),
      headers: {
        "content-type": "application/json"
      }
    });
    addNotification({
      position: 'bottom-right',
      heading: "Successfully claimed task!",
      type: "success"
    });
    await invalidateAll();
    await mergeAndSortTasks();
  }

  async function unclaimTask(taskId: string) {
    await fetch(`http://localhost:8080/engine-rest/task/${taskId}/unclaim`, {
      method: "POST"
    });
    addNotification({
      position: 'bottom-right',
      heading: "Successfully unclaimed task!",
      type: "success"
    });
    await invalidateAll();
    await mergeAndSortTasks();
  }

  async function completeTask(taskId: string) {
    await fetch(`http://localhost:8080/engine-rest/task/${taskId}/complete`, {
      method: "POST",
      headers: {
        "content-type": "application/json"
      }
    });
    addNotification({
      position: 'bottom-right',
      heading: "Successfully finished task!",
      type: "success"
    });
    await invalidateAll();
    await mergeAndSortTasks();
  }

  function moveActor() {
    fetch(`http://localhost:1880/move-actor`, {
      method: "POST",
      body: JSON.stringify({
        customerId
      }),
      headers: {
        "content-type": "application/json"
      }
    });
    addNotification({
      position: 'bottom-right',
      heading: "Moved actor!",
      type: "success"
    });
  }

  let customerId = 1;
</script>

<div class="container mx-auto">
  <Form on:submit={moveActor}>
    <NumberInput
      bind:value={customerId}
      helperText="Must match known customerId from NodeRED"
      label="CustomerId"
      min={1}
      placeholder="Enter customerId..."
    />
    <Button type="submit">Move assigned Actor</Button>
  </Form>
  <p class="text-xs text-slate-500">
    More process instances are available in the <a
    href="http://localhost:8080/camunda/app/cockpit/default/#/process-instance">Camunda Cockpit</a
  >. If you try to create more processes than actors, as seen in the process, the instance will be
    evaluated and instantly ends with notifying the customer about being unable to process the
    request right now. <br>
    Distances are based from your current location, which is determined by your browser.
  </p>
  <table class="bx--data-table mt-3">
    <thead>
    <tr>
      <th>Task title</th>
      <th>Assignee</th>
      <th>CustomerId</th>
      <th>Process instance</th>
      <th>Location</th>
      <th>Distance</th>
      <th class="table-sort-dsc">Driving Duration</th>
      <th />
      <th />
    </tr>
    </thead>
    <tbody>
    {#each tasks as task, i}
      <tr>
        <td>{task.name} <span class="text-xs">({task.id})</span></td>
        <td>{task.assignee ?? '-'}</td>
        <td>{task.variables.customerId}</td>
        <td>{task.processInstanceId}</td>
        <td>{task.variables.customerAddress} </td>
        <td>
          {#if !task.distance}
            <InlineLoading />
          {:else}
            {task.distance} km
          {/if}
        </td>
        <td>
          {#if !task.duration}
            <InlineLoading />
          {:else if task.duration === -1}
            <Tooltip triggerText="No duration">
              <p>
                You probably did not add an API-Key for api.openrouteservice.org to the .env-file
              </p>
            </Tooltip>
          {:else if task.duration < 5}
            <Tag type="green">{task.duration} min</Tag>
          {:else if task.duration < 10}
            <Tag type="blue">{task.duration} min</Tag>
          {:else if task.duration < 30}
            <Tag type="red">{task.duration} min</Tag>
          {/if}
        </td>
        <td style="padding: 0 !important;">
          {#if !task.assignee}
            <Button class="w-full" on:click={() => claimTask(task.id)}>Claim</Button>
          {:else}
            <Button class="w-full" kind="secondary" on:click={() => unclaimTask(task.id)}
            >Unclaim
            </Button>
          {/if}
        </td>
        <td style="padding: 0 !important;">
          <Button class="w-full" on:click={() => completeTask(task.id)}>Complete</Button>
        </td>
      </tr>
    {/each}
    </tbody>
  </table>
</div>
