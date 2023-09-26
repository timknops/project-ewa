export class Product {

   static productNames =
    ["Enphase IQ8+ omvormer",
    "Enphase Q kabel 1 fase ",
    "Gateway / envoy",
    "Enphase IQ8M  omvormer",
    "Enphase Q Relay 1 fase ",
    "Enphase Q Relay 3 fase "
    ]

  /**
   * Constructor of a product
   * @param {String} productName
   * @param {String} description
   * @param {Number} quantity
   */
  constructor(productName, description, quantity) {
    this.productName = productName;
    this.description = description;
    this.quantity = quantity;
  }

  /**
   * create an array of dummy products
   * @return {Product[]}
   */
  static createDummyProduct() {
    const products = [];
    Product.productNames.forEach((productName)=> {
      const description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. "
      const quantity = this.randomNumber(0, 40)
      products.push(new Product(productName, description, quantity)) ;
    })
    return products;
  }
  static randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min)
  }
}