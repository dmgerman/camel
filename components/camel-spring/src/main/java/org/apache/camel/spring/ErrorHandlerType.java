begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

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
name|XmlEnum
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
name|XmlType
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
name|builder
operator|.
name|DeadLetterChannelBuilder
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
name|builder
operator|.
name|DefaultErrorHandlerBuilder
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
name|builder
operator|.
name|NoErrorHandlerBuilder
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
name|Metadata
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
name|spring
operator|.
name|spi
operator|.
name|TransactionErrorHandlerBuilder
import|;
end_import

begin_comment
comment|/**  * Used to configure the error handler type  */
end_comment

begin_enum
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"spring,configuration,error"
argument_list|)
annotation|@
name|XmlType
annotation|@
name|XmlEnum
argument_list|(
name|String
operator|.
name|class
argument_list|)
DECL|enum|ErrorHandlerType
specifier|public
enum|enum
name|ErrorHandlerType
block|{
DECL|enumConstant|DefaultErrorHandler
DECL|enumConstant|DeadLetterChannel
DECL|enumConstant|NoErrorHandler
DECL|enumConstant|TransactionErrorHandler
name|DefaultErrorHandler
block|,
name|DeadLetterChannel
block|,
name|NoErrorHandler
block|,
name|TransactionErrorHandler
block|;
comment|/**      * Get the type as class.      *      * @return the class which represents the selected type.      */
DECL|method|getTypeAsClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getTypeAsClass
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|DefaultErrorHandler
case|:
return|return
name|DefaultErrorHandlerBuilder
operator|.
name|class
return|;
case|case
name|DeadLetterChannel
case|:
return|return
name|DeadLetterChannelBuilder
operator|.
name|class
return|;
case|case
name|NoErrorHandler
case|:
return|return
name|NoErrorHandlerBuilder
operator|.
name|class
return|;
case|case
name|TransactionErrorHandler
case|:
return|return
name|TransactionErrorHandlerBuilder
operator|.
name|class
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown error handler: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
block|}
end_enum

end_unit

