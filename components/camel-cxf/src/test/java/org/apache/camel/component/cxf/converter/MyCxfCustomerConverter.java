begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
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
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|Converter
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
name|component
operator|.
name|cxf
operator|.
name|CxfPayload
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|// This converter is used to show how to override the CxfPayload default toString converter
end_comment

begin_class
annotation|@
name|Converter
DECL|class|MyCxfCustomerConverter
specifier|public
specifier|final
class|class
name|MyCxfCustomerConverter
block|{
DECL|method|MyCxfCustomerConverter ()
specifier|private
name|MyCxfCustomerConverter
parameter_list|()
block|{
comment|//Helper class
block|}
annotation|@
name|Converter
DECL|method|cxfPayloadToString (final CxfPayload payload)
specifier|public
specifier|static
name|String
name|cxfPayloadToString
parameter_list|(
specifier|final
name|CxfPayload
name|payload
parameter_list|)
block|{
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|element
range|:
name|payload
operator|.
name|getBody
argument_list|()
control|)
block|{
name|String
name|elementString
init|=
literal|""
decl_stmt|;
try|try
block|{
name|elementString
operator|=
name|converter
operator|.
name|toString
argument_list|(
operator|(
name|Element
operator|)
name|element
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
name|elementString
operator|=
name|element
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|elementString
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

