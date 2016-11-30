begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.examples
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|examples
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|CascadeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OneToOne
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Entity
annotation|@
name|NamedQuery
argument_list|(
name|name
operator|=
literal|"findAllCustomersWithName"
argument_list|,
name|query
operator|=
literal|"SELECT c FROM Customer c WHERE c.name LIKE :custName "
argument_list|)
DECL|class|Customer
specifier|public
class|class
name|Customer
block|{
annotation|@
name|Id
annotation|@
name|GeneratedValue
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|OneToOne
argument_list|(
name|cascade
operator|=
name|CascadeType
operator|.
name|ALL
argument_list|)
DECL|field|address
specifier|private
name|Address
name|address
decl_stmt|;
DECL|field|orders
specifier|private
name|int
name|orders
decl_stmt|;
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|Address
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|setAddress (Address address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|Address
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getOrders ()
specifier|public
name|int
name|getOrders
parameter_list|()
block|{
return|return
name|orders
return|;
block|}
DECL|method|setOrders (int orders)
specifier|public
name|void
name|setOrders
parameter_list|(
name|int
name|orders
parameter_list|)
block|{
name|this
operator|.
name|orders
operator|=
name|orders
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// OpenJPA warns about fields being accessed directly in methods if NOT using the corresponding getters.
return|return
literal|"Customer[id: "
operator|+
name|getId
argument_list|()
operator|+
literal|", name: "
operator|+
name|getName
argument_list|()
operator|+
literal|", address: "
operator|+
name|getAddress
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

