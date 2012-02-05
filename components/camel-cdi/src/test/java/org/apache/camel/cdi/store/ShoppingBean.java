begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.store
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|store
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|enterprise
operator|.
name|inject
operator|.
name|New
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_class
annotation|@
name|Named
DECL|class|ShoppingBean
specifier|public
class|class
name|ShoppingBean
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Inject
DECL|field|products
specifier|private
name|Products
name|products
decl_stmt|;
DECL|field|items
specifier|private
name|List
argument_list|<
name|Item
argument_list|>
name|items
init|=
operator|new
name|ArrayList
argument_list|<
name|Item
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|ShoppingBean ()
specifier|public
name|ShoppingBean
parameter_list|()
block|{     }
annotation|@
name|Inject
DECL|method|ShoppingBean (@ew Item defaultItem)
specifier|public
name|ShoppingBean
parameter_list|(
annotation|@
name|New
name|Item
name|defaultItem
parameter_list|)
block|{
name|defaultItem
operator|.
name|setName
argument_list|(
literal|"Default Item"
argument_list|)
expr_stmt|;
name|defaultItem
operator|.
name|setPrice
argument_list|(
literal|1000L
argument_list|)
expr_stmt|;
name|items
operator|.
name|add
argument_list|(
name|defaultItem
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Produces
annotation|@
name|Named
argument_list|(
literal|"selectedItems"
argument_list|)
DECL|method|listSelectedItems ()
specifier|public
name|List
argument_list|<
name|Item
argument_list|>
name|listSelectedItems
parameter_list|()
block|{
return|return
name|this
operator|.
name|items
return|;
block|}
annotation|@
name|Produces
annotation|@
name|Named
argument_list|(
literal|"allProducts"
argument_list|)
DECL|method|listAllProducts ()
specifier|public
name|List
argument_list|<
name|Item
argument_list|>
name|listAllProducts
parameter_list|()
block|{
return|return
name|this
operator|.
name|products
operator|.
name|getProducts
argument_list|()
return|;
block|}
block|}
end_class

end_unit

