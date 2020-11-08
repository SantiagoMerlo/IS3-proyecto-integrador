Feature('RESULT');

//npx codeceptjs run --steps

Scenario('Testing View Result', ({ I }) => {
    I.amOnPage('https://is3-result.herokuapp.com/')
    I.see('CATS')
    I.see('DOGS')
    I.seeElement('//*[@id="background-stats-2"]')
    I.seeElement('//*[@id="background-stats-1"]')
});