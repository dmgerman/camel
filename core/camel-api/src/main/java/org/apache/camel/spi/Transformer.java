begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|CamelContext
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
name|CamelContextAware
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
name|Message
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  *<a href="http://camel.apache.org/transformer.html">Transformer</a>  * performs message transformation according to the declared data type.  * {@link org.apache.camel.processor.ContractAdvice} looks for a required Transformer and apply if  * input/output type declared on a route is different from current message type.  *    * @see {@link org.apache.camel.processor.ContractAdvice}  * {@link DataType} {@link org.apache.camel.model.InputTypeDefinition} {@link org.apache.camel.model.OutputTypeDefinition}  */
end_comment

begin_class
DECL|class|Transformer
specifier|public
specifier|abstract
class|class
name|Transformer
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|model
specifier|private
name|String
name|model
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
comment|/**      * Perform data transformation with specified from/to type.      *      * @param message message to apply transformation      * @param from 'from' data type      * @param to 'to' data type      */
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
specifier|abstract
name|void
name|transform
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get a data model which is supported by this transformer.      */
DECL|method|getModel ()
specifier|public
name|String
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
block|}
comment|/**      * Get 'from' data type.      */
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
comment|/**      * Get 'to' data type.      */
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
comment|/**      * Set data model.      *      * @param model data model      */
DECL|method|setModel (String model)
specifier|public
name|Transformer
name|setModel
parameter_list|(
name|String
name|model
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
name|model
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set 'from' data type.      *      * @param from 'from' data type      */
DECL|method|setFrom (String from)
specifier|public
name|Transformer
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
operator|new
name|DataType
argument_list|(
name|from
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set 'to' data type.      *      * @param to 'to' data type      */
DECL|method|setTo (String to)
specifier|public
name|Transformer
name|setTo
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
operator|new
name|DataType
argument_list|(
name|to
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|context
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
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s[scheme='%s', from='%s', to='%s']"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|model
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// no-op
block|}
block|}
end_class

end_unit

