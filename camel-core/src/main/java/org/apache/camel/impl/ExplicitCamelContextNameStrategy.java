begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|CamelContextNameStrategy
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Strategy to used an explicit (fixed) name for {@link org.apache.camel.CamelContext}.  */
end_comment

begin_class
DECL|class|ExplicitCamelContextNameStrategy
specifier|public
class|class
name|ExplicitCamelContextNameStrategy
implements|implements
name|CamelContextNameStrategy
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|ExplicitCamelContextNameStrategy (String name)
specifier|public
name|ExplicitCamelContextNameStrategy
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"CamelContext name "
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|getNextName ()
specifier|public
name|String
name|getNextName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|isFixedName ()
specifier|public
name|boolean
name|isFixedName
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

