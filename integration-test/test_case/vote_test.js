Feature('Vote');

//npx codeceptjs run --steps

Scenario('Testing Vote View', ({ I }) => {
    I.amOnPage('https://is3-vote.herokuapp.com/')
    I.see('CATS')
    I.see('DOGS')
    I.seeElement('//*[@id="content-container-center"]/h3')
    I.seeElement('//*[@id="a"]')
    I.seeElement('//*[@id="b"]')
});