import {shallowMount} from '@vue/test-utils'
import LoginPage from "@/components/LoginPage.vue";
import {createRouter, createWebHistory} from "vue-router";
import loginPage from "@/components/LoginPage.vue";
import {SessionSbService} from "@/service/SessionSbService";

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

describe('LoginPage.vue', () => {
    let wrapper;

    beforeEach(async () => {
        const router = createRouter({
            history: createWebHistory(),
            routes: routes
        })

        const sessionService = new SessionSbService();
        sessionService.saveTokenInBrowserStorage('token', {
            name: 'admin',
            password: 'admin'
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

    it('login function works as expected', async () => {

        //find the error message
        const errorMessage = wrapper.find("#error-message");
        expect(errorMessage.text(), "Empty input error message should be visible")
            .toEqual("One of the fields is empty");
    })

    console.log(mockUserInputs)
})
