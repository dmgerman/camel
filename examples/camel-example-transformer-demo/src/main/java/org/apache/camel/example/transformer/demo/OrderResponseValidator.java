begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.transformer.demo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|transformer
operator|.
name|demo
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
name|Validator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|OrderResponseValidator
specifier|public
class|class
name|OrderResponseValidator
extends|extends
name|Validator
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OrderResponseValidator
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|validate (Message message, DataType type)
specifier|public
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
block|{
name|Object
name|body
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Validating message body: {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|body
operator|instanceof
name|OrderResponse
operator|)
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"Expected OrderResponse, but was "
operator|+
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
name|OrderResponse
name|r
init|=
operator|(
name|OrderResponse
operator|)
name|body
decl_stmt|;
if|if
condition|(
operator|!
name|r
operator|.
name|isAccepted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
literal|"Order was not accepted:"
operator|+
name|r
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

