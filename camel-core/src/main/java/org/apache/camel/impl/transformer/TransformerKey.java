begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|transformer
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
name|impl
operator|.
name|DefaultCamelContext
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
name|spi
operator|.
name|DataType
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ValueHolder
import|;
end_import

begin_comment
comment|/**  * Key used in {@link org.apache.camel.spi.TransformerRegistry} in {@link DefaultCamelContext},  * to ensure a consistent lookup.  */
end_comment

begin_class
DECL|class|TransformerKey
specifier|public
specifier|final
class|class
name|TransformerKey
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
block|{
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
DECL|field|from
specifier|private
name|DataType
name|from
decl_stmt|;
DECL|field|to
specifier|private
name|DataType
name|to
decl_stmt|;
DECL|method|TransformerKey (String scheme)
specifier|public
name|TransformerKey
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|super
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|scheme
argument_list|,
literal|"scheme"
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
DECL|method|TransformerKey (DataType from, DataType to)
specifier|public
name|TransformerKey
parameter_list|(
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
block|{
name|super
argument_list|(
name|createKeyString
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
block|}
DECL|method|createKeyString (DataType from, DataType to)
specifier|private
specifier|static
name|String
name|createKeyString
parameter_list|(
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
block|{
return|return
name|from
operator|+
literal|"/"
operator|+
name|to
return|;
block|}
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
DECL|method|getFrom ()
specifier|public
name|DataType
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|getTo ()
specifier|public
name|DataType
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

