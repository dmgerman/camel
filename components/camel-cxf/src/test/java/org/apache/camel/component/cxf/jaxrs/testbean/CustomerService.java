begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs.testbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PUT
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/customerservice/"
argument_list|)
DECL|class|CustomerService
specifier|public
class|class
name|CustomerService
block|{
DECL|field|currentId
name|long
name|currentId
init|=
literal|123
decl_stmt|;
DECL|field|customers
name|Map
argument_list|<
name|Long
argument_list|,
name|Customer
argument_list|>
name|customers
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Customer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|orders
name|Map
argument_list|<
name|Long
argument_list|,
name|Order
argument_list|>
name|orders
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Order
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|CustomerService ()
specifier|public
name|CustomerService
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/customers/{id}/"
argument_list|)
DECL|method|getCustomer (@athParamR) String id)
specifier|public
name|Customer
name|getCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Customer
name|c
init|=
name|customers
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/customers/"
argument_list|)
DECL|method|updateCustomer (Customer customer)
specifier|public
name|Response
name|updateCustomer
parameter_list|(
name|Customer
name|customer
parameter_list|)
block|{
name|Customer
name|c
init|=
name|customers
operator|.
name|get
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Response
name|r
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|customers
operator|.
name|put
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|customer
argument_list|)
expr_stmt|;
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|Response
operator|.
name|notModified
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/customers/"
argument_list|)
DECL|method|addCustomer (Customer customer)
specifier|public
name|Response
name|addCustomer
parameter_list|(
name|Customer
name|customer
parameter_list|)
block|{
name|customer
operator|.
name|setId
argument_list|(
operator|++
name|currentId
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|customer
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/customersUniqueResponseCode/"
argument_list|)
DECL|method|addCustomerUniqueResponseCode (Customer customer)
specifier|public
name|Response
name|addCustomerUniqueResponseCode
parameter_list|(
name|Customer
name|customer
parameter_list|)
block|{
name|customer
operator|.
name|setId
argument_list|(
operator|++
name|currentId
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|customer
operator|.
name|getId
argument_list|()
argument_list|,
name|customer
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
literal|201
argument_list|)
operator|.
name|entity
argument_list|(
name|customer
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/customers/{id}/"
argument_list|)
DECL|method|deleteCustomer (@athParamR) String id)
specifier|public
name|Response
name|deleteCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Customer
name|c
init|=
name|customers
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
name|Response
name|r
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|r
operator|=
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|customers
operator|.
name|remove
argument_list|(
name|idNumber
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|Response
operator|.
name|notModified
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/orders/{orderId}/"
argument_list|)
DECL|method|getOrder (@athParamR) String orderId)
specifier|public
name|Order
name|getOrder
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"orderId"
argument_list|)
name|String
name|orderId
parameter_list|)
block|{
name|long
name|idNumber
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|orderId
argument_list|)
decl_stmt|;
name|Order
name|c
init|=
name|orders
operator|.
name|get
argument_list|(
name|idNumber
argument_list|)
decl_stmt|;
return|return
name|c
return|;
block|}
DECL|method|init ()
specifier|final
name|void
name|init
parameter_list|()
block|{
name|Customer
name|c
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|c
operator|.
name|setName
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|customers
operator|.
name|put
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|Order
name|o
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|o
operator|.
name|setDescription
argument_list|(
literal|"order 223"
argument_list|)
expr_stmt|;
name|o
operator|.
name|setId
argument_list|(
literal|223
argument_list|)
expr_stmt|;
name|orders
operator|.
name|put
argument_list|(
name|o
operator|.
name|getId
argument_list|()
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

