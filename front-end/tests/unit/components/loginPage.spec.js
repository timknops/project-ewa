import {shallowMount} from '@vue/test-utils'
import LoginPage from "@/components/LoginPage.vue";
import {createRouter, createWebHistory} from "vue-router";
import loginPage from "@/components/LoginPage.vue";
import {SessionSbService} from "@/service/SessionSbService";
import Dashboard from "@/components/Dashboard.vue";
import loginResetPage from "@/components/LoginResetComponent.vue";

const routes = [
    {
        path: "/loginPage",
        component: loginPage,
        meta: {
            requiresLogin: false,
        }
    },
    {
        path: "/",
        redirect: "/loginPage"
    },
    {
        path: "/dashboard",
        component: Dashboard,
        meta: {
            icon: "fa-solid fa-house",
            requiresLogin: true,
        }
    },
    {
        path: "/loginReset/:email?",
        component: loginResetPage,
        meta: {
            requiresLogin: false,
        },
    }
]

const mockUserInputs = [
    {
        username1: null,
        password1: null
    },
    {
        username1: "admin",
        password1: "wrongPassword"
    },
    {
        username1: "admin",
        password1: "admin"
    },
    {
        username1: "user",
        password1: "user"
    }];

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

const LOGIN_BUTTON = "Login";
const FORGOT_PASSWORD = "Forgot password?";

/**
 * A unit test for the login page
 *
 * @author Noa de Greef
 */
describe('LoginPage.vue', () => {
    let wrapper;

    const sessionService = new SessionSbService();
    jest.spyOn(sessionService, "asyncLogin").mockResolvedValue(null);
    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: routes
        })

        wrapper = shallowMount(LoginPage,
            {
                global: {
                    provide: {
                        sessionService: sessionService
                    },
                    plugins: [router]
                }
            })

        await wrapper.vm.$nextTick();
    })

    it('Login page is rendered', () => {
        expect(wrapper.element.children.length, "Login page has not been rendered").toBeGreaterThan(0);
    })

    it('login function works as intended when no inputs are entered', async () => {

        //find all buttons on the loginPage, currently there's only one but this is built to scale for the future
        let foundButtons = wrapper.findAll("button");
        foundButtons = foundButtons.find(button => button.text() === LOGIN_BUTTON);
        expect(foundButtons.exists(), "Login button is not rendered").toBeTruthy();

        //simulate a click on the login button
        await foundButtons.trigger('click');
        //wait for the data to be ready
        await wrapper.vm.$nextTick;

        let errorMessage = wrapper.find(".error-message");
        expect(errorMessage.exists(), "Error message does not exist").toBeTruthy()
        expect(errorMessage.text(), "Error message is wrong").toMatch("One of the fields is empty");
    })

    it('login function works as intended with wrong login inputs', async () => {

        //find all buttons on the loginPage, currently there's only one but this is built to scale for the future
        let foundButtons = wrapper.findAll("button");
        foundButtons = foundButtons.find(button => button.text() === LOGIN_BUTTON);
        expect(foundButtons.exists(), "Login button is not rendered").toBeTruthy();

        //enters the inputs
        wrapper.setData({input: mockUserInputs[1]});

        //simulate a click on the login button
        await foundButtons.trigger('click');
        //wait for the data to be ready
        await wrapper.vm.$nextTick;

        let errorMessage = wrapper.find(".error-message");
        expect(errorMessage.exists(),
            "Error message about incorrect login details does not exist").toBeTruthy()
        expect(errorMessage.text(),
            "Error message about incorrect login details is wrong")
            .toMatch("Your login details are incorrect");
    })

    it('login function works as intended with correct login function', async () => {

        jest.spyOn(sessionService, "asyncLogin").mockResolvedValue(users[0]);

        //find all buttons on the loginPage, currently there's only one but this is built to scale for the future
        let foundButtons = wrapper.findAll("button");
        foundButtons = foundButtons.find(button => button.text() === LOGIN_BUTTON);
        expect(foundButtons.exists(), "Login button is not rendered").toBeTruthy();

        //enters the inputs
        wrapper.setData({input: mockUserInputs[2]});

        //simulate a click on the login button
        await foundButtons.trigger('click');

        //wait for the data to be ready
        await wrapper.vm.$nextTick;
        //wait for the router to be ready
        await wrapper.vm.$router.isReady();


        let errorMessage = wrapper.find(".error-message");
        expect(errorMessage.text(),
            "Error message about incorrect login details does not exist").toMatch("");
        expect(wrapper.vm.$route.path, "correctly logging in does not forward the router to the dashboard page")
            .toBe("/dashboard");
    })

    it("password reset navigates to the LoginResetComponent", async () => {
        let foundSmall = wrapper.findAll("small");
        foundSmall = foundSmall.find(small => small.text() === FORGOT_PASSWORD);
        expect(foundSmall.exists(), "Forgot password is not rendered").toBeTruthy();

        //click on forgot password
        await foundSmall.trigger("click");
        //wait for the data to be ready
        await wrapper.vm.$nextTick;
        //wait for the router to be ready
        await wrapper.vm.$router.isReady();

        expect(wrapper.vm.$route.path, "did not correctly navigate to the login reset component")
            .toBe("/loginReset")
    })
})
