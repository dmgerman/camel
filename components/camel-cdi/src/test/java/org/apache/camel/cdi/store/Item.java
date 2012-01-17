begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_class
DECL|class|Item
specifier|public
class|class
name|Item
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
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|price
specifier|private
name|long
name|price
decl_stmt|;
DECL|method|Item ()
specifier|public
name|Item
parameter_list|()
block|{      }
DECL|method|Item (String name, long price)
specifier|public
name|Item
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|price
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|price
operator|=
name|price
expr_stmt|;
block|}
comment|/**      * @return the name      */
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
comment|/**      * @param name the name to set      */
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
comment|/**      * @return the price      */
DECL|method|getPrice ()
specifier|public
name|long
name|getPrice
parameter_list|()
block|{
return|return
name|price
return|;
block|}
comment|/**      * @param price the price to set      */
DECL|method|setPrice (long price)
specifier|public
name|void
name|setPrice
parameter_list|(
name|long
name|price
parameter_list|)
block|{
name|this
operator|.
name|price
operator|=
name|price
expr_stmt|;
block|}
block|}
end_class

end_unit

