import user from '../fixtures/user.json';

describe('me spec', () => {
    it('User information for simple user', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: false
            },
        })

        cy.intercept('GET', '/api/session', []).as('sessions')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        //cy.contains('button', 'Submit').click()
        //cy.get(['type=submit']).click()

        cy.intercept({ method: 'GET', url: 'api/user/1' },
            {
                id: 1,
                email: "yoga@studio.com",
                lastName: "lastName",
                firstName: "firstName",
                admin: true,
                password: "",
                createdAt: new Date(),
                updatedAt: new Date()
            }
        )

        cy.get('[routerlink=me]').click()
        cy.url().should('include', '/me')
        cy.get('[class=mat-button-wrapper]').click()

        cy.intercept({ method: 'GET', url: 'api/user/1' }, 
            {
                id: 1,
                email: "yoga@studio.com",
                lastName: "lastName",
                firstName: "firstName",
                admin: false,
                password: "",
                createdAt: new Date(),
                updatedAt: new Date()
            }
        )
        
        cy.get('[routerlink=me]').click()
        
        cy.url().should('include', '/me')

        cy.intercept('DELETE', '/api/user/1', {})

        cy.get('[color="warn"]').click()

        cy.visit('/not-found')
    })
  });