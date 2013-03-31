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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"CustomerList"
argument_list|)
DECL|class|CustomerList
specifier|public
class|class
name|CustomerList
block|{
DECL|field|customers
specifier|private
name|List
argument_list|<
name|Customer
argument_list|>
name|customers
decl_stmt|;
DECL|method|getCustomers ()
specifier|public
name|List
argument_list|<
name|Customer
argument_list|>
name|getCustomers
parameter_list|()
block|{
return|return
name|customers
return|;
block|}
DECL|method|setCustomers (List<Customer> customers)
specifier|public
name|void
name|setCustomers
parameter_list|(
name|List
argument_list|<
name|Customer
argument_list|>
name|customers
parameter_list|)
block|{
name|this
operator|.
name|customers
operator|=
name|customers
expr_stmt|;
block|}
block|}
end_class

end_unit

