begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chronicle.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chronicle
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|net
operator|.
name|openhft
operator|.
name|chronicle
operator|.
name|engine
operator|.
name|api
operator|.
name|tree
operator|.
name|AssetTree
import|;
end_import

begin_import
import|import
name|net
operator|.
name|openhft
operator|.
name|chronicle
operator|.
name|engine
operator|.
name|tree
operator|.
name|VanillaAssetTree
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The camel chronicle-engine component let you leverage the power of OpenHFT's<a href="https://github.com/OpenHFT/Chronicle-Engine">Chronicle-Engine</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.18.0"
argument_list|,
name|scheme
operator|=
literal|"chronicle-engine"
argument_list|,
name|title
operator|=
literal|"Chronicle Engine"
argument_list|,
name|syntax
operator|=
literal|"chronicle-engine:addresses/path"
argument_list|,
name|label
operator|=
literal|"datagrid,cache"
argument_list|)
DECL|class|ChronicleEngineEndpoint
specifier|public
class|class
name|ChronicleEngineEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Engine addresses. Multiple addresses can be separated by comma."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|addresses
specifier|private
name|String
name|addresses
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Engine path"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|ChronicleEngineConfiguration
name|configuration
decl_stmt|;
DECL|method|ChronicleEngineEndpoint (String uri, ChronicleEngineComponent component, ChronicleEngineConfiguration configuration)
specifier|public
name|ChronicleEngineEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ChronicleEngineComponent
name|component
parameter_list|,
name|ChronicleEngineConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ChronicleEngineProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ChronicleEngineConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
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
if|if
condition|(
operator|!
name|this
operator|.
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|this
operator|.
name|path
operator|=
literal|"/"
operator|+
name|this
operator|.
name|path
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|setAddresses (String addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|String
name|addresses
parameter_list|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|setConfiguration (ChronicleEngineConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ChronicleEngineConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|// ****************************
comment|// Helpers
comment|// ****************************
DECL|method|getConfiguration ()
specifier|protected
name|ChronicleEngineConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getPath ()
specifier|protected
name|String
name|getPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|path
return|;
block|}
DECL|method|getUri ()
specifier|protected
name|String
name|getUri
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isPersistent
argument_list|()
condition|?
name|path
else|:
name|path
operator|+
literal|"?dontPersist=true"
return|;
block|}
DECL|method|createRemoteAssetTree ()
specifier|protected
name|AssetTree
name|createRemoteAssetTree
parameter_list|()
block|{
name|String
index|[]
name|urls
init|=
name|addresses
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
return|return
operator|new
name|VanillaAssetTree
argument_list|()
operator|.
name|forRemoteAccess
argument_list|(
name|urls
argument_list|,
name|configuration
operator|.
name|getWireType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

