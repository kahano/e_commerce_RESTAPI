package com.project.custom_product.Service.Service_Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.project.custom_product.Respository.purchase_repository;

import com.project.custom_product.Service.Purchase_service;
import com.project.custom_product.entities.Customer;
import com.project.custom_product.entities.Product;
import com.project.custom_product.entities.Purchase;
import com.project.custom_product.exception.PurchaseNotFoundExcpetion;

import lombok.AllArgsConstructor;



@AllArgsConstructor

@Service
public class Purchase_serviceImpl implements Purchase_service {


     purchase_repository purchase_repos;


     Customer_serviceImpl customer_service;


     Product_serviceImpl product_service;

  


    @Override
    public Purchase savePurchase(Purchase purchase, Integer customer_id, Integer product_id) {

        Customer customer =  customer_service.findCustomerById(customer_id);
        Product product =  product_service.findProductById(product_id);

       try{
             if(purchase.getTotal_quantities() <= product.getTotal_quantities()){
                  purchase.setCustomer(customer);
                  purchase.setProduct(product);
            
             }

      
       }catch(Exception e){
             e.printStackTrace();
       }


        return purchase_repos.save(purchase);

       
    }

    @Override
    public Purchase findPurchaseBYId(Integer customer_id, Integer product_id) {
        
        Optional<Purchase> purchase = purchase_repos.findByCustomerIdAndProductId(customer_id, product_id);
        return DoesObjectExist(purchase,customer_id, product_id);
       
    }

    @Override
    public Purchase updatePurchase( Integer customer_id, Integer product_id, Integer total_quantities) {
        
          Purchase updated_purchase = this.findPurchaseBYId(customer_id, product_id);
          Product product =  product_service.findProductById(product_id);
          Integer value = 0;

         try{
             if(total_quantities <= product.getTotal_quantities()){
                 value += updated_purchase.getPrice() *  total_quantities;
               
            
             }
            

      
       }catch(Exception e){
             e.printStackTrace();
       }
       updated_purchase.setTotal_quantities(total_quantities);
       updated_purchase.setBill(value);
   
        
        return purchase_repos.save(updated_purchase);
       
    }

    @Override
    public void delete(Integer customer_id, Integer product_id) {
        
        purchase_repos.deletePurchaseByCustomerIdAndProductId(customer_id,product_id);
        
    }

    @Override
    public Purchase getTotalPricePurchase_per_Customer(  Integer customer_id, Integer product_id) {
        
        Purchase checkPurchase = this.findPurchaseBYId(customer_id, product_id);
        try{
            if(checkPurchase.getTotal_quantities() > 0){
               checkPurchase = this.updatePurchase(customer_id, product_id, checkPurchase.getTotal_quantities());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
      
       

        return checkPurchase;
   


       
    }

    

    @Override
    public List<Purchase> getAllCustomersPurchased(Integer customer_id) {
    
        return purchase_repos.findByCustomerId(customer_id);
    }

    @Override
    public List<Purchase> getAllProductssPurchased(Integer product_id) {
       
        return purchase_repos.findByProductId(product_id);
    }
    
    private Purchase DoesObjectExist(Optional<Purchase> object, Integer customer_id, Integer product_id){

        if(object.isPresent()){
            return object.get();
        }
        else{
            throw new PurchaseNotFoundExcpetion(customer_id, product_id);
        }
    }

    @Override
    public List<Purchase> getAllPurchases() {
       
        return (List<Purchase>) purchase_repos.findAll();
       
    }

    private boolean checkQuantities(Integer customer_id, Integer product_id){

        Purchase purchase = this.findPurchaseBYId(customer_id, product_id);
        Product product =  product_service.findProductById(product_id);

        boolean isPresent = false;

       try{
            if(purchase.getTotal_quantities() <= product.getTotal_quantities()){
                isPresent = true;
                
            
            }
       }
       catch(Exception e){

            e.printStackTrace();
            
       }
       return isPresent;

    }

  

 
    


}