
const crypto = require('crypto')

function createPrivateKey() {
    return new Promise((resolve, reject) => {
      crypto.generateKeyPair("ec", {
          namedCurve: "P-256",
          publicKeyEncoding: { 
              type: 'spki',
              format: 'der'
          },
          privateKeyEncoding: {
              type: 'pkcs8',
              format: 'der'
          }
      }, (err, publicKey, privateKey) => {
          resolve({publicKey: publicKey.toString('base64'), privateKey: privateKey.toString('base64')})
      })
     })
}

function sign(privateKey, body) {
   const sign = crypto.createSign('SHA256')
   sign.write(body)
   sign.end()
   const signature = sign.sign({key: Buffer.from(privateKey, 'base64'), format: 'der', type: 'pkcs8'}, 'base64')
   return signature
}

async function init() {
   let keyPair = await createPrivateKey()
   console.log('// put in your API, or load from a file')
   console.log('const PRIVATE_KEY = "' + keyPair.privateKey + '"')
   console.log('')
   console.log('// publish this string in a file on your website')
   console.log('const PUBLIC_KEY = "' + keyPair.publicKey + '"')
   console.log('')
   
   let signature = sign(keyPair.privateKey, JSON.stringify({'ok':'true'}))
      
   console.log('')
   console.log('// Example HTTP header, replace URL with URL pointing to your public key on your website')
   console.log('X-Signature-DER: https://my.site/public-api.key ' + signature)
}

init()
