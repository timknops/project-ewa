/**
 * class which represents a product sold by Solar Sedum
 *
 * @author Julian Kruithof
 */
export class Product {

  /* array of current products of Solar
  * when database is setup this will be stored in the database*/
  static productNames =
    ["Enphase IQ8+ omvormer",
      "Enphase Q kabel 1 fase",
      "Gateway / envoy",
      "Enphase IQ8M  omvormer",
      "Enphase Q Relay 1 fase ",
      "Enphase Q Relay 3 fase ",
      "MB glas/glas 380"
    ]

  /**
   * Constructor of a product
   *
   * @constructor
   * @param {number} id
   * @param {String} productName
   * @param {String} description
   * @param {Number} quantity
   */
  constructor(id, productName, description, quantity) {
    this.id = id;
    this.productName = productName;
    this.description = description;
    this.quantity = quantity;
  }

  /**
   * create an array of dummy products, for each product currently known
   * this is temporary
   *
   * @return {Product[]} - list of products with description and random stock
   */
  static createDummyProduct() {
    let id = 3000;
    const products = [];

    /*
     * Create a random stock for each product
     */
    Product.productNames.forEach((productName) => {
      const description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. "
      const quantity = this.randomNumber(0, 40)
      const product = new Product(id, productName, description, quantity)
      delete product.id //remove id from display;
      products.push(product);

      //for each product increment the id
      id += 3;
    })
    return products;
  }


  /**
   * Get a random value between the min and max, including min excluding max
   *
   * @param min - the lowest number which can be chosen
   * @param max - the highest number which can be chosen
   * @return {number} - random number between min and max
   */
  static randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min)
  }

  static copyConstructor(product) {
    if (product == null) return null;
    return Object.assign(new Product(), product);

  }
}