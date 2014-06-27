/**
 * com.vekomy.vbooks.helpers.ProductParcelable.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 26-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.helpers;

import com.vekomy.vbooks.app.response.DeliveryNoteProductResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nkr
 *
 */
public class ProductParcelable implements Parcelable {
	
	public String 	productName;
	public String 	batchNumber;
	public  Float 	productCost;
	public  Integer availableQty;
	public  Integer enteredQty;
	public  Integer bonusEnteredQty;
	public  Float	totalCost;
	public  String	bonusReason;
	
	public ProductParcelable(DeliveryNoteProductResponse product){
		productName =	product.getProductName();
		batchNumber	=	product.getBatchNumber();
		productCost =	product.getProductCost();
		availableQty=	product.getAvailableQty();
		enteredQty = 0;
		bonusEnteredQty = 0;
		totalCost = 0f;		
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(productName);
		dest.writeString(batchNumber);
		dest.writeFloat(productCost);
		dest.writeInt(availableQty);
		dest.writeInt(enteredQty);
		dest.writeInt(bonusEnteredQty);
		dest.writeFloat(totalCost);
		dest.writeString(bonusReason);
	}
	


    public ProductParcelable(Parcel in)
    {
        productName  	=	in.readString();
        batchNumber  	=	in.readString();
        productCost  	=	in.readFloat();
        availableQty 	=	in.readInt();
        enteredQty		=	in.readInt();
        bonusEnteredQty =	in.readInt();
        totalCost		=	in.readFloat();
        bonusReason		=	in.readString();
    }


	
	/**
	 * It will be required during un-marshaling data stored in a Parcel
	 */


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public ProductParcelable createFromParcel(Parcel in)
        {
            return new ProductParcelable(in);
        }
        
        public ProductParcelable[] newArray(int size)
        {
        	return new ProductParcelable[size];
        }
    };

}
