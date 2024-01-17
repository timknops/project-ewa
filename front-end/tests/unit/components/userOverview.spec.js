import {shallowMount} from "@vue/test-utils";
import UserOverview from "@/components/UserOverview.vue";
import {UserAdaptor} from "@/service/userAdaptor";
import {SessionSbService} from "@/service/SessionSbService";

jest.mock('@/service/userAdaptor');

const teams = {
    id: 1,
    team: "Solar Sedum",
    warehouse: "Warehouse 1",
    type: "INTERNAL"
}

const users = [
    {
        id: 5,
        name: "admin2",
        email: "admin2@admin.com",
        password: "admin2",
        type: "ADMIN",
        team: teams
    },
    {
        id: 6,
        name: "testUser",
        email: "testUser@admin.com",
        password: "testUser",
        type: "VIEWER",
        team: teams
    }
]
/**
 * A unit test for the UserOverview component
 *
 * @author Noa de Greef
 */
describe('UserOverview.vue', () => {
    let wrapper;

    beforeEach(async () => {
        const mockUserAdaptor = new UserAdaptor();
        jest.spyOn(mockUserAdaptor, "asyncFindAll").mockResolvedValue([
            {
                users
            }
        ])


        const sessionService = new SessionSbService();
        sessionService.saveTokenInBrowserStorage('token', {
            id: 1,
            name: "admin",
        });


        wrapper = shallowMount(UserOverview, {
            global: {
                provide: {
                    userService: mockUserAdaptor,
                    sessionService: sessionService
                }
            }
        })

        await wrapper.vm.$nextTick();
    })

    it("User overview has been rendered", () => {
        expect(wrapper.element.children.length, "User overview has not been rendered").toBeGreaterThan(0);
    })

    it("table is correctly displayed", () => {
        const table = wrapper.findComponent({name: "TableComponent"});
        expect(table.exists(), "Table not rendered").toBeTruthy();
        expect(table.componentVM.hasSearchBar, "Table does not have a search bar").toBeTruthy();
    })

    it("user data is correctly formatted for the table", () => {
        //format the raw data for the table
        const usersFormatArray = [];
        usersFormatArray[0] = wrapper.vm.formatUserForTable(users[0]);
        usersFormatArray[1] = wrapper.vm.formatUserForTable(users[1]);
        //check if the formatted data is equal to the expected formatted value
        expect(usersFormatArray, "Raw dummy user data did not format as expected").toEqual([
            {
                id: 5,
                team: "Solar Sedum",
                email: "admin2@admin.com",
                name: "admin2",
                type: "ADMIN"
            },
            {
                id: 6,
                team: "Solar Sedum",
                email: "testUser@admin.com",
                name: "testUser",
                type: "VIEWER"
            }
        ])
    })

    //crud testing
    it("delete a user", async () => {
        //format the raw dummy user data to the table data
        const usersFormatArray = [];
        usersFormatArray[0] = wrapper.vm.formatUserForTable(users[0]);
        usersFormatArray[1] = wrapper.vm.formatUserForTable(users[1]);
        wrapper.vm.userList = usersFormatArray;

        //spy on the delete function
        const mockedDelete = jest.spyOn(wrapper.vm.userService, "asyncDelete")
            .mockResolvedValue(usersFormatArray[0]);

        //show delete modal
        await wrapper.vm.showDeleteModal(usersFormatArray[0])
        const modalWrapped = wrapper.findComponent({name: "ModalComponent"});
        expect(modalWrapped.exists(), "delete modal is not rendered").toBeTruthy();

        //simulate delete function
        await wrapper.vm.onUserDelete(usersFormatArray[0]);
        await wrapper.vm.$nextTick();

        //check if the async delete was called
        expect(mockedDelete, "user delete function was not called").toHaveBeenCalled();
        expect(wrapper.vm.userList.length, "user has not been deleted from userList").toEqual(1);

        //find the toast
        const toastWrapped = wrapper.findComponent({name: "ToastComponent"})
        //test the toast received to see if it's correct
        expect(toastWrapped.exists(), "toast is not rendered").toBeTruthy();
        expect(toastWrapped.vm.toastTitle, " Toast title is not correct")
            .toEqual("Deleted user");
        expect(toastWrapped.vm.toastMessage, "Toast message is not correct")
            .toEqual("Successfully deleted the user")
    })

    it("prevent the user from deleting their own account", async () => {
        //set userlist and active user
        const usersFormatArray = [];
        usersFormatArray[0] = wrapper.vm.formatUserForTable(users[0]);
        usersFormatArray[1] = wrapper.vm.formatUserForTable(users[1]);
        wrapper.vm.userList = usersFormatArray;
        wrapper.vm.activeUser = usersFormatArray[1]

        jest.spyOn(wrapper.vm.userService, "asyncDelete").mockResolvedValue(usersFormatArray[1]);

        //delete the same account as the active user
        await wrapper.vm.showDeleteModal(usersFormatArray[1]);
        await wrapper.vm.$nextTick();

        //find the toast
        const toastWrapped = wrapper.findComponent({name: "ToastComponent"})
        //test the toast received to see if it indeed gives the right toast message
        expect(toastWrapped.exists(), "toast is not rendered").toBeTruthy();
        expect(toastWrapped.vm.toastTitle, " Toast title is not correct")
            .toEqual("Can't delete user");
        expect(toastWrapped.vm.toastMessage, "Toast message is not correct")
            .toEqual("You can't delete your own account")

        //try to find the delete-modal which should not exist, because of the showDeleteModal method
        const deleteModal = wrapper.findComponent({name: "ModalComponent"})
        expect(deleteModal.exists(), "Delete modal is still rendered").toBeFalsy();
    })
})
