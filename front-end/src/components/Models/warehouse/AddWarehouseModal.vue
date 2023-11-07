<template>
  <form>
    <div class="mb-3">
      <label for="warehouse-name" class="form-label fw-bold">Warehouse name</label>
      <input id="warehouse-name"
             type="text"
             class="form-control"
             :class="{'border-danger': nameEmpty}"
             v-model.lazy.trim="modalItem.name"
             @blur="validateName">
      <p v-if="nameEmpty" class="text-danger"> The name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="location" class="form-label fw-bold">location</label>
      <textarea id="location"
                class="form-control"
                :class="{'border-danger': locationEmpty}"
                v-model.lazy.trim="modalItem.location"
                @blur="validateLocation">
      </textarea>
      <p v-if="locationEmpty" class="text-danger"> The location can't be empty!</p>
    </div>
  </form>
</template>

<script>
export default {
  name: "AddWarehouseModal",
  data() {
    return {
      modalItem:{
        id: 0,
        name: "",
        location: "",
      },
      hasError: false,
      nameEmpty: false,
      locationEmpty: false,
    }
  },
  methods: {
    validateName() {
      this.nameEmpty = this.modalItem.name.length === 0;
      this.hasError = this.modalItem.name.length === 0;
    },
    validateLocation() {
      this.locationEmpty = this.modalItem.location.length === 0;
      this.hasError = this.modalItem.location.length === 0;
    }
  }
}
</script>

<style scoped>

</style>