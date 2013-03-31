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
literal|"Order"
argument_list|)
DECL|class|Order
specifier|public
class|class
name|Order
block|{
DECL|field|id
specifier|private
name|long
name|id
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|products
specifier|private
name|Map
argument_list|<
name|Long
argument_list|,
name|Product
argument_list|>
name|products
init|=
operator|new
name|HashMap
argument_list|<
name|Long
argument_list|,
name|Product
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|Order ()
specifier|public
name|Order
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (long id)
specifier|public
name|void
name|setId
parameter_list|(
name|long
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
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String d)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|d
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"products/{productId}/"
argument_list|)
DECL|method|getProduct (@athParamR)int productId)
specifier|public
name|Product
name|getProduct
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"productId"
argument_list|)
name|int
name|productId
parameter_list|)
block|{
name|Product
name|p
init|=
name|products
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|productId
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|p
return|;
block|}
DECL|method|init ()
specifier|final
name|void
name|init
parameter_list|()
block|{
name|Product
name|p
init|=
operator|new
name|Product
argument_list|()
decl_stmt|;
name|p
operator|.
name|setId
argument_list|(
literal|323
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
literal|"product 323"
argument_list|)
expr_stmt|;
name|products
operator|.
name|put
argument_list|(
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

