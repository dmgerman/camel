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
name|OutputStream
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
name|Result
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
name|StreamResult
import|;
end_import

begin_comment
comment|/**  * Marshallers that marshall to XML should extend this base class.  *  * @version $Revision: 520124 $  */
end_comment

begin_class
DECL|class|XmlMarshaller
specifier|public
specifier|abstract
class|class
name|XmlMarshaller
implements|implements
name|Marshaller
block|{
comment|/**      * Marshals the object to the given Stream.      */
DECL|method|marshal (Object object, OutputStream result)
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|object
parameter_list|,
name|OutputStream
name|result
parameter_list|)
throws|throws
name|IOException
block|{
name|marshal
argument_list|(
name|object
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|marshal (Object object, Result result)
specifier|abstract
specifier|public
name|void
name|marshal
parameter_list|(
name|Object
name|object
parameter_list|,
name|Result
name|result
parameter_list|)
function_decl|;
block|}
end_class

end_unit

