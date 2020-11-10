Feature('Result');

//npx codeceptjs run --steps

Scenario('Testing Result View', ({ I }) => {
    I.amOnPage('https://is3-result.herokuapp.com/')
    I.see('CATS')
    I.see('DOGS')
    I.seeElement('//*[@id="background-stats-2"]')
    I.seeElement('//*[@id="background-stats-1"]')
});