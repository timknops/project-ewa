<template>
  <div>
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

</style>