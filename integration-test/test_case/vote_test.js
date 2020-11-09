Feature('Vote');

//npx codeceptjs run --steps

Scenario('Testing Vote View', ({ I }) => {
    I.amOnPage('https://is3-vote.herokuapp.com/')
    I.see('CATS')
    I.see('DOGS')
});