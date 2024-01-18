import {shallowMount} from "@vue/test-utils";
import AddUserModal from "@/components/modal/user/AddUserModal.vue";
import {TeamAdaptor} from "@/service/teamAdaptor";

const teams = [
    {
        id: 50,
        teamName: "Solar Sedum",
        warehouse: "Warehouse 1",
        type: "INTERNAL"
    },
    {
        id: 51,
        teamName: "Solarium",
        warehouse: "Warehouse 2",
        type: "INTERNAL"
    }
]
/**
 * Test for the AddUserModal component
 *
 * The test checks if all input fields exist, select options are correct.
 * It also checks if the validation methods work and if they get displayed.
 *
 * @author Noa de Greef
 */
describe('AddUserModal.vue', () => {
    const mockTeamAdaptor = new TeamAdaptor();
    jest.spyOn(mockTeamAdaptor, 'findAll').mockResolvedValue([
        {
            teams
        }])
    let wrapper;
    beforeEach(async () => {
        wrapper = shallowMount(AddUserModal, {
            global: {
                provide: {
                    teamsService: mockTeamAdaptor
                }
            }
        })
        await wrapper.vm.$nextTick();
    })

    it('add modal is rendered', () => {
        expect(wrapper.element.children.length, "Add modal should be rendered").toBeGreaterThan(0)
    })

    it("verify the existence of all add user fields", () => {
        // let inputFields = wrapper.findAll("input");
        //find element with an id of "user-name"
        let inputUsernameField = wrapper.find("#user-name");
        expect(inputUsernameField.exists(), "username input field does not exist").toBeTruthy();

        let selectTeam = wrapper.find("#team");
        expect(selectTeam.exists(), "team select does not exist").toBeTruthy();

        //find element with an id of "email"
        let inputEmailField = wrapper.find("#email");
        expect(inputEmailField.exists(), "email input field does not exist").toBeTruthy();

        //find element with an id of "password"
        let inputPasswordField = wrapper.find("#password");
        expect(inputPasswordField.exists(), "password input field does not exist").toBeTruthy();

        //find element with an id of "user-type-select"
        let selectType = wrapper.find("#user-type-select");
        expect(selectType.exists(), "type select does not exist").toBeTruthy();
    })

    it("the select options in the modal work correctly", async () => {
        //set the teams of the component
        wrapper.setData({teams: teams})
        //get the element with id of "team"
        let selectTeam = await wrapper.find("#team");
        //get the id value at position 1 because 0 should be the placeholder
        let firstRealOption = await selectTeam.findAll("option").at(1);
        expect(selectTeam.exists(), "team select does not exist").toBeTruthy();
        expect(parseInt(firstRealOption.element.value),
            "select option is different than the given teams").toBe(teams[0].id)

        //get the element with id of "team"
        let selectType = wrapper.find("#user-type-select");
        //get the id value at position 1
        let firstTypeOption = selectType.findAll("option").at(1);
        expect(selectType.exists(), "type select does not exist").toBeTruthy();
        expect(firstTypeOption.element.value, "type select options are different from expected values")
            .toBe("VIEWER");
    })

    it("validation works as expected and tells the user what went wrong", () => {
        let inputUsernameField = wrapper.find("#user-name");
        //set the value of the input
        inputUsernameField.setValue("");
        //call the validation method for name
        wrapper.vm.validateName();
        expect(wrapper.vm.nameEmpty, "validation message does not exist").toBeTruthy()

        //find element with an id of "email"
        let inputEmailField = wrapper.find("#email");
        //set the value as empty
        inputEmailField.setValue("");
        //validate the empty data
        wrapper.vm.validateEmail();

        expect(wrapper.vm.emailEmpty, "email empty validation message does not exist").toBeTruthy();
        //set value as something that isn't in the email format so that it triggers the validation message
        inputEmailField.setValue("admin");
        //trigger the validation method
        wrapper.vm.validateEmail();
        expect(wrapper.vm.emailValid, "email valid validation message does not exist").toBeTruthy();

        //find element with an id of "password"
        let inputPasswordField = wrapper.find("#password");
        //set the password value to empty
        inputPasswordField.setValue("");
        //call the password validation method
        wrapper.vm.validatePassword();
        expect(wrapper.vm.passwordEmpty, "password validation message does not exist").toBeTruthy();
    })
})
