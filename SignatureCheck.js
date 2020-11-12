
const crypto = require('crypto')

// from signer's website
const publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEQGe6Jnaec68fy0zcWMpxOdzGInDb4pYr9yU/ywr4Uvo419OIVoFMS3BX0XzUEi1C3Qt6C2n9INin9IkTRkKxNA=="
// from signer's HTTP header
const sig = "MEUCIQDV7Ijk4YbwN1+XpJDoWMWljiFj/P0oCJyVt+Lz1mhJmgIgH1kXcw0K7eGTZ+7U4vgxqDzL8SFVcuIjYMbJz6GJXjw="

const verify = crypto.createVerify('SHA256')
verify.write(JSON.stringify({'ok':'true'}))
verify.end()

console.log(verify.verify({key: Buffer.from(publicKey, 'base64'), format: 'der', type: 'spki'}, sig, 'base64'))

