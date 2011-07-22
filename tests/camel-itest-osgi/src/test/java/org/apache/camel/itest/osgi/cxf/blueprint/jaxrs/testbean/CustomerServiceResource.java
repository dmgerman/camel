begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.cxf.blueprint.jaxrs.testbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|blueprint
operator|.
name|jaxrs
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
name|core
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  *  * @version  */
end_comment

begin_comment
comment|// START SNIPPET: example
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/customerservice/"
argument_list|)
DECL|class|CustomerServiceResource
specifier|public
class|class
name|CustomerServiceResource
block|{
DECL|method|CustomerServiceResource ()
specifier|public
name|CustomerServiceResource
parameter_list|()
block|{     }
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
return|return
literal|null
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
return|return
literal|null
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

