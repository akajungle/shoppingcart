package com.shop;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 
@Entity
@Table(name = "Cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6543153288721685514L;
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
    
    @Column(name = "Order_Date", nullable = false)
    private Date orderDate;
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int sessionId;
    
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE,  CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Collection<CartDetail> cartDetails;
 
    public Cart() {
    	super();
    	orderDate = new Date();
    }
    
	public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
    
    public void addCartDetail(CartDetail cd) {
    	if (cd == null)
    		return;
    	cd.setCart(this);
    	if (cartDetails == null)
    		cartDetails = new ArrayList<CartDetail>();
    	cartDetails.add(cd);
    }

    public Collection<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(Collection<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }
    

    public Date getOrderDate() {
        return orderDate;
    }
 
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
 
    public int getSessionId() {
        return sessionId;
    }
 
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
 
    @Override
	public String toString() {
		return "Cart [id=" + id + ", orderDate=" + orderDate + ", sessionId=" + sessionId + ", cartDetails="
				+ cartDetails + "]";
	}
   
}