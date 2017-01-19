package com.buychat.pojos;

import java.io.Serializable;


public class ShopPojo implements Serializable {

	int shop_id;
	
	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	int product_id;
	int category_id;
	
	
	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_image() {
		return product_image;
	}

	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}

	public String getProuduct_quantity() {
		return prouduct_quantity;
	}

	public void setProuduct_quantity(String prouduct_quantity) {
		this.prouduct_quantity = prouduct_quantity;
	}

	String product_name;
	String product_image;
	String product_price;

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	String product_description;
	String prouduct_quantity;

	public ShopPojo() {
		}
	
	public ShopPojo(int shop_id, int category_id, int product_id, String product_name,
					String product_image, String product_price, String prouduct_quantity) {
		super();
		this.shop_id = shop_id;
		this.category_id = category_id;
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_image = product_image;
		this.product_price = product_price;
		this.prouduct_quantity = prouduct_quantity;
	}



}
