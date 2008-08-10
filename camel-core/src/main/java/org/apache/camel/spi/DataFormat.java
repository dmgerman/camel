begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Represents a  *<a href="http://activemq.apache.org/camel/data-format.html">data format</a>  * used to marshal objects to and from streams  * such as Java Serialization or using JAXB2 to encode/decode objects using XML  * or using SOAP encoding.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|DataFormat
specifier|public
interface|interface
name|DataFormat
block|{
comment|/**      * Marshals the object to the given Stream.      */
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Unmarshals the given stream into an object.      *<p/>      *<b>Notice:</b> The result is set as body on the exchange OUT message.      * It is possible to mutate the OUT message provided in the given exchange parameter.      * For instance adding headers to the OUT message will be preserved.      */
DECL|method|unmarshal (Exchange exchange, InputStream stream)
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
function_decl|;
block|}
end_interface

end_unit

