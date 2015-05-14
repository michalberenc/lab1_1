/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.offer;

import java.math.BigDecimal;

public class OfferItem {

    private ProductSnapshot productSnapshot;

	private int quantity;

	private BigDecimal totalCost;

	private String currency;

	// discount
	private String discountCause;

	private BigDecimal discount;

	public OfferItem(ProductSnapshot productSnapshot, int quantity) {
		this(productSnapshot, quantity, null, null);
	}

	public OfferItem(ProductSnapshot productSnapshot, int quantity,
			BigDecimal discount, String discountCause) {
		this.productSnapshot = productSnapshot;

		this.quantity = quantity;
		this.discount = discount;
		this.discountCause = discountCause;

		BigDecimal discountValue = new BigDecimal(0);
		if (discount != null)
			discountValue = discountValue.subtract(discount);

		this.totalCost = productSnapshot.getPrice()
				.multiply(new BigDecimal(quantity)).subtract(discountValue);
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public String getTotalCostCurrency() {
		return currency;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public String getDiscountCause() {
		return discountCause;
	}

	public int getQuantity() {
		return quantity;
	}

    public String getProductId(){
        return productSnapshot.getId();
    }

	/**
	 * 
	 * @param other
	 * @param delta
	 *            acceptable percentage difference
	 * @return
	 */
	public boolean sameAs(OfferItem other, double delta) {
        if(! productSnapshot.sameAs(other.productSnapshot)){
            return false;
        }

		if (quantity != other.quantity){
            return false;
        }

		BigDecimal max, min;
		if (totalCost.compareTo(other.totalCost) > 0) {
			max = totalCost;
			min = other.totalCost;
		} else {
			max = other.totalCost;
			min = totalCost;
		}

		BigDecimal difference = max.subtract(min);
		BigDecimal acceptableDelta = max.multiply(new BigDecimal(delta / 100));

		return acceptableDelta.compareTo(difference) > 0;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferItem)) return false;

        OfferItem offerItem = (OfferItem) o;

        if (quantity != offerItem.quantity) return false;
        if (!currency.equals(offerItem.currency)) return false;
        if (!discount.equals(offerItem.discount)) return false;
        if (!discountCause.equals(offerItem.discountCause)) return false;
        if (!productSnapshot.equals(offerItem.productSnapshot)) return false;
        if (!totalCost.equals(offerItem.totalCost)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productSnapshot.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + totalCost.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + discountCause.hashCode();
        result = 31 * result + discount.hashCode();
        return result;
    }
}