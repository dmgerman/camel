begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|cxf
operator|.
name|mtom_feature
operator|.
name|Hello
import|;
end_import

begin_class
annotation|@
name|BindingType
argument_list|(
name|value
operator|=
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|SOAP11HTTP_MTOM_BINDING
argument_list|)
DECL|class|HelloImpl
specifier|public
class|class
name|HelloImpl
implements|implements
name|Hello
block|{
DECL|method|detail (Holder<byte[]> photo, Holder<Image> image)
specifier|public
name|void
name|detail
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
parameter_list|,
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
parameter_list|)
block|{
comment|// echo through Holder
block|}
DECL|method|echoData (Holder<byte[]> data)
specifier|public
name|void
name|echoData
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|data
parameter_list|)
block|{
comment|// echo through Holder
block|}
block|}
end_class

end_unit

