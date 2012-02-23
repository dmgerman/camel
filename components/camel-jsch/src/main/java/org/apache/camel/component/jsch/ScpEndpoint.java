begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsch
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
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
name|component
operator|.
name|file
operator|.
name|GenericFileProducer
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
name|file
operator|.
name|remote
operator|.
name|RemoteFileConfiguration
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
name|file
operator|.
name|remote
operator|.
name|RemoteFileConsumer
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
name|file
operator|.
name|remote
operator|.
name|RemoteFileEndpoint
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
name|file
operator|.
name|remote
operator|.
name|RemoteFileOperations
import|;
end_import

begin_comment
comment|/**  * Secure Copy Endpoint  */
end_comment

begin_class
DECL|class|ScpEndpoint
specifier|public
class|class
name|ScpEndpoint
extends|extends
name|RemoteFileEndpoint
argument_list|<
name|ScpFile
argument_list|>
block|{
DECL|method|ScpEndpoint ()
specifier|public
name|ScpEndpoint
parameter_list|()
block|{     }
DECL|method|ScpEndpoint (String uri, JschComponent component, RemoteFileConfiguration configuration)
specifier|public
name|ScpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JschComponent
name|component
parameter_list|,
name|RemoteFileConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|ScpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
operator|(
name|ScpConfiguration
operator|)
name|this
operator|.
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|buildConsumer (Processor processor)
specifier|protected
name|RemoteFileConsumer
argument_list|<
name|ScpFile
argument_list|>
name|buildConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
literal|null
return|;
comment|// new ScpConsumer(this, processor, createRemoteFileOperations());
block|}
DECL|method|buildProducer ()
specifier|protected
name|GenericFileProducer
argument_list|<
name|ScpFile
argument_list|>
name|buildProducer
parameter_list|()
block|{
return|return
operator|new
name|ScpProducer
argument_list|(
name|this
argument_list|,
name|createRemoteFileOperations
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createRemoteFileOperations ()
specifier|public
name|RemoteFileOperations
argument_list|<
name|ScpFile
argument_list|>
name|createRemoteFileOperations
parameter_list|()
block|{
name|ScpOperations
name|operations
init|=
operator|new
name|ScpOperations
argument_list|()
decl_stmt|;
name|operations
operator|.
name|setEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|operations
return|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"scp"
return|;
block|}
annotation|@
name|Override
DECL|method|getTempFileName ()
specifier|public
name|Expression
name|getTempFileName
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creation of temporary files not supported by the scp: protocol."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

