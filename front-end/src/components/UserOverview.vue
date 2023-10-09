<template>
  <div>
    <h2 class="mb-4">Users</h2>
    <table-component :amount-to-display="5" :table-data="users"></table-component>
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent";
import { users } from '@/models/user.js';

export default {
  components: {
    TableComponent,

  },
  data() {
    return {
      users,
    };
  },
};
</script>

<style scoped>
h2 {
  color: var(--color-primary);
}
</style>