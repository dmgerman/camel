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
name|IOException
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
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_comment
comment|/**  * Unmarshallers that unmarshall to XML should extend this base class.  *   * @version $Revision: 520124 $  */
end_comment

begin_class
DECL|class|XmlUnmarshaller
specifier|public
specifier|abstract
class|class
name|XmlUnmarshaller
implements|implements
name|Unmarshaller
block|{
comment|/**      * Unmarshals the given stream into an object.      */
DECL|method|unmarshal (InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|unmarshal
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Unmarshals the given stream into an object.      */
DECL|method|unmarshal (Source stream)
specifier|public
specifier|abstract
name|Object
name|unmarshal
parameter_list|(
name|Source
name|stream
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

