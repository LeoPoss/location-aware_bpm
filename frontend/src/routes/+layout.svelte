<script>
	import './styles.css';
	import '../app.css';

	import Footer from './Footer.svelte';
	import { Content, Header, SkipToContent } from 'carbon-components-svelte';

	let isSideNavOpen = false;
</script>

<Header bind:isSideNavOpen company="UR IoT" platformName="Location Application">
	<svelte:fragment slot="skip-to-content">
		<SkipToContent />
	</svelte:fragment>
</Header>
<Content>
	<div>
		<div>
			<slot />
		</div>
		<div class="h-10">
			<Footer />
		</div>
	</div>
</Content>
