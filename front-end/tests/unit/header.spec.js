import {shallowMount} from '@vue/test-utils'
import Header from '@/components/HeaderComponent.vue'

/**
 * @jest-environment jsdom
 * @test {Header}
 *
 * Test the Header component with a mock router and route.
 * @author Julian Kruithof
 */
describe('HelloWorld.vue', () => {

  let wrapper;
  beforeEach(() => {
    const mockRouter = {
      push: jest.fn()
    }
    const mockRoute = {
      path: '/dashboard',
      meta: {
        icon: 'fa-solid fa-house',
        requiresLogin: true
      },
      matched: [
        {
          path: '/dashboard',
          meta: {
            icon: 'fa-solid fa-house',
            requiresLogin: true
          }
        }
      ]
    }
    wrapper = shallowMount(Header,
      {
        global: {
          mocks: {
            $router: mockRouter,
            $route: mockRoute
          }
        }
      })
  })

  it("Header should render properly", () => {
    expect(wrapper.element.children.length, "Header is not rendered properly").toBeGreaterThan(0);
  })

  it('Header should contain path name', () => {
    const h2 = wrapper.find('h2');
    expect(h2.text(), "Route path should be displayed in header").toMatch('Dashboard');
  })

  it('Header should contain icon', () => {
    const icon = wrapper.findComponent({name: 'FontAwesomeIcon'});
    expect(icon.vm.icon, "Icon should contain the meta info about the icon").toContain('fa-solid fa-house');
  })
})
