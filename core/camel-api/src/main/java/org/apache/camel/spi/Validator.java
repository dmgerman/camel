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
name|ValidationException
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
comment|/**  *<a href="http://camel.apache.org/validator.html">Validator</a>  * performs message content validation according to the declared data type.  * {@link org.apache.camel.processor.ContractAdvice} applies Validator if input/output type is declared on  * a route with validation enabled.  *    * @see {@link org.apache.camel.processor.ContractAdvice}  * {@link org.apache.camel.model.InputTypeDefinition} {@link org.apache.camel.model.OutputTypeDefinition}  */
end_comment

begin_class
DECL|class|Validator
specifier|public
specifier|abstract
class|class
name|Validator
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
DECL|field|type
specifier|private
name|DataType
name|type
decl_stmt|;
comment|/**      * Perform data validation with specified type.      *      * @param message message to apply validation      * @param type the data type      * @throws ValidationException thrown if any validation error is detected      */
DECL|method|validate (Message message, DataType type)
specifier|public
specifier|abstract
name|void
name|validate
parameter_list|(
name|Message
name|message
parameter_list|,
name|DataType
name|type
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**      * Get 'from' data type.      */
DECL|method|getType ()
specifier|public
name|DataType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Set data type.      *      * @param type data type      */
DECL|method|setType (String type)
specifier|public
name|Validator
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
operator|new
name|DataType
argument_list|(
name|type
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
literal|"%s[type='%s']"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|type
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

