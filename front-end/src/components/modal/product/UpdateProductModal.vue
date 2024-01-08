<template>
  <form>
    <div class="mb-3">
      <label for="id" class="form-label fw-bold">ID</label>
      <input id="id" type="text" class="form-control" :value="modalItem.id" disabled>
    </div>
    <div class="mb-3">
      <label for="product-name" class="form-label fw-bold">Product name</label>
      <input id="product-name"
             type="text"
             class="form-control"
             :class="{'border-danger': nameEmpty}"
             v-model.lazy.trim="modalItem.productName"
             @blur="validateName">
      <p v-if="nameEmpty" class="text-danger"> The name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label fw-bold">description</label>
      <textarea id="description"
                class="form-control"
                :class="{'border-danger': descriptionEmpty}"
                v-model.lazy.trim="modalItem.description"
                @blur="validateDescription">
      </textarea>
      <p v-if="descriptionEmpty" class="text-danger"> The description can't be empty!</p>
    </div>
  </form>
</template>

<script>
/**
 * Modal for updating a product
 *
 * @author Julian Kruithof
 */
export default {
  name: "UpdateProductModal",
  data() {
    return {
      modalItem: {},
      nameEmpty: false, //specific error check for correct response to user
      descriptionEmpty: false
    }
  },

  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item)
  },

  computed: {
    hasError() {
      this.validateName();
      this.validateDescription();
      return this.nameEmpty || this.descriptionEmpty;
    }
  },

  methods: {
    /**
     * check if a name is empty
     */
    validateName() {
      this.nameEmpty = this.modalItem.productName.length === 0;
    },

    /**
     * check if description is empty
     */
    validateDescription() {
      this.descriptionEmpty = this.modalItem.description.length === 0;
    }
  }
}
</script>


<style scoped>

</style>
