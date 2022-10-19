<template>
  <div class="container content-field">
    <div class="card">
      <div class="card-body">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script scoped>
  export default {
      name: "ContentField",
  };
</script>
      
<style scoped>
  .content-field {
    margin-top: 25px;
  }
</style>