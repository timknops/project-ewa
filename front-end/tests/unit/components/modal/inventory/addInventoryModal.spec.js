import {mount} from "@vue/test-utils";
import AddInventoryModal from "@/components/modal/inventory/AddInventoryModal.vue";

describe('AddInventoryModal.vue', () => {
  let wrapper;
  beforeEach(async () => {
    wrapper = mount(AddInventoryModal, {
      props: {
        item: {
          warehouseId: 1,
          warehouseName: 'Solar Sedum',
          products : [
            {
              id: 1,
              name: 'Enphase IQ8+ omvormer',
              quantity: 18,
              minimum: 10
            },
            {
              id: 2,
              name: 'Enphase IQ7 omvormer',
              quantity: 18,
              minimum: 10
            }
          ]
        }
      }
    })
    await wrapper.vm.$nextTick()
  })

  it('renders the component', () => {
    expect(wrapper.element.children.length, "component should render").toBeGreaterThan(0)
  })

  it('item should correctly be converted to component data', () => {
    expect(wrapper.vm.activeWarehouseId, "warehouse id should be copied").toEqual(wrapper.vm.item.warehouseId)
    expect(wrapper.vm.products, "productList should be copied").toEqual(wrapper.vm.item.products)

    //the warehouse name is used in the warehouse input
    const warehouse = wrapper.find('#warehouse')
    expect(warehouse.element.value, "Warehouse input should have the correct warehouse name")
      .toEqual(wrapper.vm.item.warehouseName)
  })

  it('validation of product select works correctly', async () => {
    wrapper.vm.validateProduct()
    await wrapper.vm.$nextTick()
    expect(wrapper.vm.noProductSelectedError, "empty product should be true").toBeTruthy()

    const error = wrapper.find("p.text-danger")
    expect(error.text(), "error should be displayed").toBe("Please select a product!")

    const product = wrapper.find("#product")
    await product.setValue("1")  //select the first product

    wrapper.vm.validateProduct()
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.noProductSelectedError, "empty product should be false").toBeFalsy()
    const error1 = wrapper.find("p.text-danger")
    expect(error1.exists(), "error should not be displayed").toBeFalsy()
  });

  it( "error is displayed when quantity is empty", async () => {
    const input = wrapper.find("#quantity");
    await input.setValue("");
    wrapper.vm.validateQuantity();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.emptyQuantityError).toBeTruthy();
    const error = wrapper.find("p.text-danger");
    expect(error.exists(), "If error is empty error should exist").toBeTruthy();
    expect(error.text()).toBe("Quantity can not be empty");

    await input.setValue("10");
    wrapper.vm.validateQuantity();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.emptyQuantityError).toBeFalsy();
    const error1 = wrapper.find("p.text-danger");
    expect(error1.exists(), "Error should not exist if no error occurred").toBeFalsy();
  })

  it( "error is displayed when minimum is empty", async () => {
    const input = wrapper.find("#minimum");
    await input.setValue("-10");
    wrapper.vm.validateMinimum();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.negativeMinimum).toBeTruthy();
    const error = wrapper.find("p.text-danger");
    expect(error.exists(), "If error is empty error should exist").toBeTruthy();
    expect(error.text()).toBe("The minimum can't be a negative number");

    await input.setValue("10");
    wrapper.vm.validateMinimum();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.negativeMinimum).toBeFalsy();
    const error1 = wrapper.find("p.text-danger");
    expect(error1.exists(), "Error should not exist if no error occurred").toBeFalsy();
  })

  it('error is displayed if quantity is a decimal number', async () => {
    const input = wrapper.find("#quantity");
    input.setValue("10.5");
    wrapper.vm.validateQuantity();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.decimalError).toBeTruthy();
    const error = wrapper.find("p.text-danger");
    expect(error.text(), "If error is empty error should exist").toBe("Quantity should be a whole number");
  });

  it('hasError is computed correctly', async () => {
    wrapper.vm.noProductSelectedError = true
    wrapper.vm.emptyQuantityError = true
    wrapper.vm.negativeMinimum = true
    wrapper.vm.decimalError = true

    expect(wrapper.vm.hasError, "hasError should be true").toBeTruthy()

    //reset the errors
    const input = wrapper.find("#quantity");
    await input.setValue("10");

    const product = wrapper.find("#product");
    await product.setValue("1");

    const minimum = wrapper.find("#minimum");
    await minimum.setValue("10");

    expect(wrapper.vm.hasError, "hasError should be false").toBeFalsy()

  });
})