begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
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
name|Processor
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
name|spi
operator|.
name|Transformer
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
name|DefaultExchange
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
name|ServiceHelper
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A {@link Transformer} implementation which leverages {@link Processor} to perform transformation.  *   * {@see Transformer}  */
end_comment

begin_class
DECL|class|ProcessorTransformer
specifier|public
class|class
name|ProcessorTransformer
extends|extends
name|Transformer
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|transformerString
specifier|private
name|String
name|transformerString
decl_stmt|;
DECL|method|ProcessorTransformer (CamelContext context)
specifier|public
name|ProcessorTransformer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Perform data transformation with specified from/to type using Processor.      * @param message message to apply transformation      * @param from 'from' data type      * @param to 'to' data type      */
annotation|@
name|Override
DECL|method|transform (Message message, DataType from, DataType to)
specifier|public
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
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|CamelContext
name|context
init|=
name|exchange
operator|.
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|.
name|isJavaType
argument_list|()
condition|)
block|{
name|Object
name|input
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fromClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|from
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fromClass
operator|.
name|isAssignableFrom
argument_list|(
name|input
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Converting to '{}'"
argument_list|,
name|fromClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|fromClass
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to transform processor '{}'"
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|DefaultExchange
name|transformExchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|transformExchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|transformExchange
operator|.
name|setProperties
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|transformExchange
argument_list|)
expr_stmt|;
name|Message
name|answer
init|=
name|transformExchange
operator|.
name|hasOut
argument_list|()
condition|?
name|transformExchange
operator|.
name|getOut
argument_list|()
else|:
name|transformExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|to
operator|.
name|isJavaType
argument_list|()
condition|)
block|{
name|Object
name|answerBody
init|=
name|answer
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|toClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|to
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|toClass
operator|.
name|isAssignableFrom
argument_list|(
name|answerBody
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Converting to '{}'"
argument_list|,
name|toClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answerBody
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|toClass
argument_list|,
name|answerBody
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|answerBody
argument_list|)
expr_stmt|;
block|}
block|}
name|message
operator|.
name|copyFrom
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set processor to use      *      * @param processor Processor      * @return this ProcessorTransformer instance      */
DECL|method|setProcessor (Processor processor)
specifier|public
name|ProcessorTransformer
name|setProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|transformerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
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
if|if
condition|(
name|transformerString
operator|==
literal|null
condition|)
block|{
name|transformerString
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"ProcessorTransformer[scheme='%s', from='%s', to='%s', processor='%s']"
argument_list|,
name|getModel
argument_list|()
argument_list|,
name|getFrom
argument_list|()
argument_list|,
name|getTo
argument_list|()
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
return|return
name|transformerString
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|this
operator|.
name|processor
argument_list|)
expr_stmt|;
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
operator|.
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
