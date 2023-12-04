<template>
  <div>
    <warehouse-header-display
        :has-no-total-option="true"
        :active-warehouse="activeWarehouse"
        :active-user="activeUser"
        @setActiveWarehouse="setActiveWarehouse"
    />
  </div>

</template>

<script>
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";

export default {
  name: "orderOverview",
  components: {WarehouseHeaderDisplay},
  data() {
    return {
      activeWarehouse: {},
      activeUser: {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: {
            id: 1003,
            name: "Superzon",
          },
        },
      },
      orders: []
    }
  },
  methods: {
    setActiveWarehouse(warehouse) {
      this.activeWarehouse = warehouse
      this.$router.push("/orders/" + warehouse.name)
    }
  },

}
</script>


<style scoped>

</style>