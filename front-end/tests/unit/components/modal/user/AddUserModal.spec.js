import {shallowMount} from "@vue/test-utils";
import AddUserModal from "@/components/modal/user/AddUserModal.vue";
import {TeamAdaptor} from "@/service/teamAdaptor";

const teams = {
    id: 1,
    teamName: "Solar Sedum",
    warehouse: "Warehouse 1",
    type: "INTERNAL"
}

const users = {
    name: "admin2",
    email: "admin2@admin.com",
    password: "admin2",
    type: "ADMIN",
    team: teams[0]
}

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
        console.log(users)
        await wrapper.vm.$nextTick();
    })

    it('Add modal is rendered', () => {
        expect(wrapper.element.children.length, "Add modal should be rendered").toBeGreaterThan(0)
    })
})
