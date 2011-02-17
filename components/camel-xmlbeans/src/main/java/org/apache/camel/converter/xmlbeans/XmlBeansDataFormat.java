begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.xmlbeans
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|xmlbeans
package|;
end_package

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
name|OutputStream
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
name|spi
operator|.
name|DataFormat
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmlbeans
operator|.
name|XmlObject
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link DataFormat}) using XmlBeans to marshal to and from XML  *  * @version   */
end_comment

begin_class
DECL|class|XmlBeansDataFormat
specifier|public
class|class
name|XmlBeansDataFormat
implements|implements
name|DataFormat
block|{
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|XmlObject
name|object
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|XmlObject
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|object
operator|.
name|save
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|XmlObject
operator|.
name|Factory
operator|.
name|parse
argument_list|(
name|stream
argument_list|)
return|;
block|}
block|}
end_class

end_unit

