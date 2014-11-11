begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.validator.msv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|validator
operator|.
name|msv
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|Component
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
name|validator
operator|.
name|ValidatorEndpoint
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
name|processor
operator|.
name|validation
operator|.
name|ValidatingProcessor
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
name|UriEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|iso_relax
operator|.
name|verifier
operator|.
name|jaxp
operator|.
name|validation
operator|.
name|RELAXNGSchemaFactoryImpl
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"msv"
argument_list|)
DECL|class|MsvEndpoint
specifier|public
class|class
name|MsvEndpoint
extends|extends
name|ValidatorEndpoint
block|{
DECL|method|MsvEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|MsvEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureValidator (ValidatingProcessor validator)
specifier|protected
name|void
name|configureValidator
parameter_list|(
name|ValidatingProcessor
name|validator
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|configureValidator
argument_list|(
name|validator
argument_list|)
expr_stmt|;
name|validator
operator|.
name|setSchemaLanguage
argument_list|(
name|XMLConstants
operator|.
name|RELAXNG_NS_URI
argument_list|)
expr_stmt|;
comment|// must use Dom for Msv to work
name|validator
operator|.
name|setUseDom
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// use relax schema factory by default
if|if
condition|(
name|getSchemaFactory
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setSchemaFactory
argument_list|(
operator|new
name|RELAXNGSchemaFactoryImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

