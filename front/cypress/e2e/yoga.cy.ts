import sessions from '../fixtures/sessions.json';

describe('yoga spec', () => {
    it('yoga login, session, user and register', () => {
        let urlSession = '/api/session';
        let urlServer = 'http://localhost:4200'

        //User login 
        cy.visit('/login')
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'first name',
                lastName: 'last name',
                admin: true
            },
        })
        cy.intercept('GET', urlSession, sessions).as('sessions')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        //Check that we are in session page after login
        cy.url().should('include', 'sessions')
        cy.url().should('eq', `${urlServer}/sessions`) 

        
        //Visite detail page when click on button detail
        cy.intercept('GET', '/api/teacher/2', {
            "id": 2,
            "firstName": "first name",
            "lastName": "last name"
        })
        cy.intercept('GET', `${urlSession}/1`, sessions[0]).as('detail')
        cy.contains('button', 'Detail').first().click()
        //Check that we are in detail page after click
        cy.url().should('include', 'detail')
        cy.url().should('eq', `${urlServer}/sessions/detail/1`)


        //Come back to session page 
        cy.intercept('GET', urlSession, sessions).as('sessions')
        cy.contains('button', 'arrow_back').click()
        //Check that we are in session after click back button
        cy.url().should('include', 'sessions')
        cy.url().should('eq', `${urlServer}/sessions`) 


        //Visite detail page and delete session 
        cy.intercept('GET', `${urlSession}/1`, sessions[0]).as('detail')
        cy.contains('button', 'Detail').first().click()
        cy.intercept('GET', urlSession, [sessions[1]]).as('sessions')
        cy.intercept('DELETE', `${urlSession}/1`, sessions[0]).as('detail')
        cy.contains('button', 'Delete').click()
        //Check that we are in session after delete session 1
        cy.url().should('include', 'sessions')
        cy.url().should('eq', `${urlServer}/sessions`)

    
        //Visite page update session
        cy.intercept('GET', '/api/teacher', [
            {
                "id": 1,
                "firstName": "Teacher name",
                "lastName": ""
            },
            {
                "id": 2,
                "firstName": "My Tech",
                "lastName": "test"
            }])
        cy.intercept('GET', `${urlSession}/2`, sessions[1]).as('edit')
        cy.contains('button', 'Edit').click()
        //Check that we are in update page after click
        cy.url().should('include', 'update')
        cy.url().should('eq', `${urlServer}/sessions/update/2`)

        //Update session
        cy.intercept('PUT', `${urlSession}/2`, {
            "name": "Yoga nocturne",
            "date": sessions[1].date,
            "teacher_id": sessions[1].teacher_id,
            "description": sessions[1].description
        })
        sessions[1].name = "Yoga nocturne"
        cy.get('input[formControlName=name]').clear()
        cy.get('input[formControlName=name]').type("Yoga nocturne")
        cy.contains('button', 'Save').click()
        //Check that we are in session page after update
        cy.url().should('include', 'sessions')
        cy.url().should('eq', `${urlServer}/sessions`)
        cy.get('mat-card-title').should('contain', 'Yoga nocturne')

        
        //Visite page me
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
        //Check that we are in me page after click
        cy.url().should('include', 'me')
        cy.url().should('eq', `${urlServer}/me`)

        //Go back to page session
        cy.contains('button', 'arrow_back').click()
        cy.url().should('include', 'sessions')
        cy.url().should('eq', `${urlServer}/sessions`)


        //Go back to me page and delete user
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
        //Check that we are in me page after click
        cy.url().should('include', '/me')
        cy.url().should('eq', `${urlServer}/me`)


        //Delete session
        cy.intercept('DELETE', '/api/user/1', {})
        cy.get('[color="warn"]').click()


        //Check that we are in / page after delete
        cy.url().should('eq', `${urlServer}/`)
        cy.get('[routerlink=register]').should('contain', 'Register')


        //Visite not found page
        cy.visit('/not-found')
        //Check that we are in not found page
        cy.url().should('include', '/404')
        cy.url().should('eq', `${urlServer}/404`)


        //Visite page register
        cy.get('[routerlink=register]').click()
        /*cy.visit('/register')*/
        cy.intercept('POST', '/api/auth/register', {
          body: {
              email: "email@test.io",
              firstName: "firstName",
              lastName: "lastName",
              password: "password"
          }
        })
        cy.intercept({ method: 'GET', url: '/login'},[]).as('login')
        cy.get('input[formControlName=firstName]').type("firstName")
        cy.get('input[formControlName=lastName]').type("lastName")
        cy.get('input[formControlName=email]').type("email@test.io")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)  
        //Check that we are in login page after register
        cy.url().should('include', '/login')
        cy.url().should('eq', `${urlServer}/login`)
    })
  });