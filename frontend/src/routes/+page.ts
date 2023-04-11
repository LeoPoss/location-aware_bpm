import type { PageLoad } from '../../.svelte-kit/types/src/routes/$types';

export const ssr = false;

export type CamundaTask = {
	id: string;
	name: string;
	processInstanceId: string;
	customerAddress: string;
	assignee: string;
	variables: Record<string, string>;
	distance: number;
	duration: number;
};

export type Actor = {
	id: string;
	longitude: number;
	latitude: number;
	isBusy: boolean;
};

export type Customer = {
	name: string;
	longitude: number;
	latitude: number;
	address: string;
};

export const load = (async ({ fetch }) => {
	const [tasksRes, actorsRes] = await Promise.all([
		fetch('http://localhost:8080/engine-rest/task'),
		fetch('http://127.0.0.1:1880/craftsmen')
	]);

	const taskPromises = (await tasksRes.json()).map(async (task: CamundaTask) => {
		const resp = await fetch(`http://localhost:8080/engine-rest/task/${task.id}/form-variables`);
		task.variables = Object.fromEntries(
			Object.entries(await resp.json()).map(([key, value]) => [key, value.value])
		);
		return task;
	});
	const tasks = (await Promise.all(taskPromises)) as CamundaTask[];

	const actors = (await actorsRes.json()).map((actor: any) => ({
		id: actor[0],
		...actor[1]
	})) as Actor[];
	return { tasks, actors };
}) satisfies PageLoad;
