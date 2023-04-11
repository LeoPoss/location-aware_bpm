<script>
  import { ToastNotification } from "carbon-components-svelte";

  export let notification = {};
  export let onRemove = null;

  const handleButtonClick = () => {
    onRemove();
  };
  console.log(notification);
</script>


<ToastNotification
  caption={new Date().toLocaleString()}
  kind={notification.type}
  subtitle="{notification.description ?? ''}"
  title="{notification.heading}"
/>