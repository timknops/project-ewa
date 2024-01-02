import {shallowMount} from "@vue/test-utils";
import UpdateInventoryModal from "@/components/modal/inventory/UpdateInventoryModal.vue";

describe('UpdateInventoryModal.vue', () => {
  let wrapper;
  beforeEach(() => {
    wrapper = shallowMount(UpdateInventoryModal, {
      props: {
        item: {
          product: {
            id: 1,
            name: 'Enphase IQ8+ omvormer',
          },
          warehouse: {
            id: 1,
            name: 'Solar Sedum',
          },
          quantity: 18,
          minimum: 10
        }
      }
    })
  })

  it('renders the component', () => {
    expect(wrapper.element.children.length).toBeGreaterThan(0)
  })

  it('ModalItem should be the same as the prop', () => {
    expect(wrapper.vm.modalItem).toEqual(wrapper.vm.item)
  });

 it( "error is displayed when quantity is empty", async () => {
    const input = wrapper.find("#quantity");
    await input.setValue("");
    wrapper.vm.validateQuantity();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.emptyQuantity).toBeTruthy();
    const error = wrapper.find("p.text-danger");
    expect(error.exists(), "If error is empty error should exist").toBeTruthy();
    expect(error.text()).toBe("Quantity can not be empty");

    await input.setValue("10");
    wrapper.vm.validateQuantity();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.emptyQuantity).toBeFalsy();
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
})