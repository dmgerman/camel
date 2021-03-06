begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt.saxon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
operator|.
name|saxon
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
name|component
operator|.
name|xslt
operator|.
name|XmlSourceHandlerFactoryImpl
import|;
end_import

begin_class
DECL|class|SaxonXmlSourceHandlerFactoryImpl
specifier|public
class|class
name|SaxonXmlSourceHandlerFactoryImpl
extends|extends
name|XmlSourceHandlerFactoryImpl
block|{
annotation|@
name|Override
DECL|method|getSource (Exchange exchange, Object body)
specifier|protected
name|Source
name|getSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
comment|// body may already be a source
if|if
condition|(
name|body
operator|instanceof
name|Source
condition|)
block|{
return|return
operator|(
name|Source
operator|)
name|body
return|;
block|}
name|Source
name|source
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
comment|// try StAX if enabled
name|source
operator|=
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
name|StAXSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
name|source
operator|=
name|super
operator|.
name|getSource
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
return|return
name|source
return|;
block|}
block|}
end_class

end_unit

