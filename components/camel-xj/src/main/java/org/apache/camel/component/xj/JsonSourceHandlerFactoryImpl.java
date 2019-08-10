begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xj
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonParser
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
name|ExpectedBodyTypeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_class
DECL|class|JsonSourceHandlerFactoryImpl
specifier|public
class|class
name|JsonSourceHandlerFactoryImpl
implements|implements
name|SourceHandlerFactory
block|{
DECL|field|jsonFactory
specifier|private
name|JsonFactory
name|jsonFactory
decl_stmt|;
DECL|field|isFailOnNullBody
specifier|private
name|boolean
name|isFailOnNullBody
init|=
literal|true
decl_stmt|;
DECL|method|JsonSourceHandlerFactoryImpl (JsonFactory jsonFactory)
specifier|public
name|JsonSourceHandlerFactoryImpl
parameter_list|(
name|JsonFactory
name|jsonFactory
parameter_list|)
block|{
name|this
operator|.
name|jsonFactory
operator|=
name|jsonFactory
expr_stmt|;
block|}
DECL|method|isFailOnNullBody ()
specifier|public
name|boolean
name|isFailOnNullBody
parameter_list|()
block|{
return|return
name|isFailOnNullBody
return|;
block|}
DECL|method|setFailOnNullBody (boolean failOnNullBody)
specifier|public
name|void
name|setFailOnNullBody
parameter_list|(
name|boolean
name|failOnNullBody
parameter_list|)
block|{
name|isFailOnNullBody
operator|=
name|failOnNullBody
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSource (Exchange exchange)
specifier|public
name|Source
name|getSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|JsonParser
name|jsonParser
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
operator|(
name|File
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|InputStream
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
operator|(
name|InputStream
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Reader
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
operator|(
name|Reader
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonParser
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|bodyAsString
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|bodyAsString
operator|!=
literal|null
condition|)
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
name|bodyAsString
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jsonParser
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isFailOnNullBody
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|Source
operator|.
name|class
argument_list|)
throw|;
block|}
else|else
block|{
name|jsonParser
operator|=
name|jsonFactory
operator|.
name|createParser
argument_list|(
literal|"{}"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|XMLStreamReader
name|xmlStreamReader
init|=
operator|new
name|JsonXmlStreamReader
argument_list|(
name|jsonParser
argument_list|)
decl_stmt|;
return|return
operator|new
name|StAXSource
argument_list|(
name|xmlStreamReader
argument_list|)
return|;
block|}
block|}
end_class

end_unit

