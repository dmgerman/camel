begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.simple.linkonetomany
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|linkonetomany
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|OneToMany
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|,
name|generateHeaderColumns
operator|=
literal|true
argument_list|)
DECL|class|Order
specifier|public
class|class
name|Order
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|orderNumber
specifier|private
name|int
name|orderNumber
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|)
DECL|field|customerName
specifier|private
name|String
name|customerName
decl_stmt|;
annotation|@
name|OneToMany
DECL|field|items
specifier|private
name|List
argument_list|<
name|OrderItem
argument_list|>
name|items
decl_stmt|;
DECL|method|getOrderNumber ()
specifier|public
name|int
name|getOrderNumber
parameter_list|()
block|{
return|return
name|orderNumber
return|;
block|}
DECL|method|setOrderNumber (int orderNumber)
specifier|public
name|void
name|setOrderNumber
parameter_list|(
name|int
name|orderNumber
parameter_list|)
block|{
name|this
operator|.
name|orderNumber
operator|=
name|orderNumber
expr_stmt|;
block|}
DECL|method|getCustomerName ()
specifier|public
name|String
name|getCustomerName
parameter_list|()
block|{
return|return
name|customerName
return|;
block|}
DECL|method|setCustomerName (String customerName)
specifier|public
name|void
name|setCustomerName
parameter_list|(
name|String
name|customerName
parameter_list|)
block|{
name|this
operator|.
name|customerName
operator|=
name|customerName
expr_stmt|;
block|}
DECL|method|getItems ()
specifier|public
name|List
argument_list|<
name|OrderItem
argument_list|>
name|getItems
parameter_list|()
block|{
return|return
name|items
return|;
block|}
DECL|method|setItems (List<OrderItem> items)
specifier|public
name|void
name|setItems
parameter_list|(
name|List
argument_list|<
name|OrderItem
argument_list|>
name|items
parameter_list|)
block|{
name|this
operator|.
name|items
operator|=
name|items
expr_stmt|;
block|}
block|}
end_class

end_unit

