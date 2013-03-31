begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs.simplebinding.testbean
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
name|simplebinding
operator|.
name|testbean
package|;
end_package

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
name|QueryParam
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

begin_class
DECL|class|VipCustomerResource
specifier|public
class|class
name|VipCustomerResource
block|{
annotation|@
name|GET
DECL|method|listVipCustomers ()
specifier|public
name|Response
name|listVipCustomers
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
DECL|method|updateVipCustomer (@athParamR) String id, Customer customer)
specifier|public
name|Response
name|updateVipCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
name|Customer
name|customer
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
DECL|method|deleteVipCustomer (@athParamR) String id, @QueryParam(R) Boolean sendEmail)
specifier|public
name|Response
name|deleteVipCustomer
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"sendEmail"
argument_list|)
name|Boolean
name|sendEmail
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

