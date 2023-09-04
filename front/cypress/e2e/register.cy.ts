import user from '../fixtures/user.json'
describe('register spec', () => {
    it('Register successfull', () => {
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