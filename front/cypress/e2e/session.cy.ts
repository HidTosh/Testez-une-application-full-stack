import user from '../fixtures/user.json';

import session from '../fixtures/session.json';

import sessions from '../fixtures/sessions.json';

describe('session spec', () => {
    it('User information for simple user', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept('GET', '/api/session', sessions).as('sessions')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        // detail page
        cy.intercept('GET', '/api/teacher/2', {
            "id": 2,
            "firstName": "first name",
            "lastName": "last name"
        })
        cy.intercept('GET', '/api/session/1', session).as('detail')
        cy.contains('button', 'Detail').click()

        // detail page back button
        cy.intercept('GET', '/api/session', sessions).as('sessions')
        cy.contains('button', 'arrow_back').click()

        // detail page
        cy.intercept('GET', '/api/session/1', session).as('detail')
        cy.contains('button', 'Detail').click()

        // detail page button delete
        cy.intercept('GET', '/api/session', [sessions[1]]).as('sessions')
        cy.intercept('DELETE', '/api/session/1', session).as('detail')
        cy.contains('button', 'Delete').click()


        // edit page
        cy.intercept('GET', '/api/teacher', [{
                "id": 5,
                "firstName": "first name",
                "lastName": "last name"
            }])
        cy.intercept('GET', '/api/session/2', {
            "name": "uhijkl",
            "date": "2023-01-01T00:00:00.000+00:00",
            "teacher_id": 5,
            "description": "uijktygu"
        }).as('edit')
        cy.contains('button', 'Edit').click()


        cy.intercept('PUT', '/api/session/2', {
            "name": "retehdkjsndl",
            "date": "2023-01-01T00:00:00.000+00:00",
            "teacher_id": 5,
            "description": "uijktygu"
        })
        cy.get('input[formControlName=name]').clear()
        cy.get('input[formControlName=name]').type("uhijkl")
        cy.contains('button', 'Save').click()

        cy.url().should('include', '/sessions')


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
        cy.contains('button', 'arrow_back').click()

        cy.intercept({ method: 'GET', url: 'api/user/1' }, {
                id: 1,
                email: "yoga@studio.com",
                lastName: "lastName",
                firstName: "firstName",
                admin: false,
                password: "",
                createdAt: new Date(),
                updatedAt: new Date()
            })
        
        cy.get('[routerlink=me]').click()
        
        cy.url().should('include', '/me')

        cy.intercept('DELETE', '/api/user/1', {})

        cy.get('[color="warn"]').click()


        cy.visit('/not-found')


        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
          body: {
              email: "string",
              firstName: "string",
              lastName: "string",
              password: "string"
          },
        })
        
        cy.intercept(
          {
            method: 'GET',
            url: '/login',
          },
          []
        ).as('login')
    
        cy.get('input[formControlName=firstName]').type(user.firstName)
        cy.get('input[formControlName=lastName]').type(user.lastName)
        cy.get('input[formControlName=email]').type(user.email)
        cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)
          
        cy.url().should('include', '/login')


    })
  });