<template>
  <div
    class="d-flex justify-content-center align-items-center"
    id="temp-container"
  >
    <TableComponent
      table-width="50vw"
      :amount-to-display="2"
      :bold-first-column="true"
      :table-data="sampleData"
    />
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent.vue";

export default {
  name: "App",
  components: { TableComponent },
  data() {
    return {
      sampleData: [
        {
          id: 1451,
          name: "name1",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 4343,
        },
        {
          id: 222,
          name: "name2name2name2",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 22,
        },
        {
          id: 123412,
          name: "name3",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 14,
        },
        {
          id: 2,
          name: "name4name2",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 14212,
        },
        {
          id: 2,
          name: "name4name2",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 14212,
        },
        {
          id: 2,
          name: "name4name2",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 14212,
        },
        {
          id: 2,
          name: "name4name2",
          description: "lorem impusmsdafsl;kdjf ;lksjdaf;lksdj",
          quantity: 14212,
        },
      ],
    };
  },
};
</script>

<style>
:root {
  --color-primary: #c7d02c;
  --color-secondary: #572700;
  --color-text: #333;
  --color-bg: #fff;
  --color-subtitle: #bfbfbf;
  --color-text-bg: #f8f8f8;

  --custom-box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05),
    0 4px 6px -4px rgb(0 0 0 / 0.05);
}

html,
body {
  font-family: "Montserrat", sans-serif;
  color: var(--color-text) !important;
  background-color: var(--bs-gray-100) !important;
  height: 100vh;
  width: 100vw;
}

#temp-container {
  height: 100vh;
}
</style>
