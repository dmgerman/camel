begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_class
DECL|class|NoopXmlStreamWriterWrapper
specifier|public
class|class
name|NoopXmlStreamWriterWrapper
implements|implements
name|JaxbXmlStreamWriterWrapper
block|{
annotation|@
name|Override
DECL|method|wrapWriter (XMLStreamWriter writer)
specifier|public
name|XMLStreamWriter
name|wrapWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
return|return
name|writer
return|;
block|}
block|}
end_class

end_unit

